package com.huntercodexs.oauth2serverdemo.repository;

import com.huntercodexs.oauth2serverdemo.model.OperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<OperatorEntity, Long> {
    OperatorEntity findByUsername(String name);
}
