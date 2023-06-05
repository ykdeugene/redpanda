package com.tmsjsb.redpanda.Interface;

import jakarta.validation.GroupSequence;

@GroupSequence({ UserOnUpdatePassword.class, UserOnCreate.class })
public interface CompositeValidationGroup {
  // Empty interface, acts as a marker interface
  // for email == null option
}