package com.example.PraktikaSS.repositories;

import com.example.PraktikaSS.models.RememberMeToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RememberMeTokenRepository extends JpaRepository<RememberMeToken, Long> {
    Optional<RememberMeToken> findBySeries(String series);

    void deleteByUsername(String username);
}
