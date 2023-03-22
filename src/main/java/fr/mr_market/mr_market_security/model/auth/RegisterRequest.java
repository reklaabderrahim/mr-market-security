package fr.mr_market.mr_market_security.model.auth;

import fr.mr_market.mr_market_security.model.user.AuthProvider;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private AuthProvider provider;
    private String providerId;
    private String imageUrl;
}
