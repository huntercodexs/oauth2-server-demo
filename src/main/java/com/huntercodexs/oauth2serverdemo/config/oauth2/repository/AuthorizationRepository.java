package com.huntercodexs.oauth2serverdemo.config.oauth2.repository;

import com.huntercodexs.oauth2serverdemo.config.oauth2.model.AuthorizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<AuthorizationEntity, Long> {
	AuthorizationEntity findByClient(String client);
}
