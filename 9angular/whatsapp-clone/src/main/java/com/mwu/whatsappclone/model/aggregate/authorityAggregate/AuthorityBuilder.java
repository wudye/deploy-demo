package com.mwu.whatsappclone.model.aggregate.authorityAggregate;

public final class AuthorityBuilder {

    private AuthorityName name;

    private AuthorityBuilder() {
    }

    public static AuthorityBuilder authority() {
        return new AuthorityBuilder();
    }

    public AuthorityBuilder name(AuthorityName name) {
        this.name = name;
        return this;
    }

    public Authority build() {
        return new Authority(name);
    }
}
