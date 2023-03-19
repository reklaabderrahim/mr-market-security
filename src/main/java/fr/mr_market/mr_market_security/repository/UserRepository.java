package fr.mr_market.mr_market_security.repository;

import fr.mr_market.mr_market_security.model.user.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AuthUser, Integer> {

  Optional<AuthUser> findByEmail(String email);

}
