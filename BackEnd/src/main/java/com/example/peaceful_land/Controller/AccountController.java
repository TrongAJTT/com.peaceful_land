package com.example.peaceful_land.Controller;


import com.example.peaceful_land.DTO.RegisterRequest;
import com.example.peaceful_land.Service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/accounts")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping("/login")
    public String login(String userId, String password) {
        // TODO: Hưng, làm đê
        return accountService.tryLogin(userId, password);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest userInfo) {
        try {
            return ResponseEntity.ok(accountService.register(userInfo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
