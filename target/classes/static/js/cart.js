//ha már van CART_KEY, ne deklaráljuk újra
window.CART_KEY = window.CART_KEY || 'kosár';

function getCart(){
    const raw = localStorage.getItem(window.CART_KEY);
    return raw ? JSON.parse(raw) : [];
}

function saveCart(cart){ localStorage.setItem(window.CART_KEY, JSON.stringify(cart)); updateCartCount(); }

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

function clearCart(){ localStorage.removeItem(window.CART_KEY); updateCartCount(); }

function cartItemsDetailed(){
    const cart = getCart();
    return cart.map(i=>{ const p = findProductById(i.id); return {...p, qty:i.qty}; });
}

function cartTotal(){
    return cartItemsDetailed().reduce((s,it)=>s + (it.price * it.qty), 0);
}

// használni kívánt kuponok számának kivétele
function getKuponsToUse() {
    const kuponInput = document.getElementById('kupons-to-use');
    if(!kuponInput) return 0;
    const value = parseInt(kuponInput.value) || 0;
    return Math.max(0, Math.min(value, 50)); // 0 és 50 közötti mennyiségű kupont használhatunk egy vásárlás során
}




// Kosárban megadjuk a kuponos árt is majd itt, de a kosarat frisítjük itt
function updateCartTotals() {
    const originalTotal = cartTotal();
    const kuponsUsed = getKuponsToUse();
    const discount = originalTotal * (kuponsUsed / 100.0); // 1 kupon-->1% kedvezmény
    const finalTotal = originalTotal - discount;

    const cartTotalEl = document.getElementById('cart-total');
    const discountEl = document.getElementById('cart-discount');
    const finalTotalEl = document.getElementById('cart-final-total');
    const discountRow = document.getElementById('discount-row');



    if(cartTotalEl) cartTotalEl.textContent = originalTotal.toFixed(0) + ' Ft';
    if(discountEl) discountEl.textContent = '-' + discount.toFixed(0) + ' Ft';
    if(finalTotalEl) finalTotalEl.textContent = finalTotal.toFixed(0) + ' Ft';

if(discountRow)
{
discountRow.style.display =kuponsUsed>0 ? '' : 'none';
}
localStorage.setItem('cart_kupons_used', kuponsUsed);
}



function updateCartCount(){
    const count = getCart().reduce((s,i)=>s+i.qty,0);
    document.querySelectorAll('#cart-count').forEach(el=>el.textContent = count);
}

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

// termékek és kosár rendelését kezelő rész
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
            <button class="kosarbtn btn btn-outline-primary w-100" onclick="addToCart('${p.id}',1)">Kosárba:<br> ${p.price} Ft</button>
            <a href="product.html?id=${p.id}" class="btn btn-light kozepre">Részletek</a>
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

    if(items.length===0){
    el.innerHTML = '<p>A kosarad üres.</p>';
    const cartTotalEl = document.getElementById('cart-total');
    const finalTotalEl= document.getElementById('cart-final-total');
     if(cartTotalEl) cartTotalEl.textContent= '0 Ft';
     if(finalTotalEl) finalTotalEl.textContent = '0 FT';
            return;
}


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

const isRegistered = authService.isAuthenticated() && !authService.isGuest();
    if(isRegistered) {
        const userKupons = authService.getKupons();
        html += `
        <div class="card mb-3">
            <div class="card-body">
                <h5 class="card-title">
                    <i class="fa-solid fa-ticket"></i> Kupon használat
                </h5>
                <p class="text-muted">Rendelkezésre álló kuponok száma: <strong>${userKupons}</strong></p>
                <p class="text-muted small">Minden kupon 1% kedvezményt jelent. Minden vásárlás során maximum 50 kupon használható.</p>
                <div class="input-group" style="max-width: 300px;">
                    <input type="number"
                           id="kupons-to-use"
                           class="form-control"
                           min="0"
                           max="${Math.min(userKupons, 50)}"
                           value="0"
                           placeholder="Kuponok száma">
                    <button class="btn btn-primary" id="apply-kupons">Alkalmaz</button>
                </div>
            </div>
        </div>
        `;
    }

    el.innerHTML = html;

    // handlerek hozzáadása
    el.querySelectorAll('.remove-btn').forEach(b=>b.addEventListener('click', e=>{ removeFromCart(e.currentTarget.dataset.id); renderCartTable(containerId); }));
    el.querySelectorAll('.qty-input').forEach(inp=> inp.addEventListener('change', e=>{ updateQty(e.currentTarget.dataset.id, parseInt(e.currentTarget.value)||1); renderCartTable(containerId); }));

const applyBtn = document.getElementById('apply-kupons');
    if(applyBtn) {
        applyBtn.addEventListener('click', () => {
            updateCartTotals();
        });
    }

    const kuponInput = document.getElementById('kupons-to-use');
    if(kuponInput) {
        kuponInput.addEventListener('input', () => {
            updateCartTotals();
        });
    }

    // kosár totál frisítése
    updateCartTotals();
}


window.addEventListener('storage', updateCartCount);
document.addEventListener('DOMContentLoaded', updateCartCount);
