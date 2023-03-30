package fr.mr_market.mr_market_security.controller;

import fr.mr_market.mr_market_security.feign.NotificationFeignClient;
import fr.mr_market.mr_market_security.model.mail.MailRequest;
import fr.mr_market.mr_market_security.model.mail.MailType;
import fr.mr_market.mr_market_security.model.user.AuthProvider;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.Role;
import fr.mr_market.mr_market_security.service.AuthenticationService;
import fr.mr_market.mr_market_security.swagger.AuthApi;
import fr.mr_market.mr_market_security.swagger.model.authUser.AuthenticationRequest;
import fr.mr_market.mr_market_security.swagger.model.authUser.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class AuthController implements AuthApi {

    private final AuthenticationService authenticationService;
    private final NotificationFeignClient notificationFeignClient;
    private final HttpServletRequest httpServletRequest;

    @Override
    @Operation(operationId = "confirm")
    public ResponseEntity<Map<String, String>> confirm(String token) {
        log.debug("controller:: confirm token: {}", token);
        return AuthApi.super.confirm(token);
    }

    @Override
    @Operation(operationId = "login")
    public ResponseEntity<Map<String, String>> login(AuthenticationRequest authenticationRequest) {
        log.debug("controller:: login user: {}", authenticationRequest.getMail());
        return new ResponseEntity<>(authenticationService.authenticate(authenticationMapper(authenticationRequest)).getToken(), HttpStatus.OK);
    }

    @Override
    @Operation(operationId = "refresh")
    public ResponseEntity<Map<String, String>> refresh() {
        log.debug("controller:: refresh token");
        return new ResponseEntity<>(authenticationService.refreshToken(httpServletRequest).getToken(), HttpStatus.OK);
    }

    @Override
    @Operation(operationId = "register")
    public ResponseEntity<Map<String, String>> register(RegisterRequest registerRequest) {
        log.debug("controller:: create user: {}", registerRequest);
        AuthUser register =
                authenticationService.register(registerMapper(registerRequest), Collections.singletonList(Role.USER), null
                );
        MailRequest mailRequest = new MailRequest(Set.of(registerRequest.getEmail()), MailType.ACCOUNT_ACTIVATION, "https://google.fr");
        notificationFeignClient.sendTestReport(mailRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(register.getId()).toUri();

        Map<String, String> result = new HashMap<>();
        result.put("status", "User registered successfully@");
        return ResponseEntity.created(location)
                .body(result);
    }

    private fr.mr_market.mr_market_security.model.auth.RegisterRequest registerMapper(RegisterRequest registerRequest) {
        fr.mr_market.mr_market_security.model.auth.RegisterRequest request = new fr.mr_market.mr_market_security.model.auth.RegisterRequest();
        request.setFirstName(registerRequest.getFirstName());
        request.setLastName(registerRequest.getLastName());
        request.setEmail(registerRequest.getEmail());
        request.setPassword(registerRequest.getPassword());
        request.setProvider(AuthProvider.local);
        request.setProviderId("local");
        return request;
    }

    private fr.mr_market.mr_market_security.model.auth.AuthenticationRequest authenticationMapper(AuthenticationRequest authenticationRequest) {
        fr.mr_market.mr_market_security.model.auth.AuthenticationRequest request = new fr.mr_market.mr_market_security.model.auth.AuthenticationRequest();
        request.setEmail(authenticationRequest.getMail());
        request.setPassword(authenticationRequest.getPassword());
        return request;
    }
}
