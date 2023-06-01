package com.tmsjsb.redpanda.Service;

public interface AuthService {
  boolean validateJwt(String jwt);

  boolean CheckGroup(String username, String Group);
}
