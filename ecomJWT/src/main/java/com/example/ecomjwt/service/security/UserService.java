package com.example.ecomjwt.service.security;

import com.example.ecomjwt.model.security.RoleModel;
import com.example.ecomjwt.model.security.UserModel;


import java.util.List;

public interface UserService {

    UserModel saveUser(UserModel userModel);

    RoleModel saveRole(RoleModel roleModel);

    void addRoleToUser(String username, String name);

    UserModel getUser(String username);

    List<UserModel> getUsers();

}
