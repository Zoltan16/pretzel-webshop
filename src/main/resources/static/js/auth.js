// js/auth.js - authentication helper (front-end)
// Usage: include config.js BEFORE this file in HTML

class AuthService {
    constructor() {
        this.token = localStorage.getItem('token') || null;
        this.user = JSON.parse(localStorage.getItem('user') || 'null');
        this.apiBase = (window.CONFIG && window.CONFIG.API_BASE_URL) ? window.CONFIG.API_BASE_URL : 'http://localhost:8080/api/auth';
    }

    async register(email, password) {
        try {
            const res = await fetch(`${this.apiBase}/register`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password})
            });
            const data = await res.json();
            if (data && data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            }
            return { success: false, message: data?.message || 'Registration failed' };
        } catch (e) {
            console.error('register error', e);
            return { success: false, message: 'Network error' };
        }
    }

    async login(email, password) {
        try {
            const res = await fetch(`${this.apiBase}/login`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
                body: JSON.stringify({email, password})
            });
            const data = await res.json();
            if (data && data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            }
            return { success: false, message: data?.message || 'Login failed' };
        } catch (e) {
            console.error('login error', e);
            return { success: false, message: 'Network error' };
        }
    }

    async guestLogin() {
        try {
            const res = await fetch(`${this.apiBase}/guest`, {
                method: 'POST',
                headers: {'Content-Type': 'application/json'},
            });
            const data = await res.json();
            if (data && data.success) {
                this.saveAuthData(data.token, data.user);
                return { success: true, user: data.user };
            }
            return { success: false, message: data?.message || 'Guest login failed' };
        } catch (e) {
            console.error('guest login error', e);
            return { success: false, message: 'Network error' };
        }
    }

    saveAuthData(token, user) {
        this.token = token;
        this.user = user;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    }

    logout() {
        this.token = null;
        this.user = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        // update navbar and redirect to homepage
        this.updateNavbar();
        window.location.href = 'index.html';
    }

    isAuthenticated() {
        return !!this.token;
    }

    getCurrentUser() {
        return this.user;
    }

    getToken() {
        return this.token;
    }

    async fetchWithAuth(url, options = {}) {
        const headers = {'Content-Type': 'application/json', ...(options.headers || {})};
        if (this.token) headers['Authorization'] = `Bearer ${this.token}`;
        return fetch(url, {...options, headers});
    }

    isGuest() {
        return this.user && this.user.isGuest === true;
    }

    getUserEmail() {
        return this.user ? this.user.email : null;
    }

    updateNavbar() {
        const navbar = document.querySelector('.navbar .container');
        if (!navbar) return;
        const existing = navbar.querySelector('.user-section');
        if (existing) existing.remove();

        const cartBtn = navbar.querySelector('.btn-outline-dark');

        const userSection = document.createElement('div');
        userSection.className = 'user-section d-flex align-items-center gap-2 me-2';

        if (this.isAuthenticated()) {
            const user = this.user;
            const isGuest = this.isGuest();
            userSection.innerHTML = `
                <span class="text-muted small d-none d-md-inline">
                    <i class="fa-solid fa-user"></i>
                    ${isGuest ? 'Vendég' : escapeHtml(user.email)}
                    ${isGuest ? '<span class="badge bg-warning text-dark ms-1">Vendég</span>' : ''}
                </span>
                <button class="btn btn-sm btn-outline-secondary" id="logoutBtn">
                    <i class="fa-solid fa-right-from-bracket"></i>
                    <span class="d-none d-md-inline">Kilépés</span>
                </button>
            `;
        } else {
            userSection.innerHTML = `
                <a href="login.html" class="btn btn-sm btn-primary">
                    <i class="fa-solid fa-right-to-bracket"></i> Bejelentkezés
                </a>
            `;
        }

        if (cartBtn) {
            cartBtn.parentElement.insertBefore(userSection, cartBtn);
        } else {
            navbar.appendChild(userSection);
        }

        // attach logout handler if present
        const logoutBtn = document.getElementById('logoutBtn');
        if (logoutBtn) logoutBtn.addEventListener('click', () => this.logout());
    }

    requireAuth(redirectUrl = 'checkout.html') {
        if (!this.isAuthenticated()) {
            localStorage.setItem('redirect_after_login', redirectUrl);
            window.location.href = 'login.html';
            return false;
        }
        return true;
    }

    handleLoginRedirect() {
        const r = localStorage.getItem('redirect_after_login');
        if (r) {
            localStorage.removeItem('redirect_after_login');
            return r;
        }
        return 'index.html';
    }
}

// helper
function escapeHtml(s){ if(!s) return ''; return String(s).replaceAll('&','&amp;').replaceAll('<','&lt;').replaceAll('>','&gt;').replaceAll('"','&quot;'); }

const authService = new AuthService();
window.authService = authService;
