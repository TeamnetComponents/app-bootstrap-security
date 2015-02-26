package ro.teamnet.bootstrap.web.rest.dto;

import javax.validation.constraints.Pattern;
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

    public AccountDTO() {
    }

    public AccountDTO(Long id, String login, String password, String firstName, String lastName, String email, String langKey,
                      List<String> roles, String gender) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.langKey = langKey;
        this.roles = roles;
        this.gender=gender;
    }

    public long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountDTO{");
        sb.append("id='").append(id).append('\'');
        sb.append("login='").append(login).append('\'');
        if(password != null) {
            sb.append(", password='").append(password.length()).append('\'');
        }
        sb.append(", firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", langKey='").append(langKey).append('\'');
        sb.append(", roles=").append(roles);
        sb.append('}');
        return sb.toString();
    }

    public String getGender() {
        return gender;
    }
}
