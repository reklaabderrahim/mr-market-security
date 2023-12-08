package fr.mr_market.mr_market_security.controller;

import fr.mr_market.mr_market_security.kafka.MessageProducer;
import fr.mr_market.mr_market_security.feign.NotificationFeignClient;
import fr.mr_market.mr_market_security.model.auth.AuthenticationResponse;
import fr.mr_market.mr_market_security.model.mail.MailRequest;
import fr.mr_market.mr_market_security.model.mail.MailType;
import fr.mr_market.mr_market_security.model.token.TokenType;
import fr.mr_market.mr_market_security.model.user.AuthProvider;
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
    private final MessageProducer messageProducer;

    @Override
    @Operation(operationId = "confirm")
    public ResponseEntity<Map<String, String>> confirm(String token, String email) {
        log.debug("controller:: confirm token: {}", token);
        return new ResponseEntity<>(authenticationService.confirm(token, email).getToken(), HttpStatus.OK);
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
        AuthenticationResponse authenticationResponse =
                authenticationService.register(registerMapper(registerRequest), Collections.singletonList(Role.USER), null
                );
        String callbackUrl = "http://localhost:4200/auth/confirm" + "?" +
                "token=" +
                authenticationResponse.getToken().get(TokenType.ACTIVATION_TOKEN.getValue()) +
                "&" +
                "email=" +
                registerRequest.getEmail();
        MailRequest mailRequest = new MailRequest(Set.of(registerRequest.getEmail()), MailType.ACCOUNT_ACTIVATION, callbackUrl);
        log.debug("controller:: send mail message: {}", mailRequest);
        messageProducer.sendEmailMessage("mail-register", mailRequest);

        Map<String, String> result = new HashMap<>();
        result.put("status", "User registered successfully@");
        return new ResponseEntity<>(result, HttpStatus.OK);
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
