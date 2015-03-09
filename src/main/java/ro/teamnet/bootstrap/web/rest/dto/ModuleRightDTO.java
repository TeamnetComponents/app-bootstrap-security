package ro.teamnet.bootstrap.web.rest.dto;

public class ModuleRightDTO {

    private Long id;
    private String version;
    private Short right;
    private ModuleDTO module;

    public ModuleRightDTO() {
    }

    public ModuleRightDTO(Long id, String version, Short right, ModuleDTO module) {
        this.id = id;
        this.version = version;
        this.right = right;
        this.module = module;
    }

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

    public ModuleDTO getModule() {
        return module;
    }

    public void setModule(ModuleDTO module) {
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
