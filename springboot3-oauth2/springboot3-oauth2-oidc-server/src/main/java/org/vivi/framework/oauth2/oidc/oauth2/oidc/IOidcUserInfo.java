package org.vivi.framework.oauth2.oidc.oauth2.oidc;

import lombok.Builder;
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

    public static IOidcUserInfo.Builder myBuilder() {
        return new IOidcUserInfo.Builder();
    }

    public static final class Builder {
        private final Map<String, Object> claims = new LinkedHashMap();

        private Builder() {
        }

        public IOidcUserInfo.Builder claim(String name, Object value) {
            this.claims.put(name, value);
            return this;
        }

        public IOidcUserInfo.Builder claims(Consumer<Map<String, Object>> claimsConsumer) {
            claimsConsumer.accept(this.claims);
            return this;
        }

        public IOidcUserInfo.Builder username(String username) {
            return this.claim("username", username);
        }

        public IOidcUserInfo.Builder name(String name) {
            return this.claim("name", name);
        }

        public IOidcUserInfo.Builder description(String description) {
            return this.claim("description", description);
        }

        public IOidcUserInfo.Builder status(Integer status) {
            return this.claim("status", status);
        }

        public IOidcUserInfo.Builder phoneNumber(String phoneNumber) {
            return this.claim("phone_number", phoneNumber);
        }

        public IOidcUserInfo.Builder email(String email) {
            return this.claim("email", email);
        }

        public IOidcUserInfo.Builder profile(String profile) {
            return this.claim("profile", profile);
        }

        public IOidcUserInfo.Builder address(String address) {
            return this.claim("address", address);
        }

        public IOidcUserInfo build() {
            return new IOidcUserInfo(this.claims);
        }

    }
}
