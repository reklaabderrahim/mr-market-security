package fr.mr_market.mr_market_security.exception;

import org.apache.commons.lang3.StringUtils;

public class ResourceNotFoundException extends GenericException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String param) {
        super(StringUtils.replace(message, "{}", param));
    }
}
