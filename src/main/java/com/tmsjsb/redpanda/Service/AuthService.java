package com.tmsjsb.redpanda.Service;

public interface AuthService {
  boolean validateJwt(String jwt, String ipAddress, String browserType);

  String TokenToUsername(String jwt);

  boolean CheckGroup(String username, String Group);
}
