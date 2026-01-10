package com.mwu.whatsappclone.model.aggregate.messageAggregate;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;

public final class MessageSendNewBuilder {

    private MessageContent messageContent;
    private ConversationPublicId conversationPublicId;

    private MessageSendNewBuilder() {
    }

    public static MessageSendNewBuilder messageSendNew() {
        return new MessageSendNewBuilder();
    }

    public MessageSendNewBuilder messageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    public MessageSendNewBuilder conversationPublicId(ConversationPublicId conversationPublicId) {
        this.conversationPublicId = conversationPublicId;
        return this;
    }

    public MessageSendNew build() {
        return new MessageSendNew(messageContent, conversationPublicId);
    }
}
