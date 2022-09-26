package com.example.ecomjwt.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    // Attemp when user try to log in
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is {}",username);
        log.info("Password is {}",password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    // Precision !!! => Because use request parameter in postman we need to need use Body request and x-www-from-encoded
    // when access for login is with the column name !!!!
    // When login is unsuccefful .
    // And when succeful it generate JWT token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        // UserModel from userdetails not my UserModel
        // Return object user who is login
        User user = (User)authentication.getPrincipal();
        // Algo for signed token and refresh token
        // the string is to hide !!
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                // credential
                .withSubject(user.getUsername())
                //Time token
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000 ))
                //Author of token
                .withIssuer(request.getRequestURL().toString())
                // Send role to string
                .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                //to sign with the algorithm encryption
                .sign(algorithm);

        // no need for roles
        String refresh_token = JWT.create()
                // credential
                .withSubject(user.getUsername())
                //Time token
                .withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000 ))
                //Author of token
                .withIssuer(request.getRequestURL().toString())
                //to sign with the algorithm encryption
                .sign(algorithm);
        /*// This is for header
        response.setHeader("access_token",access_token);
        response.setHeader("refesh_token",refresh_token);*/

        // Send back as response body
        Map<String,String> token =new HashMap<>();
        token.put("access_token",access_token);
        token.put("refesh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),token);
    }

}
