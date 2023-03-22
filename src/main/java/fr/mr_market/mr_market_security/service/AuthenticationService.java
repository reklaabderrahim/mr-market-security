package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.auth.AuthenticationRequest;
import fr.mr_market.mr_market_security.model.auth.AuthenticationResponse;
import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import fr.mr_market.mr_market_security.model.user.AuthUserRole;
import fr.mr_market.mr_market_security.model.user.Role;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface AuthenticationService {

    AuthUser register(RegisterRequest request, List<Role> roles, Map<String, Object> attributes);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request);

}
