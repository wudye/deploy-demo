package com.mwu.whatsappclone.model.response.userResponse;

import com.mwu.whatsappclone.model.aggregate.userAggregate.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Set;
import java.util.UUID;

@Builder
public record RestUser(UUID publicId,
                       String firstName,
                       String lastName,
                       String email,
                       String imageUrl,
                       Set<RestAuthority> authorities) {

    public static RestUser from(User user) {
        RestUserBuilder restUserBuilder = RestUser.builder();

        if(user.getImageUrl() != null) {
            restUserBuilder.imageUrl(user.getImageUrl().value());
        }

        return restUserBuilder
                .email(user.getEmail().value())
                .firstName(user.getFirstname().value())
                .lastName(user.getLastName().value())
                .publicId(user.getUserPublicId().value())
                .authorities(RestAuthority.fromSet(user.getAuthorities()))
                .build();
    }
}
