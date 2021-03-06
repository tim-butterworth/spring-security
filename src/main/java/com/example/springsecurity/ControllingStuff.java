package com.example.springsecurity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

@RestController
@RequestMapping("/authenticationProcessing")
public class ControllingStuff {

    @GetMapping("/hi")
    public ResponseEntity<String> sayHi(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.setLocation(new URI("http://google.com"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        response.setHeader("key", "value");

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
    }

    @GetMapping(path = "/bye")
    public ResponseEntity<String> sayBye() {
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

        for (GrantedAuthority authority : authorities) {
            System.out.println(authority.getAuthority());
        }

        return ResponseEntity.ok("You shouldn't have done that...");
    }

    @GetMapping(path = "/info")
    public ResponseEntity<String> getSession() {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();

        return ResponseEntity.ok(session.getId());
    }

}
