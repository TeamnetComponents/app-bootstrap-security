package ro.teamnet.bootstrap.web.rest.dto;

import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;

import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountDTO {

    private long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String langKey;
    private List<String> roles;
    private String gender;
    private Collection<ModuleRight> moduleRights;

    public AccountDTO() { }

    public AccountDTO(Account account) {
        List<String> roles = new ArrayList<>();
        for (Role role : account.getRoles()) {
            roles.add(role.getCode());
        }

        this.id = account.getId();
        this.login = account.getLogin();
        this.password = account.getPassword();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.langKey = account.getLangKey();
        this.roles = roles;
        this.gender=account.getGender();
        this.moduleRights = account.getModuleRights();
    }

    public long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getLangKey() {
        return langKey;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getGender() {
        return gender;
    }

    public Collection<ModuleRight> getModuleRights() {
        return moduleRights;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountDTO{");
        sb.append("id='").append(id).append('\'');
        sb.append("login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", langKey='").append(langKey).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }
}
