package com.mwu.whatsappclone.model.response.userResponse;


import com.mwu.whatsappclone.model.aggregate.authorityAggregate.Authority;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record RestAuthority(String name) {

    public static Set<RestAuthority> fromSet(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> RestAuthority.builder().name(authority.getName().name()).build())
                .collect(Collectors.toSet());
    }

}
