package com.tmsjsb.redpanda.Service.ServiceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.UserRepository;
import com.tmsjsb.redpanda.Service.AuthService;

import java.net.URLDecoder;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

  private final UserRepository userRepository;

  private AuthServiceImpl(UserRepository userRepository) {
      this.userRepository = userRepository;
  }

  @Override
  public boolean validateJwt(String jwt){
    String token = jwt.split("Bearer ")[1];
    DecodedJWT decoded;

    try{
    decoded = decodeJWT(token);
    }
    catch(Exception e){
      return false;
    }
    
    String username = decoded.getClaim("username").asString();
    Optional<UserEntity> CheckUser = userRepository.findById(username);
    UserEntity ThisUser = CheckUser.get();
    
    return (ThisUser.getActiveStatus() == 1) ? true : false;
  }

  public static DecodedJWT decodeJWT(String jwt) throws JWTDecodeException {
    try {
        Algorithm algorithm = Algorithm.HMAC256("secret"); // Set your secret key here
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(jwt);
        return decodedJWT;
    } catch (Exception e) {
        throw new JWTDecodeException("Failed to decode JWT", e);
    }
}

  @Override
  public boolean CheckGroup(String username, String Group){
    return true;
  }
  
}
