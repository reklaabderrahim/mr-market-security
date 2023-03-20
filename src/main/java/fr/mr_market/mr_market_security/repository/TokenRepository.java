package fr.mr_market.mr_market_security.repository;

import fr.mr_market.mr_market_security.model.token.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = """
            select t from Token t inner join AuthUser u\s
            on t.authUser.id = u.id\s
            where u.id = :id and t.isRevoked = false\s
            """)
    List<Token> findAllValidTokenByUser(Long id);

    Optional<Token> findByToken(String token);
}
