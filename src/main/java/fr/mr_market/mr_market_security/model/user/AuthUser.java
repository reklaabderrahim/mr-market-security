package fr.mr_market.mr_market_security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.mr_market.mr_market_security.model.BaseEntity;
import fr.mr_market.mr_market_security.model.auth.RegisterRequest;
import fr.mr_market.mr_market_security.model.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_user")
public class AuthUser extends BaseEntity implements Serializable, UserDetails, OAuth2User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_id_seq")
    @SequenceGenerator(name = "auth_user_id_seq", sequenceName = "auth_user_id_seq", allocationSize = 1)
    private Long id;
    @Column(name = "first_name")
    @Size(max = 50)
    @NotBlank(message = "The first name is required")
    private String firstName;

    @Column(name = "last_name")
    @Size(max = 50)
    @NotBlank(message = "The last name is required")
    private String lastName;

    private String imageUrl;
    @Column(nullable = false)
    private Boolean emailVerified = false;
    @Column(name = "active")
    private boolean active;
    @Column(name = "email")
    @Size(max = 255)
    @Email
    private String email;
    @NotBlank
    @JsonIgnore
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;
    private String providerId;
    @Column(name = "login_date", columnDefinition = "DATE")
    private LocalDateTime loginDate;
    @OneToMany(mappedBy = "authUser", cascade = CascadeType.ALL)
    private List<AuthUserRole> authUserRoles;
    @OneToMany(mappedBy = "authUser")
    private List<Token> tokens;
    @Transient
    private Map<String, Object> attributes;

    public static AuthUser create(String firstName, String lastName, String email, String password, AuthProvider provider, String providerId, String imageUrl, List<Role> roles) {
        AuthUser authUser = AuthUser.update(firstName, lastName, email, password, provider, providerId, imageUrl, roles);
        authUser.setUuid(UUID.randomUUID());
        authUser.setCreateDate(LocalDateTime.now());
        authUser.setActive(true);
        return authUser;
    }

    public static AuthUser update(String firstName, String lastName, String email, String password, AuthProvider provider, String providerId, String imageUrl, List<Role> roles) {
        AuthUser authUser = new AuthUser();
        authUser.firstName = firstName;
        authUser.lastName = lastName;
        authUser.email = email;
        authUser.password = password;
        authUser.provider = provider;
        authUser.providerId = providerId;
        authUser.imageUrl = imageUrl;
        List<AuthUserRole> authUserRoleList = roles.stream().map(role -> AuthUserRole.builder().authUser(authUser).role(role).build()).collect(Collectors.toList());
        authUser.setAuthUserRoles(authUserRoleList);
        return authUser;
    }

    public static AuthUser create(RegisterRequest registerRequest, List<Role> roles, Map<String, Object> attributes) {
        AuthUser authUser = create(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getProvider(), registerRequest.getProviderId(), registerRequest.getImageUrl(), roles);
        authUser.setAttributes(attributes);
        return authUser;
    }

    public static AuthUser create(AuthUser user, List<Role> roles, Map<String, Object> attributes) {
        AuthUser authUser = create(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getProvider(), user.getProviderId(), user.getImageUrl(), roles);
        authUser.setAttributes(attributes);
        return authUser;
    }

    public static AuthUser update(RegisterRequest registerRequest, List<Role> roles, Map<String, Object> attributes) {
        AuthUser authUser = update(registerRequest.getFirstName(), registerRequest.getLastName(), registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getProvider(), registerRequest.getProviderId(), registerRequest.getImageUrl(), roles);
        authUser.setAttributes(attributes);
        return authUser;
    }

    public static AuthUser update(AuthUser user, List<Role> roles, Map<String, Object> attributes) {
        AuthUser authUser = update(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getProvider(), user.getProviderId(), user.getImageUrl(), roles);
        authUser.setAttributes(attributes);
        return authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authUserRoles.stream().map(auth -> new SimpleGrantedAuthority(auth.getRole().getValue().trim().toUpperCase())).collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isAccountNonLocked() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return active;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", AuthUser.class.getSimpleName() + "[", "]").add("firstName=" + firstName).add("lastName=" + lastName).add("email=" + email).add("loginDate=" + loginDate).add("active=" + active).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthUser authUser)) return false;
        return Objects.equals(getUuid(), authUser.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
