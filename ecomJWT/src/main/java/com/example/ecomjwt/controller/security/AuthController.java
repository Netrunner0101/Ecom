package com.example.ecomjwt.controller.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecomjwt.model.security.RoleModel;
import com.example.ecomjwt.model.security.UserModel;
import com.example.ecomjwt.service.security.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel userModel){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/userModel/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(userModel));
    }

    @PostMapping("/role/save")
    public ResponseEntity<RoleModel> saveRole(@RequestBody RoleModel roleModel){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roleModel/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(roleModel));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
        // Add role
        userService.addRoleToUser(form.getUsername(), form.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                /**
                 * Need algorithm
                 * **/
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT= verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                // Check if the user exist
                UserModel user = userService.getUser(username);
                // Create a new token
                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000 ))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(RoleModel::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> token =new HashMap<>();
                token.put("access_token",access_token);
                token.put("refesh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),token);
            }catch (Exception e){
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                //response.sendError(FORBIDDEN.value());
                Map<String,String> error =new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }

}

@Data
class RoleToUserForm{
    private String username;
    private String name;
}