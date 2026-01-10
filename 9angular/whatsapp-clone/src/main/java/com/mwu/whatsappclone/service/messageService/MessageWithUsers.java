package com.mwu.whatsappclone.service.messageService;

import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;

import java.util.List;

public record MessageWithUsers(Message message, List<UserPublicId> userToNotify) {
}
