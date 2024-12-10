package org.vivi.framework.sso.server.oauth2.oidc;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public class IOidcUserInfo extends OidcUserInfo {

    private static final long serialVersionUID = 1L;

    private final Map<String,Object> claims;

    public IOidcUserInfo(Map<String, Object> claims) {
        super(claims);
        Assert.notEmpty(claims, "claims cannot be empty");
        this.claims = Collections.unmodifiableMap(new LinkedHashMap(claims));
    }

    public static Builder myBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private final Map<String, Object> claims = new LinkedHashMap();

        private Builder() {
        }

        public Builder claim(String name, Object value) {
            this.claims.put(name, value);
            return this;
        }

        public Builder claims(Consumer<Map<String, Object>> claimsConsumer) {
            claimsConsumer.accept(this.claims);
            return this;
        }

        public Builder username(String username) {
            return this.claim("username", username);
        }

        public Builder name(String name) {
            return this.claim("name", name);
        }

        public Builder description(String description) {
            return this.claim("description", description);
        }

        public Builder status(Integer status) {
            return this.claim("status", status);
        }

        public Builder phoneNumber(String phoneNumber) {
            return this.claim("phone_number", phoneNumber);
        }

        public Builder email(String email) {
            return this.claim("email", email);
        }

        public Builder profile(String profile) {
            return this.claim("profile", profile);
        }

        public Builder address(String address) {
            return this.claim("address", address);
        }

        public IOidcUserInfo build() {
            return new IOidcUserInfo(this.claims);
        }

    }
}
