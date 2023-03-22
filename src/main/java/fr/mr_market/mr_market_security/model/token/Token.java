package fr.mr_market.mr_market_security.model.token;

import fr.mr_market.mr_market_security.model.BaseEntity;
import fr.mr_market.mr_market_security.model.user.AuthUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token extends BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_seq")
  @SequenceGenerator(name = "token_id_seq", sequenceName = "token_id_seq", allocationSize = 1)
  private Long id;

  @Column(unique = true)
  public String token;

  @Enumerated(EnumType.STRING)
  public TokenType tokenType;

  public boolean isRevoked;

  @ManyToOne
  @JoinColumn(name = "auth_user_id")
  public AuthUser authUser;

  public static Token create(String inputToken, TokenType tokenType, boolean isRevoked) {
    Token token = new Token();
    token.setUuid(UUID.randomUUID());
    token.setCreateDate(LocalDateTime.now());
    token.setToken(inputToken);
    token.setTokenType(tokenType);
    token.setRevoked(isRevoked);
    return token;
  }

  public static Token create(String inputToken, TokenType tokenType, boolean isRevoked, AuthUser authUser) {
    Token token = create(inputToken, tokenType, isRevoked);
    token.setAuthUser(authUser);
    return token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Token Token)) return false;
    return Objects.equals(getUuid(), Token.getUuid());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUuid());
  }
}
