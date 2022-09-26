package com.example.ecomjwt;

import com.example.ecomjwt.model.Product;
import com.example.ecomjwt.model.security.RoleModel;
import com.example.ecomjwt.model.security.UserModel;
import com.example.ecomjwt.service.ProductService;
import com.example.ecomjwt.service.security.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class EcomJwtApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomJwtApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService, ProductService productService){
		return args -> {
			userService.saveRole(new RoleModel(1,"ROLE_USER"));
			userService.saveRole(new RoleModel(2,"ROLE_MANAGER"));
			userService.saveRole(new RoleModel(3,"ROLE_ADMIN"));
			userService.saveRole(new RoleModel(4,"ROLE_SUPER_ADMIN"));

			userService.saveUser(new UserModel(1,"jack","jack","jack",new ArrayList<>()));
			userService.saveUser(new UserModel(2,"eric","eric","eric",new ArrayList<>()));
			userService.saveUser(new UserModel(3,"buzz","buzz","buzz",new ArrayList<>()));

			userService.addRoleToUser("eric","ROLE_ADMIN");
			userService.addRoleToUser("eric","ROLE_USER");
			userService.addRoleToUser("eric","ROLE_MANAGER");
			userService.addRoleToUser("jack","ROLE_USER");
			userService.addRoleToUser("jack","ROLE_MANAGER");
			userService.addRoleToUser("buzz","ROLE_USER");
			userService.addRoleToUser("buzz","ROLE_SUPER_ADMIN");

			productService.newProduct(new Product(1,"Gundam2","Season 2 gundam",9999) );
			productService.newProduct(new Product(2,"Gundam 0","Gundam Zero special edition",9999) );
			productService.newProduct(new Product(3,"Gundam Wing","Gundam wing best toys",699) );
			productService.newProduct(new Product(4,"Gundam Unicorn","Gundam Unicorn special edition",7999) );

		};
	}
}
