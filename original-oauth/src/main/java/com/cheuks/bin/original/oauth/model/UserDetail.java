package com.cheuks.bin.original.oauth.model;

import com.cheuks.bin.original.oauth.model.User.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class UserDetail implements UserDetails {

    private static final long serialVersionUID = 6875540371634419460L;
    private final User userInfo;// 用户信息
    private Object output;// 输出信息
    private final String verificationCode;// 当前身份码：一般为随机码
    private Map<String, Object> addition;// 附加参数

    public UserDetail appendAddition(String key, Object value) {
        if (null == this.addition) {
            synchronized (this) {
                this.addition = new HashMap<String, Object>();
            }
        }
        this.addition.put(key, value);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAddition(String key) {
        if (null == this.addition)
            return null;
        return (T) this.addition.get(key);
    }

    public User getUserInfo() {
        return userInfo;
    }

    @SuppressWarnings("unchecked")
    public <T> T getOutputByForceConver() {
        return (T) output;
    }

    public Object getOutput() {
        return output;
    }

    public UserDetail setOutput(Object output) {
        this.output = output;
        return this;
    }

    public Map<String, Object> getAddition() {
        return addition;
    }

    public UserDetail setAddition(Map<String, Object> addition) {
        this.addition = addition;
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (null == userInfo.getRoles())
            return null;
        return userInfo.getRoles().getGranteds();
    }

    public String getSource() {
        return userInfo.getSource();
    }

    @Override
    public String getPassword() {
        return userInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return userInfo.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userInfo.getStatus() == 10;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userInfo.getStatus() == AccountStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userInfo.getStatus() < 1 || System.currentTimeMillis() < userInfo.getExpired();
    }

    @Override
    public boolean isEnabled() {
        return userInfo.getStatus() == AccountStatus.NORMAL;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

}
