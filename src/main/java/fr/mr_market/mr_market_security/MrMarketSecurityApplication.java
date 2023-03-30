package fr.mr_market.mr_market_security;

import fr.mr_market.mr_market_security.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class})
@EnableFeignClients
public class MrMarketSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MrMarketSecurityApplication.class, args);
    }

}
