// guard a main.js többszöri betöltése ellen
if (window.__pretzelMainLoaded) {
    console.warn('pretzel main already loaded, skipping duplicate initialization');
} else {
    window.__pretzelMainLoaded = true;
}

document.addEventListener('DOMContentLoaded', async()=>{
await initializeProducts();
    console.log('Products loaded, ready to render');

    const homeEl = document.getElementById('home-products');
    if(homeEl){
        renderProducts('home-products', p=>p.type==='pretzel' || p.type==='dessert');
    }
    // render food and merch pages
    const foodEl = document.getElementById('food-products');
    if(foodEl) renderProducts('food-products', p=>p.type==='pretzel' || p.type==='dessert');

    const merchEl = document.getElementById('merch-products');
    if(merchEl) renderProducts('merch-products', p=>p.type==='merch');

    // product detail page
    const params = new URLSearchParams(window.location.search);
    const pid = params.get('id');
    if(pid && document.getElementById('product-detail')){
        const p = findProductById(pid);
        if(p){
            document.getElementById('product-detail').innerHTML = `
        <div class="col-md-6"><img src="${p.img}" class="img-fluid rounded" alt="${p.name}"></div>
        <div class="col-md-6">
          <h2>${p.name}</h2>
          <p>${p.desc}</p>
          <h4>${p.price} Ft</h4>
          <div class="d-flex gap-2 mt-3">
            <button class="btn btn-primary" onclick="addToCart('${p.id}',1)">Kosárba</button>
            <a href="shop-food.html" class="btn btn-light">Vissza</a>
          </div>
        </div>
      `;
        }
    }

    // render cart on cart page
    if(document.getElementById('cart-container')) renderCartTable('cart-container');

    // clear cart button
    const clearBtn = document.getElementById('clear-cart');
    if(clearBtn) clearBtn.addEventListener('click', ()=>{ if(confirm('Biztosan üríted a kosarat?')){ clearCart(); renderCartTable('cart-container'); }});

    // checkout form submit
    const checkoutForm = document.getElementById('checkout-form');
    if(checkoutForm){
        checkoutForm.addEventListener('submit', async (e)=>{
            e.preventDefault();

            // Check authentication
                    if(!authService.isAuthenticated()) {
                        alert('Please login or continue as guest to complete checkout');
                        authService.requireAuth('checkout.html');
                        return;
                    }
            const cart = getCart();

            console.log('Cart contents: ', cart); // Debug

            if(!cart || cart.length === 0) {
                        alert('A kosarad üres!');
                        return;
                    }

        let kuponsUsed = 0;
                if(!authService.isGuest()) {
                    const kuponInput = document.getElementById('kupons-used-checkout');
                    if(kuponInput) {
                        kuponsUsed = parseInt(kuponInput.value) || 0;
                    }
                }


            // collect data (this is demo only)
            const data = {
                name: document.getElementById('name').value,
                email: document.getElementById('email').value,
                address: document.getElementById('address').value,
                city: document.getElementById('city').value,
                zip: document.getElementById('zip').value,
                phone: document.getElementById('phone').value,
                payment: document.getElementById('payment').value,
                cart: cart,
                total: cartTotal(),
                kuponsUsed: kuponsUsed
            };

console.log('Sending order:', data); // Debug log


            try {
            // Send order to backend
            const response = await authService.fetchWithAuth('http://localhost:8080/api/orders', {
                method: 'POST',
                body: JSON.stringify(data)
            });

            const result = await response.json();

console.log('Order response:', result); // Debug log

            if(result.success) {

            if(result.newKuponBalance !==undefined){
            authService.updateKupons(result.newKuponBalance);
            }

                            // kosar kiuritese
                            clearCart();
                            // rendes id tarolasa
                            localStorage.setItem('last_order_id', result.orderId);

                            localStorage.setItem('last_order_kupons_earned', result.kuponsEarned || 0);

                            // sikeres lapra iranyitas
                            window.location.href = 'success.html';
                        } else {
                            alert('Order failed: ' + result.message);
                        }
                    } catch(error) {
                        console.error('Checkout error:', error);
                        alert('Failed to place order. Please try again.');
                    }
        });
    }

    // update cart count badges initially
    updateCartCount();
});
