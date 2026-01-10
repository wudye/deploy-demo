package com.mwu.whatsappclone.service;

import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.MessageSendNew;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserEmail;
import com.mwu.whatsappclone.respository.dddRepository.ConversationRepository;
import com.mwu.whatsappclone.respository.dddRepository.MessageRepository;
import com.mwu.whatsappclone.respository.dddRepository.UserRepository;
import com.mwu.whatsappclone.security.AuthenticatedUser;
import com.mwu.whatsappclone.service.conversiationService.ConversationReader;
import com.mwu.whatsappclone.service.messageService.MessageChangeNotifier;
import com.mwu.whatsappclone.service.messageService.MessageCreator;
import com.mwu.whatsappclone.service.userService.UserReader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MessageApplicationService {

    private final MessageCreator messageCreator;
    private final UserReader userReader;

    public MessageApplicationService(MessageRepository messageRepository, UserRepository userRepository,
                                     ConversationRepository conversationRepository, MessageChangeNotifier messageChangeNotifier) {
        ConversationReader conversationReader = new ConversationReader(conversationRepository);
        this.messageCreator = new MessageCreator(messageRepository, messageChangeNotifier, conversationReader);
        this.userReader = new UserReader(userRepository);
    }

    @Transactional
    public State<Message, String> send(MessageSendNew messageSendNew) {
        State<Message, String> creationState;
        Optional<User> connectedUser = this.userReader.getByEmail(new UserEmail(AuthenticatedUser.username().username()));
        if(connectedUser.isPresent()) {
            creationState = this.messageCreator.create(messageSendNew, connectedUser.get());
        } else {
            creationState = State.<Message, String>builder()
                    .forError(String.format("Error retrieving user information inside the DB : %s", AuthenticatedUser.username().username()));
        }
        return creationState;
    }
}
