package com.example.springsecurity;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class SessionClearingController {

    @GetMapping("/clearSession")
    public ResponseEntity<String> clearSession(HttpServletRequest request) {
        request.getSession().invalidate();
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Successfully cleared the session");
    }

}
