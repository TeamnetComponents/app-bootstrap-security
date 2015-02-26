package ro.teamnet.bootstrap.domain.util;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ModuleRightType {

    public static final short NO_ACCESS = 0;
    public static final short READ_ACCESS = 1;
    public static final short WRITE_ACCESS = 2;
    public static final short INSERT_ACCESS = 4;
    public static final short DELETE_ACCESS = 8;

    public static final  Map<Short,String>  getConstantsMap() {
        ModuleRightType moduleRightType = new ModuleRightType();
        Map<Short,String> map = new HashMap<>();
        for(Field field : moduleRightType.getClass().getDeclaredFields()){
            try {
                map.put(field.getShort(null),field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
