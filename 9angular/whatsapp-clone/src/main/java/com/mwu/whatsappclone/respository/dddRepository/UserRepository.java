package com.mwu.whatsappclone.respository.dddRepository;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserEmail;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserRepository {
    void save(User user);

    Optional<User> get(UserPublicId userPublicId);

    Optional<User> getOneByEmail(UserEmail userEmail);

    List<User> getByPublicIds(Set<UserPublicId> userPublicIds);

    Page<User> search(Pageable pageable, String query);

    int updateLastSeenByPublicId(UserPublicId userPublicId, Instant lastSeen);

    List<User> getRecipientByConversationIdExcludingReader(ConversationPublicId conversationPublicId, UserPublicId readerPublicId);

    Optional<User> getOneByPublicId(UserPublicId userPublicId);

}
