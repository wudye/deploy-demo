package com.mwu.whatsappclone.service.conversiationService;


import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationToCreate;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.respository.dddRepository.ConversationRepository;
import com.mwu.whatsappclone.service.userService.UserReader;

import java.util.List;
import java.util.Optional;

public class ConversationCreator {

    private final ConversationRepository conversationRepository;
    private final UserReader userReader;

    public ConversationCreator(ConversationRepository conversationRepository, UserReader userReader) {
        this.conversationRepository = conversationRepository;
        this.userReader = userReader;
    }


    public State<Conversation, String> create(ConversationToCreate newConversation, User authenticatedUser) {
        newConversation.getMembers().add(authenticatedUser.getUserPublicId());
        List<User> members = userReader.getUsersByPublicId(newConversation.getMembers());
        List<UserPublicId> membersUuids = members.stream().map(User::getUserPublicId).toList();
        Optional<Conversation> conversationAlreadyPresent = conversationRepository.getConversationByUserPublicIds(membersUuids);
        State<Conversation, String> stateResult;
        if (conversationAlreadyPresent.isEmpty()) {
            Conversation newConversationSaved = conversationRepository.save(newConversation, members);
            stateResult = State.<Conversation, String>builder().forSuccess(newConversationSaved);
        } else {
            stateResult = State.<Conversation, String>builder().forError("This conversation already exists");
        }
        return stateResult;
    }
}
