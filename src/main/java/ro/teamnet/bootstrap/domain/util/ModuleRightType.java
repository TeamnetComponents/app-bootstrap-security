package ro.teamnet.bootstrap.domain.util;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains String representation of {@link ro.teamnet.bootstrap.domain.ModuleRight#right} values.
 */
public class ModuleRightType {

    public static final short NO_ACCESS = 0;
    public static final short READ_ACCESS = 1;
    public static final short WRITE_ACCESS = 2;
    public static final short INSERT_ACCESS = 4;
    public static final short DELETE_ACCESS = 8;


    /**
     * This method return a Map object whitch contains all the class constant names keyed by their values
     * @return HashMap with all the class constant names keyed by their values
     */
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
