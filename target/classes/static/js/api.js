const API_BASE = 'http://localhost:8080';

let PRODUCTS = [];

// Fetch products from backend
async function fetchProducts() {
    try {
        const response = await fetch(`${API_BASE}/api/products`);
        if (!response.ok) throw new Error('Failed to fetch products');
        const backendProducts = await response.json();

        // Map backend structure to frontend structure
        const mappedProducts = backendProducts.map(p => ({
            id: p.id.toString(),
            name: p.name,
            price: p.price,
            type: p.type,
            img: p.imageUrl,
            desc: p.description
        }));

        console.log('Loaded ' + mappedProducts.length + ' products from backend');
        return mappedProducts;
    } catch (error) {
        console.error('Error fetching products:', error);
        // Fallback to hardcoded products if backend is down
        return [];
    }
}

// Initialize products on page load
async function initializeProducts() {
    PRODUCTS = await fetchProducts();
    window.PRODUCTS = PRODUCTS; // Make globally available
        return PRODUCTS;
}

function findProductById(id) {
    return PRODUCTS.find(p => String(p.id) === String(id)); //
}

// Export for use in other files
window.fetchProducts = fetchProducts;
window.initializeProducts = initializeProducts;
window.findProductById = findProductById;