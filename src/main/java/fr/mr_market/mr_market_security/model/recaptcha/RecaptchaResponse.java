package fr.mr_market.mr_market_security.model.recaptcha;

public record RecaptchaResponse(Boolean success,String challege_ts,String hostname,Double score, String action) {
}
