package ro.teamnet.bootstrap.domain.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains String representation of {@link ro.teamnet.bootstrap.domain.Module#type} values.
 */
public class ModuleType {

    public static final Short MENU = 1;
    public static final Short ENTITY = 2;
    public static final Short FIELD = 3;
    public static final Short OTHER = 4;

    /**
     * This method return a Map object whitch contains all the class constant names keyed by their values
     * @return HashMap with all the class constant names keyed by their values
     */
    public static final Map<Short,String> getConstantsMap() {
        ModuleType moduleType = new ModuleType();
        Map<Short,String> map = new HashMap<>();
        for(Field field : moduleType.getClass().getDeclaredFields()){
            try {
                map.put(field.getShort(null),field.getName());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
