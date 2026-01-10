package com.mwu.whatsappclone.respository.impl;


import com.mwu.whatsappclone.entity.ConversationEntity;
import com.mwu.whatsappclone.entity.MessageEntity;
import com.mwu.whatsappclone.entity.UserEntity;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.model.enums.MessageSendState;
import com.mwu.whatsappclone.model.enums.MessageType;
import com.mwu.whatsappclone.respository.dddRepository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SpringDataMessageRepository implements MessageRepository {

    private final JpaMessageRepository jpaMessageRepository;
    private final JpaMessageBinaryContent jpaMessageBinaryContent;

    public SpringDataMessageRepository(JpaMessageRepository jpaMessageRepository, JpaMessageBinaryContent jpaMessageBinaryContent) {
        this.jpaMessageRepository = jpaMessageRepository;
        this.jpaMessageBinaryContent = jpaMessageBinaryContent;
    }

    @Override
    public Message save(Message message, User sender, Conversation conversation) {
        MessageEntity messageEntity = MessageEntity.from(message);
        messageEntity.setSender(UserEntity.from(sender));
        messageEntity.setConversation(ConversationEntity.from(conversation));

        if (message.getContent().type() != MessageType.TEXT) {
            jpaMessageBinaryContent.save(messageEntity.getContentBinary());
        }

        MessageEntity messageSaved = jpaMessageRepository.save(messageEntity);
        return MessageEntity.toDomain(messageSaved);
    }

    @Override
    public int updateMessageSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId, MessageSendState state) {
        return jpaMessageRepository.updateMessageSendState(conversationPublicId.value(), userPublicId.value(), state);
    }

    @Override
    public List<Message> findMessageToUpdateSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId) {
        return jpaMessageRepository.findMessageToUpdateSendState(conversationPublicId.value(), userPublicId.value())
                .stream().map(MessageEntity::toDomain).toList();
    }
}
