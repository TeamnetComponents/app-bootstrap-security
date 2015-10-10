package ro.teamnet.bootstrap.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ro.teamnet.bootstrap.plugin.security.UserDetailsExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class UserExtension implements UserDetailsExtension {
    private UserDetails userDetails;
    private Map<String, Object> extensions;
    private Collection<GrantedAuthority> authorities;

    public UserExtension(UserDetails userDetails) {
        this(userDetails, new HashMap<String, Object>());
    }

    public UserExtension(UserDetails userDetails, Map<String, Object> extensions) {
        this(userDetails, userDetails.getAuthorities(), extensions);
    }

    public UserExtension(UserDetails userDetails, Collection<? extends GrantedAuthority> grantedAuthorities,
                         Map<String, Object> extensions) {
        this.userDetails = userDetails;
        this.authorities = new HashSet<>();
        this.authorities.addAll(grantedAuthorities);
        this.extensions = new HashMap<>();
        if (userDetails instanceof UserDetailsExtension) {
            addExtensions(((UserDetailsExtension) userDetails).getExtensions());
        }
        addExtensions(extensions);
    }

    @Override
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    @Override
    public void addExtensions(Map<String, Object> extensions) {
        this.extensions.putAll(extensions);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return userDetails.getPassword();
    }

    @Override
    public String getUsername() {
        return userDetails.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userDetails.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return userDetails.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userDetails.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return userDetails.isEnabled();
    }
}
