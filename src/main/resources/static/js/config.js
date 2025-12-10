
// auth service.
class AuthService {
    constructor() {
        this.token = localStorage.getItem('token');
        this.user = JSON.parse(localStorage.getItem('user'));
    }

    // Uj felhasználó regisztrálása
    async register(email, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/register`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            } else {
                return { success: false, message: data.message };
            }
        } catch (error) {
            console.error('Regisztrációs hiba:', error);
            return { success: false, message: 'Regisztráció sikertelen!' };
        }
    }

    // felhasználó beléptetése
    async login(email, password) {
        try {
            const response = await fetch(`${API_BASE_URL}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password })
            });

            const data = await response.json();

            if (data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            } else {
                return { success: false, message: data.message };
            }
        } catch (error) {
            console.error('Login error:', error);
            return { success: false, message: 'Login failed. Please try again.' };
        }
    }

    // Vendég belépés
    async guestLogin() {
        try {
            const response = await fetch(`${API_BASE_URL}/guest`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                }
            });

            const data = await response.json();

            if (data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            } else {
                return { success: false, message: data.message };
            }
        } catch (error) {
            console.error('Vendég belépés hiba:', error);
            return { success: false, message: 'Vendég belépés sikertelen!' };
        }
    }

    // hitelesítési adat mentése
    saveAuthData(token, user) {
        this.token = token;
        this.user = user;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    }

    // kilépés
    logout() {
        this.token = null;
        this.user = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }

    // belépés ellenörzés
    isAuthenticated() {
        return !!this.token;
    }

    // jelenlegi felhasználó lekérésée
    getCurrentUser() {
        return this.user;
    }

    // token kérése
    getToken() {
        return this.token;
    }

    // API kérés küldése
    async fetchWithAuth(url, options = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers,
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return fetch(url, {
            ...options,
            headers
        });
    }

// vendég ellenörzés
    isGuest() {
        return this.user && this.user.isGuest === true;
    }

    // email kérése
    getUserEmail() {
        return this.user ? this.user.email : null;
    }

    // navbar frissítése
    updateNavbar() {
        const navbar = document.querySelector('.navbar .container');
        if (!navbar) return;

        // Remove existing user section if any
        const existingUserSection = navbar.querySelector('.user-section');
        if (existingUserSection) {
            existingUserSection.remove();
        }

        if (this.isAuthenticated()) {
            const user = this.user;
            const isGuest = this.isGuest();

            // felhasználó szekció....
            const userSection = document.createElement('div');
            userSection.className = 'user-section d-flex align-items-center gap-2 me-2';

            userSection.innerHTML = `
                <span class="text-muted small d-none d-md-inline">
                    <i class="fa-solid fa-user"></i>
                    ${isGuest ? 'Vendég' : user.email}
                    ${isGuest ? '<span class="badge bg-warning text-dark ms-1">Vendég</span>' : ''}
                </span>
                <button class="btn btn-sm btn-outline-secondary" onclick="authService.logout()">
                    <i class="fa-solid fa-right-from-bracket"></i>
                    <span class="d-none d-md-inline">Kilépés</span>
                </button>
            `;


            const cartBtn = navbar.querySelector('.btn-outline-dark');
            if (cartBtn) {
                cartBtn.parentElement.insertBefore(userSection, cartBtn);
            } else {
                navbar.appendChild(userSection);
            }
        } else {

            const userSection = document.createElement('div');
            userSection.className = 'user-section me-2';

            userSection.innerHTML = `
                <a href="login.html" class="btn btn-sm btn-primary">
                    <i class="fa-solid fa-right-to-bracket"></i> Bejelentkezés
                </a>
            `;


            const cartBtn = navbar.querySelector('.btn-outline-dark');
            if (cartBtn) {
                cartBtn.parentElement.insertBefore(userSection, cartBtn);
            } else {
                navbar.appendChild(userSection);
            }
        }
    }

    // hitelesítés kérése
    requireAuth(redirectUrl = 'checkout.html') {
        if (!this.isAuthenticated()) {
            if (confirm('A vásárláshoz bejelentkezés szükséges. Szeretnél bejelentkezni?')) {
                localStorage.setItem('redirect_after_login', redirectUrl);
                window.location.href = 'login.html';
            }
            return false;
        }
        return true;
    }

    // átíránytás kezelése
    handleLoginRedirect() {
        const redirect = localStorage.getItem('redirect_after_login');
        if (redirect) {
            localStorage.removeItem('redirect_after_login');
            return redirect;
        }
        return 'index.html';
    }
}

const authService = new AuthService();
window.authService = authService;