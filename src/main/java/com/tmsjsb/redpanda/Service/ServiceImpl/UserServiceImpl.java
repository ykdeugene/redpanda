package com.tmsjsb.redpanda.Service.ServiceImpl;

import com.tmsjsb.redpanda.Entity.UserEntity;
import com.tmsjsb.redpanda.Repository.UserRepository;
import com.tmsjsb.redpanda.Service.ErrorMgrService;
import com.tmsjsb.redpanda.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.List;

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
    public Map<String, Object> login(String username, String password, String ipAddress, String browserType) {

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            System.out.println("this user: " + ThisUser);

            String HashedPassword = ThisUser.getPassword();
            int UserActive = ThisUser.getActiveStatus();
            if (passwordEncoder().matches(password, HashedPassword) && UserActive == 1) {
                Algorithm algorithm = Algorithm.HMAC256("secret");

                Date expirationDate = new Date(System.currentTimeMillis() + 10800000); // this is 180 minutes

                String jwt = JWT.create()
                        .withClaim("username", username)
                        .withClaim("ipAddress", ipAddress)
                        .withClaim("browserType", browserType)
                        .withExpiresAt(expirationDate)
                        .sign(algorithm);

                jsonObject.put("result", "true");
                jsonObject.put("jwt", jwt);
            } else {
                // incorrect pw or not active
                jsonObject = ErrorMgrService.errorHandler("invalid credentials",
                        Thread.currentThread().getStackTrace()[1]);
            }
        } else {
            // login user not found
            jsonObject = ErrorMgrService.errorHandler("invalid credentials", Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;
    }

    @Override
    public Optional<UserEntity> getUserById(String username) {
        return userRepository.findById(username);
    }

    @Override
    public Map<String, Object> updatePwd(String token, String password) {

        DecodedJWT decoded = JWT.decode(token);
        String username = decoded.getClaim("username").asString();

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            System.out.println("this user: " + ThisUser);
            try {
                String hashedPassword = passwordEncoder().encode(password);
                ThisUser.setPassword(hashedPassword);
                userRepository.save(ThisUser);
                jsonObject.put("result", "true");
            } catch (Exception e) {
                jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
            }
        } else {
            // user not found error
            jsonObject = ErrorMgrService.errorHandler("user not found", Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;

    }

    @Override
    public Map<String, Object> updateEmail(String token, String email) {

        DecodedJWT decoded = JWT.decode(token);
        String username = decoded.getClaim("username").asString();

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            try {
                ThisUser.setEmail(email);
                userRepository.save(ThisUser);
                jsonObject.put("result", "true");
            } catch (Exception e) {
                jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
            }
        } else {
            // user not found error
            jsonObject = ErrorMgrService.errorHandler("user not found", Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;
    }

    @Override
    public Map<String, Object> adminUpdateUserPwd(String username, String password) {

        Optional<UserEntity> CheckUser = getUserById(username);
        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            try {
                String hashedPassword = passwordEncoder().encode(password);
                ThisUser.setPassword(hashedPassword);
                System.out.println(password);
                System.out.println(hashedPassword);
                userRepository.save(ThisUser);
                jsonObject.put("result", "true");
            } catch (Exception e) {
                jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
            }
        } else {
            jsonObject = ErrorMgrService.errorHandler("user not found", Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;

    }

    @Override
    public Map<String, Object> adminUpdateUserEmail(String username, String email) {
        Optional<UserEntity> CheckUser = getUserById(username);

        Map<String, Object> jsonObject = new HashMap<>();

        if (CheckUser.isPresent()) {
            UserEntity ThisUser = CheckUser.get();
            try {
                ThisUser.setEmail(email);
                userRepository.save(ThisUser);
                jsonObject.put("result", "true");
            } catch (Exception e) {
                jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
            }
        } else {
            jsonObject = ErrorMgrService.errorHandler("user not found", Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;
    }

    @Override
    public Map<String, Object> createUser(String username, String password, String email, int activeStatus) {
        String result = "true";
        Map<String, Object> jsonObject = new HashMap<>(0);
        try {
            Optional<UserEntity> CheckUser = getUserById(username);

            if (CheckUser.isPresent()) {
                jsonObject = ErrorMgrService.errorHandler("data exists", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder().encode(password));
            newUser.setEmail(email);
            newUser.setActiveStatus(activeStatus);
            userRepository.save(newUser);

        } catch (Exception e) {
            jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
        }

        jsonObject.put("result", result);
        return jsonObject;
    }

    @Override
    public Map<String, Object> getProfile(String token) {
        Map<String, Object> jsonObject = new HashMap<>(0);
        try {
            DecodedJWT decoded = JWT.decode(token);
            String username = decoded.getClaim("username").asString();
            Optional<UserEntity> user = userRepository.findById(username);
            if (user.isPresent()) {
                jsonObject.put("username", user.get().getUsername());
                jsonObject.put("email", user.get().getEmail());
                return jsonObject;
            } else {
                jsonObject = ErrorMgrService.errorHandler("user not found", Thread.currentThread().getStackTrace()[1]);
                return jsonObject;
            }

        } catch (Exception e) {
            jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
            return jsonObject;
        }
    }

    @Override
    public Map<String, Object> updateActiveStatus(String username, int activeStatus) {
        Map<String, Object> jsonObject = new HashMap<>(0);
        try {
            Optional<UserEntity> user = userRepository.findById(username);
            UserEntity EditedUser = user.get();
            EditedUser.setActiveStatus(activeStatus);
            userRepository.save(EditedUser);
            jsonObject.put("result", "true");

        } catch (Exception e) {
            jsonObject = ErrorMgrService.errorHandler(e, Thread.currentThread().getStackTrace()[1]);
        }
        return jsonObject;
    }

    @Override
    public List<Map<String,Object>> getAllUsers() {

        List<UserEntity> users = userRepository.findAll();
        List<Map<String,Object>> newList = new ArrayList<>();
        
        for(int i = 0; i < users.size();i++)
        {
            Map<String,Object> temp = new HashMap<>(0);
            temp.put("username", users.get(i).getUsername());
            temp.put("email", users.get(i).getEmail());
            temp.put("activeStatus", users.get(i).getActiveStatus());
            newList.add(temp);
        }

        return newList;
    }

}
