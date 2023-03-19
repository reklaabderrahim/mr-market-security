package fr.mr_market.mr_market_security.model.user;

import fr.mr_market.mr_market_security.model.BaseEntity;
import fr.mr_market.mr_market_security.model.Gender;
import fr.mr_market.mr_market_security.model.token.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_user")
public class AuthUser extends BaseEntity implements Serializable, UserDetails {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;
    @Column(name = "email")
    @Size(max = 255)
    @Email
    private String email;
    @NotBlank
    //@Size(min = 8, max = 20)
    // @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")
    private String password;
    @Column(name = "login_date", columnDefinition = "DATE")
    private LocalDateTime loginDate;
    @Column(name = "active")
    private boolean active;
    @OneToMany(mappedBy = "authUser")
    private List<AuthUserRole> authUserRoles;
    @OneToMany(mappedBy = "authUser")
    private List<Token> tokens;

    public static AuthUser create(String firstName, String lastName, Gender gender, LocalDate birthDate, String mail) {
        AuthUser authUser = new AuthUser();
        authUser.setUuid(UUID.randomUUID());
        authUser.setCreateDate(LocalDateTime.now());
        authUser.setActive(true);
        authUser.firstName = firstName;
        authUser.lastName = lastName;
        authUser.email = mail;
        authUser.gender = gender;
        authUser.birthDate = birthDate;
        return authUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return (this.authUserRoles != null) ? new ArrayList<>(this.authUserRoles) : null;
    }

    @Override
    public String getPassword() {
        return password;
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
        return new StringJoiner(", ", AuthUser.class.getSimpleName() + "[", "]").add("firstName=" + firstName).add("lastName=" + lastName).add("gender=" + gender).add("birthDate=" + birthDate).add("email=" + email).add("loginDate=" + loginDate).add("active=" + active).toString();
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
}
