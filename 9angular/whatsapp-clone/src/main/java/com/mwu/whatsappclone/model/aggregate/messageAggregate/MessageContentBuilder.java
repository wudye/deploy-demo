package com.mwu.whatsappclone.model.aggregate.messageAggregate;

import com.mwu.whatsappclone.model.enums.MessageType;

public final class MessageContentBuilder {

    private String text;
    private MessageType type;
    private MessageMediaContent media;

    private MessageContentBuilder() {
    }

    public static MessageContentBuilder messageContent() {
        return new MessageContentBuilder();
    }

    public MessageContentBuilder text(String text) {
        this.text = text;
        return this;
    }

    public MessageContentBuilder type(MessageType type) {
        this.type = type;
        return this;
    }

    public MessageContentBuilder media(MessageMediaContent media) {
        this.media = media;
        return this;
    }

    public MessageContent build() {
        return new MessageContent(text, type, media);
    }
}
