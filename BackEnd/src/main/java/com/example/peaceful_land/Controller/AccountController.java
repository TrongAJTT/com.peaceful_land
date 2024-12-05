package com.example.peaceful_land.Controller;

import com.example.peaceful_land.DTO.PurchaseRoleRequest;
import com.example.peaceful_land.ErrorHandler.RestExceptionHandler;
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

    @PostMapping("/purchase-role")
    public ResponseEntity<?> purchaseRole(@RequestBody PurchaseRoleRequest request) {
        try {
            return ResponseEntity.ok(accountService.purchaseRole(request));
        }
        catch (Exception e) {
            RestExceptionHandler restExceptionHandler = new RestExceptionHandler();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
