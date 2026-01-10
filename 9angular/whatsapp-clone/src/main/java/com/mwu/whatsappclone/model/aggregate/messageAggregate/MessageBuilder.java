package com.mwu.whatsappclone.model.aggregate.messageAggregate;

import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.model.enums.MessageSendState;

public final class MessageBuilder {

    private MessageSentTime sentTime;
    private MessageContent content;
    private MessageSendState sendState;
    private MessagePublicId publicId;
    private UserPublicId sender;
    private ConversationPublicId conversationId;

    private MessageBuilder() {
    }

    public static MessageBuilder message() {
        return new MessageBuilder();
    }

    public MessageBuilder sentTime(MessageSentTime sentTime) {
        this.sentTime = sentTime;
        return this;
    }

    public MessageBuilder content(MessageContent content) {
        this.content = content;
        return this;
    }

    public MessageBuilder sendState(MessageSendState sendState) {
        this.sendState = sendState;
        return this;
    }

    public MessageBuilder publicId(MessagePublicId publicId) {
        this.publicId = publicId;
        return this;
    }

    public MessageBuilder sender(UserPublicId sender) {
        this.sender = sender;
        return this;
    }

    public MessageBuilder conversationId(ConversationPublicId conversationId) {
        this.conversationId = conversationId;
        return this;
    }

    public Message build() {
        return new Message(
                sentTime,
                content,
                sendState,
                publicId,
                sender,
                conversationId
        );
    }
}
