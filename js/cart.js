// cart.js - handles cart in localStorage
const CART_KEY = 'pretzel_cart_v1';

function getCart(){ 
  const raw = localStorage.getItem(CART_KEY);
  return raw ? JSON.parse(raw) : [];
}

function saveCart(cart){ localStorage.setItem(CART_KEY, JSON.stringify(cart)); updateCartCount(); }

function addToCart(productId, qty=1){
  const prod = findProductById(productId);
  if(!prod) return;
  const cart = getCart();
  const existing = cart.find(i=>i.id===productId);
  if(existing){ existing.qty += qty; } else { cart.push({id: productId, qty}); }
  saveCart(cart);
  showProductToast(prod);
}

function removeFromCart(productId){
  let cart = getCart();
  cart = cart.filter(i=>i.id!==productId);
  saveCart(cart);
}

function updateQty(productId, qty){
  const cart = getCart();
  const item = cart.find(i=>i.id===productId);
  if(item){ item.qty = qty; if(item.qty<=0) removeFromCart(productId); saveCart(cart); }
}

function clearCart(){ localStorage.removeItem(CART_KEY); updateCartCount(); }

function cartItemsDetailed(){
  const cart = getCart();
  return cart.map(i=>{ const p = findProductById(i.id); return {...p, qty:i.qty}; });
}

function cartTotal(){
  return cartItemsDetailed().reduce((s,it)=>s + (it.price * it.qty), 0);
}

function updateCartCount(){
  const count = getCart().reduce((s,i)=>s+i.qty,0);
  document.querySelectorAll('#cart-count').forEach(el=>el.textContent = count);
}

// toast per type
function showProductToast(product){
  const icons = { pretzel: '🥨', dessert: '🧁', merch: '🎁' };
  const icon = icons[product.type] || '🛒';
  const text = `${icon} ${product.name} hozzáadva a kosaradhoz!`;
  showToast(text);
}

function showToast(message){
  const container = document.getElementById('toast-container');
  if(!container) return;
  const toast = document.createElement('div');
  toast.className = 'toast-bakery mb-2';
  toast.innerHTML = `<div class="me-2">${message}</div>`;
  container.appendChild(toast);
  setTimeout(()=>{ toast.classList.add('hide'); toast.style.opacity=0; setTimeout(()=>toast.remove(),400); }, 3000);
}

// Utility for pages to render cart or products
function renderProducts(selector, filterFn){
  const el = document.getElementById(selector);
  if(!el) return;
  const items = typeof filterFn === 'function' ? PRODUCTS.filter(filterFn) : PRODUCTS;
  el.innerHTML = items.map(p=>`
    <div class="col-md-6 col-lg-4">
      <div class="card shadow-sm">
        <img src="${p.img}" class="card-img-top" alt="${p.name}">
        <div class="card-body">
          <h5 class="card-title">${p.name}</h5>
          <p class="card-text">${p.desc}</p>
          <div class="d-flex gap-2">
            <button class="btn btn-outline-primary w-100" onclick="addToCart('${p.id}',1)">Kosárba — ${p.price} Ft</button>
            <a href="product.html?id=${p.id}" class="btn btn-light">Részletek</a>
          </div>
        </div>
      </div>
    </div>
  `).join('');
}

function renderCartTable(containerId){
  const el = document.getElementById(containerId);
  if(!el) return;
  const items = cartItemsDetailed();
  if(items.length===0){ el.innerHTML = '<p>A kosarad üres.</p>'; document.getElementById('cart-total').textContent = '0 Ft'; return; }
  let html = `<table class="table"><thead><tr><th>Termék</th><th>Ár</th><th>Mennyiség</th><th>Részösszeg</th><th></th></tr></thead><tbody>`;
  for(const it of items){
    html += `<tr>
      <td>${it.name}</td>
      <td>${it.price} Ft</td>
      <td><input type="number" min="1" value="${it.qty}" class="form-control qty-input" data-id="${it.id}" style="width:100px;"></td>
      <td>${it.price * it.qty} Ft</td>
      <td><button class="btn btn-sm btn-outline-danger remove-btn" data-id="${it.id}">Törlés</button></td>
    </tr>`;
  }
  html += `</tbody></table>`;
  el.innerHTML = html;
  document.getElementById('cart-total').textContent = cartTotal() + ' Ft';
  // attach handlers
  el.querySelectorAll('.remove-btn').forEach(b=>b.addEventListener('click', e=>{ removeFromCart(e.target.dataset.id); renderCartTable(containerId); }));
  el.querySelectorAll('.qty-input').forEach(inp=> inp.addEventListener('change', e=>{ updateQty(e.target.dataset.id, parseInt(e.target.value)||1); renderCartTable(containerId); }));
}

window.addEventListener('storage', updateCartCount);
document.addEventListener('DOMContentLoaded', updateCartCount);
