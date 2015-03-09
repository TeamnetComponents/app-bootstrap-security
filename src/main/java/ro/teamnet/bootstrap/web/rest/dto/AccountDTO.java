package ro.teamnet.bootstrap.web.rest.dto;

import ro.teamnet.bootstrap.domain.Account;
import ro.teamnet.bootstrap.domain.Module;
import ro.teamnet.bootstrap.domain.ModuleRight;
import ro.teamnet.bootstrap.domain.Role;
import ro.teamnet.bootstrap.domain.util.ModuleRightSource;

import javax.validation.constraints.Pattern;
import java.util.*;

public class AccountDTO {

    private long id;

    @Pattern(regexp = "^[a-z0-9]*$")
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String langKey;
    private String gender;
    private List<RoleDTO> roles = new ArrayList<>();
    private HashMap<String, ModuleRightDTO> moduleRights = new HashMap<>();

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.login = account.getLogin();
        this.password = account.getPassword();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.email = account.getEmail();
        this.langKey = account.getLangKey();
        this.gender = account.getGender();

        // load account module rights
        for(ModuleRight mr: account.getModuleRights()) {
            moduleRights.put(
                mr.getModule().getCode()+"_"+mr.getModuleRightCode(),
                loadModuleRightDTO(mr, ModuleRightSource.ACCOUNT.name())
            );
        }

        // load roles
        for (Role role : account.getRoles()) {
            // load role module rights
            for(ModuleRight mr: role.getModuleRights()) {
                moduleRights.put(
                    mr.getModule().getCode() + "_" + mr.getModuleRightCode(),
                    loadModuleRightDTO(mr, ModuleRightSource.ROLE.name())
                );
            }

            roles.add(new RoleDTO(role.getId(), role.getVersion(), role.getCode(), role.getDescription(),
                    role.getOrder(), role.getValidFrom(), role.getValidTo(), role.getActive(), role.getLocal(), null));
        }
    }

    private ModuleRightDTO loadModuleRightDTO(ModuleRight mr, String source) {
        Module module = mr.getModule();

        ModuleDTO moduleDTO = new ModuleDTO(module.getId(), module.getVersion(), module.getCode(),
                module.getDescription(), module.getType(), module.getParentModule(), null);

        return new ModuleRightDTO(mr.getId(), mr.getVersion(), mr.getRight(), moduleDTO, source);
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

    public Collection<RoleDTO> getRoles() {
        return roles;
    }

    public String getGender() {
        return gender;
    }

    public HashMap<String, ModuleRightDTO> getModuleRights() {
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
        sb.append(", moduleRights=").append(moduleRights);
        sb.append('}');
        return sb.toString();
    }
}
