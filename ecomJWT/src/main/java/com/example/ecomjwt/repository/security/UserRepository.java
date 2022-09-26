package com.example.ecomjwt.repository.security;

import com.example.ecomjwt.model.security.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserModel,Integer> {

    UserModel findByEmail(String email);

    UserModel findByUserName(String username);

}
