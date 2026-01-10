package com.mwu.whatsappclone.service;

import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserEmail;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.respository.dddRepository.UserRepository;
import com.mwu.whatsappclone.security.AuthenticatedUser;
import com.mwu.whatsappclone.service.userService.UserPresence;
import com.mwu.whatsappclone.service.userService.UserReader;
import com.mwu.whatsappclone.service.userService.UserSynchronizer;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UsersApplicationService {
    private final UserSynchronizer userSynchronizer;
    private final UserReader userReader;
    private final UserPresence userPresence;

    /*
    在构造方法里用 new UserReader(userRepository) 自己创建 UserReader（同样还有 UserSynchronizer、UserPresence）。
     */
    public UsersApplicationService(UserRepository userRepository) {
        this.userSynchronizer = new UserSynchronizer(userRepository);
        this.userReader = new UserReader(userRepository);
        this.userPresence = new UserPresence(userRepository, userReader);

    }


    @Transactional
    public User getAuthenticatedUserWithSync(Jwt oauth2User, boolean forceResync) {
        userSynchronizer.syncWithIdp(oauth2User, forceResync);
        return userReader.getByEmail(new UserEmail(AuthenticatedUser.username().get()))
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public User getAuthenticatedUser() {
        return userReader.getByEmail(new UserEmail(AuthenticatedUser.username().get()))
                .orElseThrow();
    }


    @Transactional(readOnly = true)
    public List<User> search(Pageable pageable, String query) {
        return userReader.search(pageable, query).stream().toList();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(UserEmail userEmail) {
        return userReader.getByEmail(userEmail);
    }

    @Transactional
    public void updatePresence(UserPublicId userPublicId) {
        userPresence.updatePresence(userPublicId);
    }

    @Transactional(readOnly = true)
    public Optional<Instant> getLastSeen(UserPublicId userPublicId) {
        return userPresence.getLastSeenByPublicId(userPublicId);
    }

}
