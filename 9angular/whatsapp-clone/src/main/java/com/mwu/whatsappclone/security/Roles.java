package com.mwu.whatsappclone.security;

import com.mwu.whatsappclone.model.enums.Role;
import org.springframework.util.Assert;


import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
public record Roles(Set<Role> roles) {

    /*
    public static final Roles EMPTY = new Roles(null);

    public Roles(Set<Role> roles) {
        this.roles = Collections.unmodifiableSet(roles);
    }

    public boolean hasRole() {
        return !roles.isEmpty();
    }

    public boolean hasRole(Role role) {
        Assert.notNull("role", role);

        return roles.contains(role);
    }

    public Stream<Role> stream() {
        return get().stream();
    }

    public Set<Role> get() {
        return roles();
    }

     */
    public static final Roles EMPTY = new Roles(Set.of());

    public Roles(Set<Role> roles) {
        Set<Role> safe = (roles == null) ? Set.of() : roles;

        Set<Role> copy = safe.stream()
                .filter(Objects::nonNull)
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

        this.roles = Collections.unmodifiableSet(copy);
    }

    public boolean hasRole() {
        return !roles.isEmpty();
    }

    public boolean hasRole(Role role) {
        Assert.notNull(role, "role must not be null");
        return roles.contains(role);
    }

    public Stream<Role> stream() {
        return roles.stream();
    }

    public Set<Role> get() {
        return roles;
    }
}