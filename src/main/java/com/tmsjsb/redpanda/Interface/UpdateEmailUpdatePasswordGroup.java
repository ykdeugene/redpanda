package com.tmsjsb.redpanda.Interface;

import jakarta.validation.GroupSequence;

@GroupSequence({ UserOnUpdatePassword.class, UserOnUpdateEmail.class })
public interface UpdateEmailUpdatePasswordGroup {

}
