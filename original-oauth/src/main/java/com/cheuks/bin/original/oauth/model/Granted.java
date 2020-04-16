package com.cheuks.bin.original.oauth.model;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Granted implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String authority;//权限

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @Override
    public int hashCode() {
        return this.authority.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj || !(obj instanceof Granted))
            return false;
        return this.authority.equals(((Granted) obj).authority);
    }

}
