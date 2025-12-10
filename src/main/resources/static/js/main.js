// guard a main.js többszöri betöltése ellen
if (window.__pretzelMainLoaded) {
    console.warn('main már be van töltve (duplikált betöltés!!)');
} else {
    window.__pretzelMainLoaded = true;
}

document.addEventListener('DOMContentLoaded', async()=>{
await initializeProducts();
    // debug: console.log('termékadatok betöltve, képbetöltés kezdődik!');

    const homeEl = document.getElementById('home-products');
    if(homeEl){
        renderProducts('home-products', p=>p.type==='pretzel' || p.type==='dessert');
    }
    // kaja és merch lapok renderelése
    const foodEl = document.getElementById('food-products');
    if(foodEl) renderProducts('food-products', p=>p.type==='pretzel' || p.type==='dessert');

    const merchEl = document.getElementById('merch-products');
    if(merchEl) renderProducts('merch-products', p=>p.type==='merch');

    // termékadat lap
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

    // kosár rendelése a kosár lapon
    if(document.getElementById('cart-container')) renderCartTable('cart-container');

    // kosár ürítése gomb
    const clearBtn = document.getElementById('clear-cart');
    if(clearBtn) clearBtn.addEventListener('click', ()=>{ if(confirm('Biztosan üríted a kosarat?')){ clearCart(); renderCartTable('cart-container'); }});

    // kifizetési lap elküldése
    const checkoutForm = document.getElementById('checkout-form');
    if(checkoutForm){
        checkoutForm.addEventListener('submit', async (e)=>{
            e.preventDefault();

            // authtenikáció ellenörzése (regisztrációhoz kötött vásárlás miatt)
                    if(!authService.isAuthenticated()) {
                        alert('Kérlek jelentkez állandó vagy vendég profilodba a fizetéshez!');
                        authService.requireAuth('checkout.html');
                        return;
                    }
            const cart = getCart();

            // DEBUG console.log('Kosár tartalma: ', cart);

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


            // Adatok güyjtése a form-ból
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

// DEBUG console.log('rendelés elküldése:', data);


            try {
            // rendelés elküldése a backend-nek
            const response = await authService.fetchWithAuth('http://localhost:8080/api/orders', {
                method: 'POST',
                body: JSON.stringify(data)
            });

            const result = await response.json();

// DEBUG console.log('rendelés válasza:', result);

            if(result.success) {

            if(result.newKuponBalance !==undefined){
            authService.updateKupons(result.newKuponBalance);
            }

                            // kosar kiuritese
                            clearCart();
                            // rendelés id tarolasa
                            localStorage.setItem('last_order_id', result.orderId);

                            localStorage.setItem('utolso_vasarlas_utanni_szerzett_kuponok', result.kuponsEarned || 0);

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

    // kosár frissítése
    updateCartCount();
});
