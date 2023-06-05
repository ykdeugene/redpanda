package com.tmsjsb.redpanda.Service.ServiceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tmsjsb.redpanda.Entity.GroupKeyEntity;
import com.tmsjsb.redpanda.Entity.GroupsEntity;
import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.UserRepository;
import com.tmsjsb.redpanda.Repository.GroupsRepository;
import com.tmsjsb.redpanda.Service.AuthService;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

  private final UserRepository userRepository;
  private final GroupsRepository groupsRepository;

  private AuthServiceImpl(UserRepository userRepository, GroupsRepository groupsRepository) {
      this.userRepository = userRepository;
      this.groupsRepository = groupsRepository;
  }

  @Override
  public boolean validateJwt(String jwt, String SourceipAddress, String SourcebrowserType){
    String token = jwt.split("Bearer ")[1]; // jwt = "Bearer xyz123abc" => token = "xyz123abc"
    DecodedJWT decoded;

    try{
    decoded = decodeJWT(token);
    }
    catch(Exception e){
      return false;
    }
    
    String username = decoded.getClaim("username").asString();
    String ipAddress = decoded.getClaim("ipAddress").asString();
    String browserType = decoded.getClaim("browserType").asString();
    System.out.println(username +" "+ browserType +" "+ ipAddress);
    System.out.println("source "+ SourcebrowserType +" "+ SourceipAddress);
    if (ipAddress.equals(SourceipAddress) && browserType.equals(SourcebrowserType)){
      Optional<UserEntity> CheckUser = userRepository.findById(username);
      UserEntity ThisUser = CheckUser.get();
      
      return (ThisUser.getActiveStatus() == 1) ? true : false;
    }
    else return false;
  }

  private DecodedJWT decodeJWT(String jwt) throws JWTDecodeException {
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
  public String TokenToUsername(String jwt){
    String token = jwt.split("Bearer ")[1]; // jwt = "Bearer xyz123abc" => token = "xyz123abc"
    DecodedJWT decoded;

    try{
    decoded = decodeJWT(token);
    }
    catch(Exception e){
      return "token error";
    }
    
    String username = decoded.getClaim("username").asString();
    return username;
  }

  @Override
  public boolean CheckGroup(String username, String Group){
    GroupKeyEntity usergroup = new GroupKeyEntity();
    usergroup.setGroupName(Group);
    usergroup.setUsername(username);

    Optional<GroupsEntity> CheckUser = groupsRepository.findById(usergroup);
    boolean result = CheckUser.isPresent();

    return result;
  }
  
}
