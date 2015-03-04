package ro.teamnet.bootstrap.domain.util;

/**
* This Enum contains all the components for whitch the user must have permissions and their value representations
*/
public enum ModuleTypeEnum {

    MENU((short)1),
    ENTITY((short)2),
    FIELD((short)3),
    OTHER((short)4);

    private short type;

    public short getType() {
        return type;
    }

    private ModuleTypeEnum(short type) {
        this.type = type;
    }

    public static String getCodeByValue(int type){
        String code = null;
        for (ModuleTypeEnum moduleTypeEnum : ModuleTypeEnum.values()){
            if(moduleTypeEnum.getType() == type) {
                code=moduleTypeEnum.name();
            }
        }
        return code;
    }

}
