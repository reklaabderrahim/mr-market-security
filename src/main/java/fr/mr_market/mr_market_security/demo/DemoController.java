package fr.mr_market.mr_market_security.demo;

import fr.mr_market.mr_market_security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
@RequiredArgsConstructor
public class DemoController {

    private CustomUserDetailsService userDetailsService;

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<String> admin() {
        return ResponseEntity.ok("Hello from secured endpoint: ADMIN");
    }

    @GetMapping("/user")
    @PreAuthorize("hasAuthority('SCOPE_USER')")
    public ResponseEntity<String> user() {
        return ResponseEntity.ok("Hello from secured endpoint: USER");
    }
}
