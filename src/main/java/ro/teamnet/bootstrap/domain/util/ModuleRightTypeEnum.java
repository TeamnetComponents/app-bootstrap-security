package ro.teamnet.bootstrap.domain.util;


/**
 * This Enum contains all the CRUD permissions and their numeric representations
 */
public enum ModuleRightTypeEnum {

    NO_ACCESS((short)0),
    READ_ACCESS((short)1),
    WRITE_ACCESS((short)2),
    INSERT_ACCESS((short)4),
    DELETE_ACCESS((short)8);

    private short right;

    public short getRight() {
        return right;
    }

    private ModuleRightTypeEnum(short right) {
        this.right = right;
    }

    public static String getCodeByValue(int moduleRight){
        String code = null;
        for (ModuleRightTypeEnum moduleRightTypeEnum : ModuleRightTypeEnum.values()){
            if(moduleRightTypeEnum.getRight() == moduleRight) {
                code=moduleRightTypeEnum.name();
            }
        }
        return code;
    }

}
