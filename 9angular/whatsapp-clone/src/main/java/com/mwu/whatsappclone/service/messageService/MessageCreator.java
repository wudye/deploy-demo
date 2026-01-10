package com.mwu.whatsappclone.service.messageService;


import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.*;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.enums.MessageSendState;
import com.mwu.whatsappclone.respository.dddRepository.MessageRepository;
import com.mwu.whatsappclone.service.conversiationService.ConversationReader;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class MessageCreator {

    private final MessageRepository messageRepository;
    private final MessageChangeNotifier messageChangeNotifier;
    private final ConversationReader conversationReader;

    public MessageCreator(MessageRepository messageRepository, MessageChangeNotifier messageChangeNotifier,
                          ConversationReader conversationReader) {
        this.messageRepository = messageRepository;
        this.messageChangeNotifier = messageChangeNotifier;
        this.conversationReader = conversationReader;
    }


    public State<Message, String> create(MessageSendNew messageSendNew, User sender) {
       Message newMessage = MessageBuilder.message()
                .content(messageSendNew.messageContent())
                .publicId(new MessagePublicId(UUID.randomUUID()))
                .sendState(MessageSendState.RECEIVED)
                .sentTime(new MessageSentTime(Instant.now()))
                .conversationId(messageSendNew.conversationPublicId())
                .sender(sender.getUserPublicId())
                .build();

        State<Message, String> creationState;
        Optional<Conversation> conversationToLink = conversationReader.getOneByPublicId(messageSendNew.conversationPublicId());
        if (conversationToLink.isPresent()) {
         Message messageSaved = messageRepository.save(newMessage, sender, conversationToLink.get());
            messageChangeNotifier.send(newMessage, conversationToLink.get().getMembers().stream()
                    .map(User::getUserPublicId).toList());
            creationState = State.<Message, String>builder().forSuccess(messageSaved);
        } else {
            creationState = State.<Message, String>builder().forError(
                    String.format("Unable to find the conversation to link the message with the " +
                            "following publicId %s", messageSendNew.conversationPublicId().value())
            );
        }
        return creationState;
    }

}
