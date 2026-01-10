package com.mwu.whatsappclone.service.conversiationService;

import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.model.enums.MessageSendState;
import com.mwu.whatsappclone.respository.dddRepository.MessageRepository;
import com.mwu.whatsappclone.service.messageService.MessageChangeNotifier;
import com.mwu.whatsappclone.service.userService.UserReader;

import java.util.List;

public class ConversationViewed {

    private final MessageRepository messageRepository;
    private final MessageChangeNotifier messageChangeNotifier;
    private final UserReader userReader;

    public ConversationViewed(MessageRepository messageRepository, MessageChangeNotifier messageChangeNotifier, UserReader userReader) {
        this.messageRepository = messageRepository;
        this.messageChangeNotifier = messageChangeNotifier;
        this.userReader = userReader;
    }

    public State<Integer, String> markAsRead(ConversationPublicId conversationPublicId, UserPublicId connectedUserPublicId) {
        List<Message> messageToUpdateSendState = messageRepository.findMessageToUpdateSendState(conversationPublicId, connectedUserPublicId);
        int nbUpdatedMessages = messageRepository.updateMessageSendState(conversationPublicId, connectedUserPublicId, MessageSendState.READ);
        List<UserPublicId> usersToNotify = userReader.findUsersToNotify(conversationPublicId, connectedUserPublicId)
                .stream().map(User::getUserPublicId).toList();
        ConversationViewedForNotification conversationViewedForNotification = new ConversationViewedForNotification(conversationPublicId.value(),
                messageToUpdateSendState.stream().map(message -> message.getPublicId().value()).toList());
        messageChangeNotifier.view(conversationViewedForNotification, usersToNotify);

        if (nbUpdatedMessages > 0) {
            return State.<Integer, String>builder().forSuccess(nbUpdatedMessages);
        } else {
            return State.<Integer, String>builder().forSuccess();
        }
    }
}
