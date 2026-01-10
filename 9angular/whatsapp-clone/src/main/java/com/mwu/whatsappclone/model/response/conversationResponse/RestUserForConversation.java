package com.mwu.whatsappclone.model.response.conversationResponse;



import com.mwu.whatsappclone.model.aggregate.userAggregate.User;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RestUserForConversation(String lastName, String firstName,
                                      UUID publicId, String imageUrl,
                                      Instant lastSeen) {

    public static RestUserForConversation from(User user) {
        RestUserForConversationBuilder restUserForConversationBuilder = RestUserForConversation.builder();

        if (user.getImageUrl() != null) {
            restUserForConversationBuilder.imageUrl(user.getImageUrl().value());
        }

        return restUserForConversationBuilder
                .lastName(user.getLastName().value())
                .firstName(user.getFirstname().value())
                .publicId(user.getUserPublicId().value())
                .lastSeen(user.getLastSeen())
                .build();
    }

    public static List<RestUserForConversation> from(Set<User> users) {
        return users.stream().map(RestUserForConversation::from).toList();
    }
}
