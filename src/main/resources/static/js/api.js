const API_BASE = 'http://localhost:8080';

let PRODUCTS = [];

// Termék adatok betöltése a backend-ről
async function fetchProducts() {
    try {
        const response = await fetch(`${API_BASE}/api/products`);
        if (!response.ok) throw new Error('Hiba történt a termékek betöltése során');
        const backendProducts = await response.json();

        // backend struktúra feltérképzése és átalakítása a front-end struktúrájára
        const mappedProducts = backendProducts.map(p => ({
            id: p.id.toString(),
            name: p.name,
            price: p.price,
            type: p.type,
            img: p.imageUrl,
            desc: p.description
        }));

        // DEBUG console.log(mappedProducts.length + ' Termék betöltve a háttérszerverről !');
        return mappedProducts;
    } catch (error) {
        console.error('Hiba történt a termékek betöltése során:', error);
        // ha nincs szerver, az erededti adattömböt használjuk a termékhekhez
        return [];
    }
}

// termékek inicializálása a lap betöltésekkor
async function initializeProducts() {
    PRODUCTS = await fetchProducts();
    window.PRODUCTS = PRODUCTS; // termékek tömb globálisan elérhető legyen
        return PRODUCTS;
}

function findProductById(id) {
    return PRODUCTS.find(p => String(p.id) === String(id));
}

// Más fájlok is tudják ezeket használni..
window.fetchProducts = fetchProducts;
window.initializeProducts = initializeProducts;
window.findProductById = findProductById;