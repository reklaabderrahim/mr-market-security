package fr.mr_market.mr_market_security.repository;

import fr.mr_market.mr_market_security.model.token.Token;
import fr.mr_market.mr_market_security.model.token.TokenType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join AuthUser u\s
            on t.authUser.id = u.id\s
            where u.id = :id and t.isRevoked = false\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByTokenAndAuthUser_emailAndTokenTypeIn(String token, String email, List<TokenType> tokenTypes);

    Optional<Token> findByTokenAndTokenTypeIn(String token, List<TokenType> tokenTypes);
}
