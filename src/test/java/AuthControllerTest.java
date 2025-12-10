

import com.pretzel.backend.controller.AuthController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController underTest;

    @BeforeEach
    void setup() {
        underTest = new AuthController();
    }


    @Test
    void testRegisterShouldReturnBadRequestWhenMissingFields() {
        Map<String, String> body = new HashMap<>();
        body.put("email", null);
        body.put("password", null);

        ResponseEntity<?> response = underTest.register(body);

        assertEquals(400, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals(false, result.get("success"));
        assertEquals("Missing fields", result.get("message"));
    }

    @Test
    void testRegisterShouldReturnUserExistsWhenEmailAlreadyUsed() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "test@example.com");
        body.put("password", "pw");

        // első regisztráció
        underTest.register(body);
        // második regisztráció ugyanazzal
        ResponseEntity<?> response = underTest.register(body);

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals(false, result.get("success"));
        assertEquals("User already exists", result.get("message"));
    }

    @Test
    void testRegisterShouldCreateUserWhenValidInput() {

        Map<String, String> body = new HashMap<>();
        body.put("email", "new@example.com");
        body.put("password", "pw");

        ResponseEntity<?> response = underTest.register(body);

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();

        assertEquals(true, result.get("success"));
        assertNotNull(result.get("token"));

        Map<?, ?> user = (Map<?, ?>) result.get("user");
        assertEquals("new@example.com", user.get("email"));
        assertEquals(false, user.get("isGuest"));
    }


    @Test
    void testLoginShouldReturnBadRequestWhenMissingFields() {
        Map<String, String> body = new HashMap<>();
        body.put("email", null);
        body.put("password", null);

        ResponseEntity<?> response = underTest.login(body);

        assertEquals(400, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals(false, result.get("success"));
        assertEquals("Missing fields", result.get("message"));
    }

    @Test
    void testLoginShouldReturnInvalidCredentialsWhenWrongPassword() {
        Map<String, String> reg = new HashMap<>();
        reg.put("email", "test@example.com");
        reg.put("password", "pw");

        underTest.register(reg);

        Map<String, String> body = new HashMap<>();
        body.put("email", "test@example.com");
        body.put("password", "wrong");

        ResponseEntity<?> response = underTest.login(body);

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();
        assertEquals(false, result.get("success"));
        assertEquals("Invalid credentials", result.get("message"));
    }

    @Test
    void testLoginShouldReturnSuccessWhenCorrectCredentials() {
        Map<String, String> reg = new HashMap<>();
        reg.put("email", "test@example.com");
        reg.put("password", "pw");
        underTest.register(reg);

        Map<String, String> body = new HashMap<>();
        body.put("email", "test@example.com");
        body.put("password", "pw");

        ResponseEntity<?> response = underTest.login(body);

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();

        assertEquals(true, result.get("success"));
        assertNotNull(result.get("token"));

        Map<?, ?> user = (Map<?, ?>) result.get("user");
        assertEquals("test@example.com", user.get("email"));
        assertEquals(false, user.get("isGuest"));
    }


    @Test
    void testGuestShouldCreateGuestUserWhenCalled() {
        ResponseEntity<?> response = underTest.guest();

        assertEquals(200, response.getStatusCode().value());
        Map<?, ?> result = (Map<?, ?>) response.getBody();

        assertEquals(true, result.get("success"));
        assertNotNull(result.get("token"));

        Map<?, ?> user = (Map<?, ?>) result.get("user");
        assertTrue(((String) user.get("email")).contains("guest_"));
        assertEquals(true, user.get("isGuest"));
    }
}
