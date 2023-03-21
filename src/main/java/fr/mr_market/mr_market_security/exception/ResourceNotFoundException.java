package fr.mr_market.mr_market_security.exception;

import org.apache.commons.lang3.StringUtils;

public class UserNotFoundException extends GenericException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, String param) {
        super(StringUtils.replace(message, "{}", param));
    }
}
