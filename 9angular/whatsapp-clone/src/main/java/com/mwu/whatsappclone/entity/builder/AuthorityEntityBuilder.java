package com.mwu.whatsappclone.entity.builder;

import com.mwu.whatsappclone.entity.AuthorityEntity;

public final class AuthorityEntityBuilder {

    private String name;

    private AuthorityEntityBuilder() {
    }

    public static AuthorityEntityBuilder authorityEntity() {
        return new AuthorityEntityBuilder();
    }

    public AuthorityEntityBuilder name(String name) {
        this.name = name;
        return this;
    }

    public AuthorityEntity build() {
        return new AuthorityEntity(name);
    }
}
