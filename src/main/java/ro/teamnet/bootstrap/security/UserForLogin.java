package ro.teamnet.bootstrap.security;

import ro.teamnet.bootstrap.plugin.security.UserDetailsBase;

public class UserForLogin implements UserDetailsBase{

    private String loginName;

    public UserForLogin(String loginName) {
        this.loginName = loginName;
    }

    @Override
    public String loginName() {
        return loginName;
    }
}
