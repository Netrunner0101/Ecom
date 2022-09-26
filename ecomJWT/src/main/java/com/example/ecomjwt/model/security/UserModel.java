package com.example.ecomjwt.model.security;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "users")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class UserModel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="username")
    private String userName;
    @Column(name="email")
    private String email;
    @Column(name="password")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private Collection<RoleModel> roles = new ArrayList<>();

    public UserModel() {
    }

    public UserModel(String userName, String email, String password, Collection<RoleModel> roles) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserModel(Integer id, String userName, String email, String password, Collection<RoleModel> roles) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<RoleModel> getRoles() {
        return roles;
    }

    public void setRoles(Collection<RoleModel> roleModels) {
        this.roles = roleModels;
    }
}
