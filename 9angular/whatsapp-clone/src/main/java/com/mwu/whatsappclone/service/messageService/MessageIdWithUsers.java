package com.mwu.whatsappclone.service.messageService;

import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.service.conversiationService.ConversationViewedForNotification;

import java.util.List;

public record MessageIdWithUsers(ConversationViewedForNotification conversationViewedForNotification,
                                 List<UserPublicId> usersToNotify) {
}
