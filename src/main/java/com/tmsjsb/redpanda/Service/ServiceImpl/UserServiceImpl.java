package com.tmsjsb.redpanda.Service.ServiceImpl;

import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.UserRepository;
import com.tmsjsb.redpanda.Service.UserService;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Optional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    private UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public MappingJacksonValue login(String username, String password) {
        Optional<UserEntity> CheckUser = getUserById(username);

        JWTJsonData json = new JWTJsonData();
        json.setResult("false");

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            String HashedPassword = ThisUser.getPassword();

            int UserActive = ThisUser.getActiveStatus();
            if (passwordEncoder().matches(password, HashedPassword) && UserActive == 1) {
                Algorithm algorithm = Algorithm.HMAC256("secret");

                String jwt = JWT.create()
                        .withClaim("username", username)
                        .sign(algorithm);

                json.setResult("true");
                json.setJwt(jwt);
            }
        }

        MappingJacksonValue mapping = new MappingJacksonValue(json);
        return mapping;
    }

    @Override
    public Optional<UserEntity> getUserById(String username) {
        return userRepository.findById(username);
    }

    @Override
    public Map<String, Object> updatePwd(String username, String password) {

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            System.out.println("this user: " + ThisUser);
            try {
                String hashedPassword = passwordEncoder().encode(password);
                ThisUser.setPassword(hashedPassword);
                System.out.println(password);
                System.out.println(hashedPassword);
                userRepository.save(ThisUser);
                // userRepository.save(ThisUser);
                jsonObject.put("results", "true");
            } catch (Exception e) {
                System.out.println(e);
                jsonObject.put("results", "BSJxxx (error code here) .updatePwd");
            }
        }
        return jsonObject;

    }

    @Override
    public Map<String, Object> updateEmail(String username, String email) {

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            try {
                ThisUser.setEmail(email);
                userRepository.save(ThisUser);
                jsonObject.put("results", "true");
            } catch (Exception e) {
                jsonObject.put("results", "BSJxxx (error code here)");
            }
        }
        return jsonObject;
    }

    /*
     * @Override
     * public MappingJacksonValue updatePwd(String username, String password) {
     * Optional<UserEntity> CheckUser = getUserById(username);
     * 
     * UpdateProfileJsonData json = new UpdateProfileJsonData();
     * json.setResult("false");
     * 
     * if (CheckUser.isPresent()) {
     * UserEntity ThisUser = CheckUser.get();
     * // need to write error handlers <start>
     * // need to write error handlers <end>
     * String hashedPassword = passwordEncoder().encode(password);
     * ThisUser.setPassword(hashedPassword);
     * userRepository.save(ThisUser);
     * json.setResult("true");
     * 
     * }
     * MappingJacksonValue mapping = new MappingJacksonValue(json);
     * return mapping;
     * }
     */

    /*
     * @Override
     * public MappingJacksonValue updateEmail(String username, String email) {
     * Optional<UserEntity> CheckUser = getUserById(username);
     * 
     * UpdateProfileJsonData json = new UpdateProfileJsonData();
     * json.setResult("false");
     * 
     * if (CheckUser.isPresent()) {
     * UserEntity ThisUser = CheckUser.get();
     * // need to write error handlers <start>
     * // need to write error handlers <end>
     * ThisUser.setEmail(email);
     * userRepository.save(ThisUser);
     * json.setResult("true");
     * 
     * }
     * MappingJacksonValue mapping = new MappingJacksonValue(json);
     * return mapping;
     * }
     */

    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

}

@Getter
@Setter
class JWTJsonData {
    private String result;
    private String jwt;
}

@Getter
@Setter
class UpdateProfileJsonData {
    private String result;
}