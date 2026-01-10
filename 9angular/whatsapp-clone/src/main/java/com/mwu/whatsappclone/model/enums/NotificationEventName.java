package com.mwu.whatsappclone.model.enums;

public enum NotificationEventName {
    NEW_MESSAGE("message"), DELETE_CONVERSATION("delete-conversation"),
    VIEWS_MESSAGES("view-messages");

    public final String value;

    NotificationEventName(String value) {
        this.value = value;
    }
}
