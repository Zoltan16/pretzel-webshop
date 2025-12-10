package com.pretzel.backend.controller;

import com.pretzel.backend.model.User;
import com.pretzel.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    // in-memory felhasználói adattárolás
    private final Map<String, User> activeTokens = new HashMap<>();

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // Regisztáció
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if(email == null || password == null || email.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing fields"));
        }
        if(userRepository.existsByEmail(email))
        {
            return ResponseEntity.ok(Map.of("success", false, "message", "User already exists"));
        }

        User user = new User(email, password, false); // Plain text-es tarolas!!
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user);

        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("id", user.getId(), "email", user.getEmail(), "isGuest", false, "kupons", user.getKupons())
        ));
    }


    // bejelentkezés
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if(email == null || password == null) return ResponseEntity.badRequest().body(Map.of("success",false,"message","Missing fields"));
        Optional<User> userOpt = userRepository.findByEmail(email);
        if(userOpt.isEmpty() || !userOpt.get().getPassword().equals(password)) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid credentials"));
        }
        User user = userOpt.get();
        String token = UUID.randomUUID().toString();
        activeTokens.put(token, user);

        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("id", user.getId(), "email", user.getEmail(), "isGuest", user.isGuest(), "kupons", user.getKupons())
        ));
    }

    // vendég felhasználó
    @PostMapping("/guest")
    public ResponseEntity<?> guest() {
        String guestEmail = "guest_" + UUID.randomUUID().toString().substring(0,8) + "@pretzel.local";

        User guestUser = new User(guestEmail, "", true);
        userRepository.save(guestUser);

        String token = UUID.randomUUID().toString();
        activeTokens.put(token, guestUser);

        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("id", guestUser.getId(), "email", guestEmail, "isGuest", true, "kupons", 0)
        ));
    }
    //token validácios rész..
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.ok(Map.of("success", false, "message", "No token provided"));
        }

        String token = authHeader.substring(7);
        User user = activeTokens.get(token);

        if(user == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid token"));
        }

        return ResponseEntity.ok(Map.of("success", true, "user", Map.of("id", user.getId(), "email", user.getEmail(), "isGuest", user.isGuest(), "kupons", user.getKupons())
        ));
    }

    // Kijelentkezés
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            activeTokens.remove(token);
        }
        return ResponseEntity.ok(Map.of("success", true));
    }

    // user kinyerése tokenből
    public User getUserFromToken(String token) {
        return activeTokens.get(token);
    }
}
