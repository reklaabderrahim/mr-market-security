package fr.mr_market.mr_market_security.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String param) {
        super(StringUtils.replace(message, "{}", param));
    }
}
