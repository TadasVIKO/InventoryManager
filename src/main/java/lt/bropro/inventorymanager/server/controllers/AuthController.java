package lt.bropro.inventorymanager.server.controllers;

import lombok.RequiredArgsConstructor;
import lt.bropro.inventorymanager.server.middleware.AuthResponse;
import lt.bropro.inventorymanager.server.middleware.AuthRequest;
import lt.bropro.inventorymanager.server.middleware.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.authenticate(authRequest));
    }
}
