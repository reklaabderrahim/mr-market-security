package fr.mr_market.mr_market_security.service;

import fr.mr_market.mr_market_security.model.recaptcha.RecaptchaResponse;

public interface RecaptchaService {
    RecaptchaResponse validateToken(String recaptchaToken);

    boolean isActif();
}
