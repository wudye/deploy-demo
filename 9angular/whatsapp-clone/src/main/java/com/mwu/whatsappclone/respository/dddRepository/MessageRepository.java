package com.mwu.whatsappclone.respository.dddRepository;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.Conversation;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.model.enums.MessageSendState;

import java.util.List;

public interface MessageRepository {

    Message save(Message message, User sender, Conversation conversation);

    int updateMessageSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId, MessageSendState state);

    List<Message> findMessageToUpdateSendState(ConversationPublicId conversationPublicId, UserPublicId userPublicId);

}
