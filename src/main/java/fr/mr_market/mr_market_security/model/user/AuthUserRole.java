package fr.mr_market.mr_market_security.model.user;

import fr.mr_market.mr_market_security.model.BaseEntity;
import fr.mr_market.mr_market_security.model.token.Token;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auth_user_role")
public class AuthUserRole implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auth_user_role_id_seq")
    @SequenceGenerator(name = "auth_user_role_id_seq", sequenceName = "auth_user_role_id_seq", allocationSize = 1)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "auth_user_id")
    public AuthUser authUser;

    public static AuthUserRole create(Role role) {
        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setRole(role);
        return authUserRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthUserRole authUserRole)) return false;
        return Objects.equals(id, authUserRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
