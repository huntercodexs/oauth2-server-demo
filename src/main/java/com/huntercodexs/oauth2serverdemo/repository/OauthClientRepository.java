package com.huntercodexs.oauth2serverdemo.repository;

import com.huntercodexs.oauth2serverdemo.model.OauthClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthClientRepository extends JpaRepository<OauthClientEntity, Long> {
	OauthClientEntity findByClient(String client);
}
