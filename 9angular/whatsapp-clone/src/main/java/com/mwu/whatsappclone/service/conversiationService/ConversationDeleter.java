package com.mwu.whatsappclone.service.conversiationService;

import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.respository.dddRepository.ConversationRepository;
import com.mwu.whatsappclone.service.messageService.MessageChangeNotifier;

import java.util.Optional;

public class ConversationDeleter {

    private final ConversationRepository conversationRepository;
    private final MessageChangeNotifier messageChangeNotifier;

    public ConversationDeleter(ConversationRepository conversationRepository, MessageChangeNotifier messageChangeNotifier) {
        this.conversationRepository = conversationRepository;
        this.messageChangeNotifier = messageChangeNotifier;
    }

    public State<ConversationPublicId, String> deleteById(ConversationPublicId conversationId, User connectedUser) {
        State<ConversationPublicId, String> stateResult;

        Optional<Conversation> conversationToDeleteOpt = this.conversationRepository.getConversationByUsersPublicIdAndPublicId(connectedUser.getUserPublicId(), conversationId);
        if (conversationToDeleteOpt.isPresent()) {
            this.conversationRepository.deleteByPublicId(connectedUser.getUserPublicId(), conversationId);
            messageChangeNotifier.delete(conversationId, conversationToDeleteOpt.get()
                    .getMembers().stream().map(User::getUserPublicId).toList());
            stateResult = State.<ConversationPublicId, String>builder().forSuccess(conversationId);
        } else {
            stateResult = State.<ConversationPublicId, String>builder().forError("This conversation doesn't belong to this user or doesn't exist");
        }
        return stateResult;
    }
}
