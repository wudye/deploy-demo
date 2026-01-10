package com.mwu.whatsappclone.service.conversiationService;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.respository.dddRepository.ConversationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ConversationReader {

    private final ConversationRepository conversationRepository;

    public ConversationReader(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    public Page<Conversation> getAllByUserPublicID(UserPublicId userPublicId, Pageable pageable) {
        return conversationRepository.getConversationByUserPublicId(userPublicId, pageable);
    }

    public Optional<Conversation> getOneByPublicId(ConversationPublicId conversationPublicId) {
        return conversationRepository.getOneByPublicId(conversationPublicId);
    }

    public Optional<Conversation> getOneByPublicIdAndUserId(ConversationPublicId conversationPublicId, UserPublicId userPublicId) {
        return conversationRepository.getConversationByUsersPublicIdAndPublicId(userPublicId, conversationPublicId);
    }
}
