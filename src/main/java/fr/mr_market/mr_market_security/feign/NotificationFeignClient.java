package fr.mr_market.mr_market_security.feign;

import feign.Headers;
import fr.mr_market.mr_market_security.model.mail.MailRequest;
import fr.mr_market.mr_market_security.model.mail.MailResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", url ="http://localhost:8085")
public interface NotificationFeignClient {
    @PostMapping("/notification/api/v1/mailProcessing")
    @Headers("Content-Type: application/json")
    ResponseEntity<MailResponse> sendEmail(@RequestBody MailRequest request);
}
