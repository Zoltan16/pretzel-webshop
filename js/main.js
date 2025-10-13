// main.js - UI behaviors (checkout flow, product detail render)
document.addEventListener('DOMContentLoaded', ()=>{

// Update navbar with user info (if auth.js is loaded)
  if(typeof authService !== 'undefined'){
    authService.updateNavbar();
  }

  // render home products (some selection)
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
    checkoutForm.addEventListener('submit', (e)=>{
      e.preventDefault();
      // collect data

      let userEmail = '';
      let userId = null;
      if(typeof authService !== 'undefined' && authService.isAuthenticated()){
        userEmail = authService.getUserEmail();
        const user = authService.getCurrentUser();
        userId = user.id || null;
      }

      const data = {
        name: document.getElementById('name').value,
        email: document.getElementById('email').value,
        address: document.getElementById('address').value,
        city: document.getElementById('city').value,
        zip: document.getElementById('zip').value,
        phone: document.getElementById('phone').value,
        payment: document.getElementById('payment').value,
        cart: cartItemsDetailed(),
        total: cartTotal()
      };
      // normally send to server; here simulate
      localStorage.setItem('last_order', JSON.stringify(data));
      clearCart();
      window.location.href = 'success.html';
    });
  }

  // update cart count badges initially
  updateCartCount();
});
