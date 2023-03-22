package fr.mr_market.mr_market_security.exception;

import org.apache.commons.lang3.StringUtils;

public class BadRequestException extends GenericException {
    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadRequestException(String message, String param) {
        super(StringUtils.replace(message, "{}", param));
    }
}
