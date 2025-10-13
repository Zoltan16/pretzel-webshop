package com.pretzel.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // simple in-memory user store for dev only
    private final Map<String, Map<String,Object>> users = new HashMap<>();

    // register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if(email == null || password == null) return ResponseEntity.badRequest().body(Map.of("success",false,"message","Missing fields"));
        if(users.containsKey(email)) return ResponseEntity.ok(Map.of("success", false, "message", "User already exists"));
        Map<String,Object> u = new HashMap<>();
        u.put("email", email);
        u.put("password", password); // WARNING: plain text, dev only
        u.put("isGuest", false);
        users.put(email, u);
        String token = UUID.randomUUID().toString();
        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("email", email, "isGuest", false)));
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {
        String email = body.get("email");
        String password = body.get("password");
        if(email == null || password == null) return ResponseEntity.badRequest().body(Map.of("success",false,"message","Missing fields"));
        Map<String,Object> u = users.get(email);
        if(u == null || !Objects.equals(u.get("password"), password)) {
            return ResponseEntity.ok(Map.of("success", false, "message", "Invalid credentials"));
        }
        String token = UUID.randomUUID().toString();
        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("email", email, "isGuest", false)));
    }

    // guest
    @PostMapping("/guest")
    public ResponseEntity<?> guest() {
        String guestEmail = "guest_" + UUID.randomUUID().toString().substring(0,8) + "@pretzel.local";
        Map<String,Object> u = new HashMap<>();
        u.put("email", guestEmail);
        u.put("isGuest", true);
        String token = UUID.randomUUID().toString();
        // store guest for session (optional)
        users.put(guestEmail, u);
        return ResponseEntity.ok(Map.of("success", true, "token", token, "user", Map.of("email", guestEmail, "isGuest", true)));
    }
}
