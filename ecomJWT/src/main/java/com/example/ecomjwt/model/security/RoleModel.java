package com.example.ecomjwt.model.security;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "roles")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,property = "id")
public class RoleModel {

        @Id
        @GeneratedValue(strategy= GenerationType.IDENTITY)
        @Column(name="id")
        private int id;
        @Column(name="name")
        private String name;

        @ManyToMany(mappedBy = "roles")
        private Collection<UserModel> users = new ArrayList<>();

        public RoleModel() {
        }

        public RoleModel(String name) {
                this.name = name;
        }

        public RoleModel(int id, String name) {
                this.id = id;
                this.name = name;
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }
}
