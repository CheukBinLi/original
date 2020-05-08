package com.cheuks.bin.original.oauth.security.token;

import com.cheuks.bin.original.oauth.security.token.handle.TokenHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Getter
// @Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterChain {

    TokenHandler head;
    private ThreadLocal<TokenHandler> handler = new ThreadLocal<>();

    public Authentication doAnalysis(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        try {
            TokenHandler item = next();
            if (null == item) {
                return null;
            }
            return item.doAnalysis(request, response, this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public Authentication doAnalysis(HttpServletRequest request, HttpServletResponse response, TokenHandler hanlder) throws Throwable {
        try {
            TokenHandler item = next();
            if (null == item) {
                return null;
            }
            return item.doAnalysis(request, response, this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public void doLogout(HttpServletRequest request, HttpServletResponse response, String token) throws Throwable {
        TokenHandler item = next();
        if (null == item) {
            return;
        }
        item.doLogout(request, response, token, this);
    }

    public FilterChain setHandler(final TokenHandler handler) {
        this.head = handler;
        return this;
    }

    protected TokenHandler next() {
        TokenHandler item = this.handler.get();
        if (null == item) {
            item = head;
        } else if (item == item.getNext()) {
            return null;
        }
        this.handler.set(item.getNext());
        return item;
    }

}
