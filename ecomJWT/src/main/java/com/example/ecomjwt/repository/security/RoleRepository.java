package com.example.ecomjwt.repository.security;

import com.example.ecomjwt.model.security.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface RoleRepository extends JpaRepository<RoleModel,Integer> {

    RoleModel findByName(String role);
}
