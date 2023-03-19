package fr.mr_market.mr_market_security.controller;

import com.github.dozermapper.core.Mapper;
import fr.mr_market.mr_market_security.service.AuthenticationService;
import fr.mr_market.mr_market_security.swagger.AuthApi;
import fr.mr_market.mr_market_security.swagger.model.authUser.AuthenticationRequest;
import fr.mr_market.mr_market_security.swagger.model.authUser.RegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class AuthController implements AuthApi {

    private final Mapper mapper;
    private final AuthenticationService authenticationService;
    private HttpServletRequest httpServletRequest;

    public AuthController(AuthenticationService authenticationService, Mapper mapper) {
        this.authenticationService = authenticationService;
        this.mapper = mapper;
    }

    @Override
    @Operation(operationId = "login")
    public ResponseEntity<Map<String, String>> login(AuthenticationRequest authenticationRequest) {
        log.debug("controller:: login user: {}", authenticationRequest.getMail());
        fr.mr_market.mr_market_security.model.auth.AuthenticationRequest request = mapper.map(authenticationRequest, fr.mr_market.mr_market_security.model.auth.AuthenticationRequest.class);
        return new ResponseEntity<>(authenticationService.authenticate(request).getToken(), HttpStatus.OK);
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
        fr.mr_market.mr_market_security.model.auth.RegisterRequest request = mapper.map(registerRequest, fr.mr_market.mr_market_security.model.auth.RegisterRequest.class);
        return new ResponseEntity<>(authenticationService.register(request).getToken(), HttpStatus.CREATED);
    }
}
