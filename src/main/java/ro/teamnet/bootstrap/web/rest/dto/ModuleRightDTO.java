package ro.teamnet.bootstrap.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import ro.teamnet.bootstrap.domain.util.ModuleRightTypeEnum;

public class ModuleRightDTO {

    private Long id;
    private String version;
    private Short right;
    private ModuleDTO module;
    private String source;

    public ModuleRightDTO() {
    }

    public ModuleRightDTO(Long id, String version, Short right, ModuleDTO module, String source) {
        this.id = id;
        this.version = version;
        this.right = right;
        this.module = module;
        this.source = source;
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

    @JsonInclude
    public String getModuleRightCode(){
        return ModuleRightTypeEnum.getCodeByValue(getRight());
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
