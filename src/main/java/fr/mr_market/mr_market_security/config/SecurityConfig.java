package fr.mr_market.mr_market_security.config;

import fr.mr_market.mr_market_security.security.RestAuthenticationEntryPoint;
import fr.mr_market.mr_market_security.security.oauth2.CustomOAuth2UserService;
import fr.mr_market.mr_market_security.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import fr.mr_market.mr_market_security.security.oauth2.OAuth2AuthenticationFailureHandler;
import fr.mr_market.mr_market_security.security.oauth2.OAuth2AuthenticationSuccessHandler;
import fr.mr_market.mr_market_security.service.RecaptchaService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final RecaptchaService recaptchaService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors().disable().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .formLogin().disable().httpBasic().disable().exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint()).and()
//                .authorizeHttpRequests()
//                    .requestMatchers("/",
//                            "/error",
//                            "/favicon.ico",
//                            "/**/*.png",
//                            "/**/*.gif",
//                            "/**/*.svg",
//                            "/**/*.jpg",
//                            "/**/*.html",
//                            "/**/*.css",
//                            "/**/*.js")
//                    .permitAll()
//                    .and()
                .authorizeHttpRequests().requestMatchers("/api/v1/auth/**", "/api/v1/oauth2/**").permitAll().anyRequest().authenticated().and().
                authenticationProvider(authenticationProvider).oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                .addFilterBefore(new RecaptchaFilter(recaptchaService), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login().authorizationEndpoint().baseUri("/api/v1/oauth2/authorize")
                .authorizationRequestRepository(cookieAuthorizationRequestRepository()).and()
                .redirectionEndpoint().baseUri("/api/v1/oauth2/callback/*").and().
                userInfoEndpoint().userService(customOAuth2UserService).and()
                .successHandler(oAuth2AuthenticationSuccessHandler).failureHandler(oAuth2AuthenticationFailureHandler).and().
                logout().logoutUrl("/api/v1/auth/logout").addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());


        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource(AppProperties appProperties) {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(appProperties.getCors().getAllowedOrigins());
        configuration.setAllowedMethods(appProperties.getCors().getAllowedMethods());
        configuration.setAllowedHeaders(appProperties.getCors().getAllowedHeaders());
        configuration.setMaxAge(appProperties.getCors().getMaxAge());
        configuration.setAllowCredentials(appProperties.getCors().getAllowCredentials());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

