package com.mwu.whatsappclone.model.aggregate.messageAggregate;


import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;

public record MessageSendNew(MessageContent messageContent,
                             ConversationPublicId conversationPublicId) {
}
