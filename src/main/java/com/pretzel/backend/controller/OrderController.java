package com.pretzel.backend.controller;

import com.pretzel.backend.model.*;
import com.pretzel.backend.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final AuthController authController;

    public OrderController(OrderRepository orderRepository,
                           UserRepository userRepository,
                           ProductRepository productRepository,
                           AuthController authController) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.authController = authController;
    }

    //rendeles elkészítése
    @PostMapping
    public ResponseEntity<?> createOrder(

            @RequestHeader(value = "Authorization", required = false) String authHeader,
            @RequestBody Map<String, Object> orderData) {

        // Debug
        //System.out.println("Beérkezett rendelés adatok: " + orderData);

        // hitelesítés ellenörzése
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Hitelesítés szükséges!"));
        }

        String token = authHeader.substring(7);
        User user = authController.getUserFromToken(token);

        if(user == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Helytelen token!"));
        }



        try {
            Integer kuponsToUse = 0;
            if (orderData.containsKey("kuponsUsed")) {
                Object kuponObj = orderData.get("kuponsUsed");
                if (kuponObj instanceof Number) {
                    kuponsToUse = ((Number) kuponObj).intValue();
                } else if (kuponObj instanceof String) {
                    kuponsToUse = Integer.parseInt((String) kuponObj);
                }
            }
            if (kuponsToUse > 50) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Maximum 50 kupont tudsz egy vásárlás során használni!"));
            }
            if (!user.isGuest() && kuponsToUse > user.getKupons()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Nincs elég kuponod! Összesen " + user.getKupons()+ "kuponod van!"));
            }
            if(user.isGuest() && kuponsToUse > 0) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Vendég felhasználók nem használhatnak kuponokat!"));
            }

            // Rendelés tényleges létrehozása
            Order order = new Order();
            order.setUser(user);
            order.setCustomerName((String) orderData.get("name"));
            order.setAddress((String) orderData.get("address"));
            order.setCity((String) orderData.get("city"));
            order.setZip((String) orderData.get("zip"));
            order.setPhone((String) orderData.get("phone"));
            order.setPaymentMethod((String) orderData.get("payment"));

            // Kosár tartalmának feldolgozása
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cartItems = (List<Map<String, Object>>) orderData.get("cart");

            if(cartItems == null || cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "A kosarad üres!"));
            }

            double subtotal = 0;
            for(Map<String, Object> item : cartItems) {
                try {
                    String productIdStr = item.get("id").toString();
                    if (productIdStr.startsWith("p") || productIdStr.startsWith("m")) {
                        productIdStr = productIdStr.substring(1);
                    }
                    Long productId = Long.parseLong(productIdStr);


                    Integer quantity = null;
                    Object qtyObj = item.get("qty");

                    if (qtyObj instanceof Integer) {
                        quantity = (Integer) qtyObj;
                    } else if (qtyObj instanceof Number) {
                        quantity = ((Number) qtyObj).intValue();
                    } else if (qtyObj instanceof String) {
                        quantity = Integer.parseInt((String) qtyObj);
                    }
                    if (quantity == null || quantity <= 0) {
                        System.err.println("Nem megfelelő termékszám " + productId);
                    }

                    Optional<Product> productOpt = productRepository.findById(productId);
                    if (productOpt.isPresent()) {
                        Product product = productOpt.get();
                        OrderItem orderItem = new OrderItem(product, quantity);
                        order.addItem(orderItem);
                        subtotal += orderItem.getSubtotal();
                    } else {
                        System.err.println("Hibás termék nem található: " + item);
                    }
                } catch (Exception e) {
                    System.err.println("A következő kosár elemet nem sikerült feldolgozni: " + item);
                    e.printStackTrace();
                }
            }
            if (order.getItems().isEmpty())
            {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Nincs érvényes termék a  kosárban!"));
            }

            order.setOriginalAmount(subtotal);
            double discount= subtotal * (kuponsToUse /100.0);
            double finalTotal= subtotal - discount;
            order.setTotalAmount(finalTotal);

            int kuponsEarned = (int) Math.floor(finalTotal/500.0);
            //jelenleg a kuponos ár a mérvadó a kapott kuponok adása esetén (átírható subtotal-ra is)

            order.setKuponsEarned(kuponsEarned);

            if(!user.isGuest()){
                user.useKupons(kuponsToUse);
                user.addKupons(kuponsEarned);
                userRepository.save(user);
            }

            orderRepository.save(order);

            System.out.println("Rendelés sikeresen létrehozva! Rendelés száma: " + order.getId());
            System.out.println("Használt kuponok száma: " + kuponsToUse + " Kapott kuponok száma: " + kuponsEarned);
            System.out.println("Felhasználó új kuponegyenlege: " + user.getKupons());

            return ResponseEntity.ok(Map.of("success", true, "orderId", order.getId(), "message", "Rendelés sikeresen létrehozva!", "kuponsUsed", kuponsToUse, "newKuponBalance", user.getKupons(), "kuponsEarned", kuponsEarned, "discount", discount, "finalTotal", finalTotal
            ));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Rendelés létrehozása sikertelen: " + e.getMessage()
            ));
        }
    }

    // Felhasználó rendelései
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Hitelesítés szükséges!"));
        }

        String token = authHeader.substring(7);
        User user = authController.getUserFromToken(token);

        if(user == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Helytelen token!"));
        }

        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);

        // Frontedes igazítás
        List<Map<String, Object>> orderList = new ArrayList<>();
        for(Order order : orders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("id", order.getId());
            orderMap.put("customerName", order.getCustomerName());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("status", order.getStatus());
            orderMap.put("createdAt", order.getCreatedAt().toString());
            orderMap.put("itemCount", order.getItems().size());
            orderList.add(orderMap);
        }

        return ResponseEntity.ok(Map.of("success", true, "orders", orderList));
    }

    // rendelés adatainak elérése
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetails(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Hitelesítés szükséges!"));
        }

        String token = authHeader.substring(7);
        User user = authController.getUserFromToken(token);

        if(user == null) {
            return ResponseEntity.status(401).body(Map.of("success", false, "message", "Helytelen token!"));
        }

        Optional<Order> orderOpt = orderRepository.findById(id);
        if(orderOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("success", false, "message", "Rendelés nem található!  "));
        }

        Order order = orderOpt.get();

        // A rendelés felhasználóhoz való tartozásának vizsgálata
        if(!order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Hozzáférés megtagadva!"));
        }

        // Adatok kiadása a megrendelésről
        Map<String, Object> orderDetails = new HashMap<>();
        orderDetails.put("id", order.getId());
        orderDetails.put("customerName", order.getCustomerName());
        orderDetails.put("address", order.getAddress());
        orderDetails.put("city", order.getCity());
        orderDetails.put("zip", order.getZip());
        orderDetails.put("phone", order.getPhone());
        orderDetails.put("paymentMethod", order.getPaymentMethod());
        orderDetails.put("totalAmount", order.getTotalAmount());
        orderDetails.put("status", order.getStatus());
        orderDetails.put("createdAt", order.getCreatedAt().toString());

        List<Map<String, Object>> items = new ArrayList<>();
        for(OrderItem item : order.getItems()) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("productName", item.getProduct().getName());
            itemMap.put("quantity", item.getQuantity());
            itemMap.put("price", item.getPriceAtPurchase());
            itemMap.put("subtotal", item.getSubtotal());
            items.add(itemMap);
        }
        orderDetails.put("items", items);

        return ResponseEntity.ok(Map.of("success", true, "order", orderDetails));
    }
}
