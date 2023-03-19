package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.auth.AuthenticationRequest;
import fr.mr_market.mr_market_security.model.auth.AuthenticationResponse;
import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request);

}
