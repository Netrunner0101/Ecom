package com.example.ecomjwt.service.security;

import com.example.ecomjwt.model.security.RoleModel;
import com.example.ecomjwt.model.security.UserModel;
import com.example.ecomjwt.repository.security.RoleRepository;
import com.example.ecomjwt.repository.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUserName(username);
        if(userModel ==null){
            log.error("UserModel nor found");
            throw  new UsernameNotFoundException("UserModel nor found");
        }else{
            log.info("UserModel is found");
        }

     /*   Create a new collection for simple granted authorites
        than call role from userModel that i create not the UserModel(userdetails)
        loop trough role and add authorities
      */
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        userModel.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(userModel.getUserName(), userModel.getPassword(),authorities);
    }

    @Override
    public UserModel saveUser(UserModel userModel) {
        log.info("Save new userModel : "+ userModel);
        // Get userModel model
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    @Override
    public RoleModel saveRole(RoleModel roleModel) {
        log.info("Save new RoleModel : "+ roleModel);
        return roleRepository.save(roleModel);
    }

    @Override
    public void addRoleToUser(String username, String name) {
        log.info("Add new roleModel: {} to userModel : {} ", name,username );
        UserModel userModel = userRepository.findByUserName(username);
        RoleModel roleModel = roleRepository.findByName(name);
        userModel.getRoles().add(roleModel);
    }

    @Override
    public UserModel getUser(String username) {
        log.info("Get user : "+username);
        return userRepository.findByUserName(username);
    }

    @Override
    public List<UserModel> getUsers() {
        log.info("Get list of user.");
        return userRepository.findAll();
    }


}
