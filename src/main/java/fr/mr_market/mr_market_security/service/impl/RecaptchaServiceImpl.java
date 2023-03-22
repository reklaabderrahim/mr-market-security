package fr.mr_market.mr_market_security.service.impl;

import fr.mr_market.mr_market_security.config.AppProperties;
import fr.mr_market.mr_market_security.model.recaptcha.RecaptchaResponse;
import fr.mr_market.mr_market_security.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RecaptchaServiceImpl implements RecaptchaService {
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;

    public RecaptchaResponse validateToken(String recaptchaToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", appProperties.getRecaptcha().getSecretKey());
        map.add("response", recaptchaToken);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<RecaptchaResponse> response = restTemplate.exchange(appProperties.getRecaptcha().getVerifyUrl(), HttpMethod.POST, entity, RecaptchaResponse.class);

        return response.getBody();
    }

    @Override
    public boolean isActif() {
        return !appProperties.getRecaptcha().getDisabled();
    }
}
