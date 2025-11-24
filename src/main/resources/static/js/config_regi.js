// auth.js - Place this in your frontend project


// Authentication Service
class AuthService {
    constructor() {
        this.token = localStorage.getItem('token');
        this.user = JSON.parse(localStorage.getItem('user'));
    }

    // Register new user
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
            console.error('Registration error:', error);
            return { success: false, message: 'Registration failed. Please try again.' };
        }
    }

    // Login existing user
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

    // Guest login
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
            console.error('Guest login error:', error);
            return { success: false, message: 'Guest login failed. Please try again.' };
        }
    }

    // Save authentication data
    saveAuthData(token, user) {
        this.token = token;
        this.user = user;
        localStorage.setItem('token', token);
        localStorage.setItem('user', JSON.stringify(user));
    }

    // Logout
    logout() {
        this.token = null;
        this.user = null;
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }

    // Check if user is logged in
    isAuthenticated() {
        return !!this.token;
    }

    // Get current user
    getCurrentUser() {
        return this.user;
    }

    // Get token for API requests
    getToken() {
        return this.token;
    }

    // Make authenticated API requests
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

// Check if user is guest
    isGuest() {
        return this.user && this.user.isGuest === true;
    }

    // Get user email
    getUserEmail() {
        return this.user ? this.user.email : null;
    }

    // Update navbar with user info (call this on every page)
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

            // Create user section
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

            // Insert before cart button
            const cartBtn = navbar.querySelector('.btn-outline-dark');
            if (cartBtn) {
                cartBtn.parentElement.insertBefore(userSection, cartBtn);
            } else {
                navbar.appendChild(userSection);
            }
        } else {
            // Show login button if not authenticated
            const userSection = document.createElement('div');
            userSection.className = 'user-section me-2';

            userSection.innerHTML = `
                <a href="login.html" class="btn btn-sm btn-primary">
                    <i class="fa-solid fa-right-to-bracket"></i> Bejelentkezés
                </a>
            `;

            // Insert before cart button
            const cartBtn = navbar.querySelector('.btn-outline-dark');
            if (cartBtn) {
                cartBtn.parentElement.insertBefore(userSection, cartBtn);
            } else {
                navbar.appendChild(userSection);
            }
        }
    }

    // Require authentication (useful for checkout page)
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

    // Handle redirect after login
    handleLoginRedirect() {
        const redirect = localStorage.getItem('redirect_after_login');
        if (redirect) {
            localStorage.removeItem('redirect_after_login');
            return redirect;
        }
        return 'index.html'; // default
    }
}

// Export singleton instance
const authService = new AuthService();
window.authService = authService;