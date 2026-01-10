package com.mwu.whatsappclone.service.messageService;


import com.mwu.whatsappclone.model.State;
import com.mwu.whatsappclone.model.aggregate.conversationAggregate.ConversationPublicId;
import com.mwu.whatsappclone.model.aggregate.messageAggregate.Message;
import com.mwu.whatsappclone.model.aggregate.userAggregate.UserPublicId;
import com.mwu.whatsappclone.service.conversiationService.ConversationViewedForNotification;

import java.util.List;

public interface MessageChangeNotifier {

    State<Void, String> send(Message message, List<UserPublicId> userToNotify);

    State<Void, String> delete(ConversationPublicId conversationPublicId, List<UserPublicId> userToNotify);

    State<Void, String> view(ConversationViewedForNotification conversationViewedForNotification, List<UserPublicId> usersToNotify);
}
