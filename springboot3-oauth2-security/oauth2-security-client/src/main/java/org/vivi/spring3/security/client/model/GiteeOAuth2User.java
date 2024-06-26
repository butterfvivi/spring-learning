package org.vivi.spring3.security.client.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GiteeOAuth2User implements OAuth2User {

    private String id;
    private String name;
    private String email;

    private List<GrantedAuthority> authorities= AuthorityUtils.createAuthorityList("ROLE_USER");
    private Map<String,Object> attributes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        if (attributes==null){
            attributes=new HashMap<>();

            attributes.put("id",this.getId());
            attributes.put("name",this.getName());
            attributes.put("email",this.getEmail());
        }

        return attributes;
    }
}
