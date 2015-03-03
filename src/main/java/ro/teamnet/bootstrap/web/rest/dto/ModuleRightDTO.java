package ro.teamnet.bootstrap.web.rest.dto;

import ro.teamnet.bootstrap.domain.Module;

public class ModuleRightDTO {

    private Long id;
    private String version;
    private Short right;
    private Module module;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Short getRight() {
        return right;
    }

    public void setRight(Short right) {
        this.right = right;
    }

    public Module getModule() {
        return module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    @Override
    public String toString(){
        return "ModuleRight{" +
                "id='" + id + '\'' +
                ", version='" + version + '\'' +
                ", right='" + right + '\'' +
                ", module='" + module + '\'' +
                "}";
    }

}
