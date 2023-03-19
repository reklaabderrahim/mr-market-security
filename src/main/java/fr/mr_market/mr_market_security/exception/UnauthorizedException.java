package fr.mr_market.mr_market_security.exception;

import org.apache.commons.lang3.StringUtils;

public class UnauthorizedException extends GenericException {

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String param) {
        super(StringUtils.replace(message, "{}", param));
    }
}
