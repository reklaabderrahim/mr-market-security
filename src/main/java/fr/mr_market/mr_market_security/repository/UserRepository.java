package fr.mr_market.mr_market_security.repository;

import fr.mr_market.mr_market_security.model.user.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Integer> {

  Optional<AuthUser> findByEmail(String email);
  Boolean existsByEmail(String email);
}
