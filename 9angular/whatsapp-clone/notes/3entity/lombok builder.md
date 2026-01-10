package com.mwu.whatsappclone.model.aggregate;

import com.mwu.whatsappclone.security.Assert;
import lombok.Builder;

@Builder
public class Authority {

    private AuthorityName name;

    public Authority(AuthorityName name) {
        Assert.notNull("name", name);
        this.name = name;
    }

    public AuthorityName getName() {
        return name;
    }
}

package com.mwu.whatsappclone.model.aggregate;

import com.mwu.whatsappclone.security.Assert;

public class Authority {

    private AuthorityName name;

    public Authority(AuthorityName name) {
        Assert.notNull("name", name);
        this.name = name;
    }

    public AuthorityName getName() {
        return name;
    }

    // Lombok 生成：入口
    public static AuthorityBuilder builder() {
        return new AuthorityBuilder();
    }

    // Lombok 生成：内部 Builder（注意是 Authority.AuthorityBuilder）
    public static class AuthorityBuilder {
        private AuthorityName name;

        AuthorityBuilder() {
        }

        public AuthorityBuilder name(AuthorityName name) {
            this.name = name;
            return this;
        }

        public Authority build() {
            return new Authority(this.name);
        }

        @Override
        public String toString() {
            return "Authority.AuthorityBuilder(name=" + this.name + ")";
        }
    }
}

