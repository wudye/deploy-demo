package com.mwu.whatsappclone.service.userService;

import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.respository.dddRepository.UserRepository;

import java.time.Instant;
import java.util.Optional;

public class UserPresence {

    private final UserRepository userRepository;
    private final UserReader userReader;

    public UserPresence(UserRepository userRepository, UserReader userReader) {
        this.userRepository = userRepository;
        this.userReader = userReader;
    }

    public void updatePresence(UserPublicId userPublicId) {
        userRepository.updateLastSeenByPublicId(userPublicId, Instant.now());
    }

    public Optional<Instant> getLastSeenByPublicId(UserPublicId userPublicId) {
        Optional<User> byPublicId = userReader.getByPublicId(userPublicId);
        return byPublicId.map(User::getLastSeen);
    }
}
