package fr.mr_market.mr_market_security.config;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DozerConfig {

    @Bean(name = "DozerMapper")
    public Mapper dozerBean() {
        return DozerBeanMapperBuilder.create().withMappingFiles("classpath:dozer/dozerConfiguration.xml").build();
    }
}
