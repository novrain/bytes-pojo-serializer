package cn.iota.jiot.serialization.reflect;

import java.util.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import cn.iota.jiot.serialization.meta.SerializeField;
import cn.iota.jiot.serialization.meta.SerializeFieldOrder;

public class ReflectionUtil {

    private ReflectionUtil() {
    }

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(9);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        primitiveWrapperTypeMap.put(Void.class, void.class);

    }

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    public static boolean isPrimitiveArray(Class<?> clazz) {
        return (clazz.isArray() && clazz.getComponentType().isPrimitive());
    }

    public static boolean isStringArray(Class<?> clazz) {
        return (clazz.isArray() && clazz.getComponentType() == String.class);
    }

    public static boolean isPrimitiveWrapperArray(Class<?> clazz) {
        return (clazz.isArray() && isPrimitiveWrapper(clazz.getComponentType()));
    }

    public static boolean isPrimitiveOrWrapperArray(Class<?> clazz) {
        Class<?> type = clazz.getComponentType();
        return (clazz.isArray() && (type.isPrimitive() || isPrimitiveWrapper(type)));
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    public static boolean isPrimitiveList(Field field) {
        Class<?> type = field.getType();
        if (type == List.class) {
            Type gType = field.getGenericType();
            if (gType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) gType;
                Class<?> itemType = (Class<?>) pType.getActualTypeArguments()[0];
                return itemType.isPrimitive();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isPrimitiveWrapperList(Field field) {
        Class<?> type = field.getType();
        if (type == List.class) {
            Type gType = field.getGenericType();
            if (gType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) gType;
                Class<?> itemType = (Class<?>) pType.getActualTypeArguments()[0];
                return isPrimitiveWrapper(itemType);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isPrimitiveOrWrapperList(Field field) {
        Class<?> type = field.getType();
        if (type == List.class) {
            Type gType = field.getGenericType();
            if (gType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) gType;
                Class<?> itemType = (Class<?>) pType.getActualTypeArguments()[0];
                return itemType.isPrimitive() || isPrimitiveWrapper(itemType);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isStringList(Field field) {
        Class<?> type = field.getType();
        if (type == List.class) {
            Type gType = field.getGenericType();
            if (gType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) gType;
                Class<?> itemType = (Class<?>) pType.getActualTypeArguments()[0];
                return itemType == String.class;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static Class<?> getElementType(Field field) {
        Class<?> type = field.getType();
        if (type == List.class) {
            Type gType = field.getGenericType();
            if (gType instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) gType;
                Class<?> itemType = (Class<?>) pType.getActualTypeArguments()[0];
                return itemType;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> String[] findSerializeFieldOrder(Class<T> clazz) {
        SerializeFieldOrder order = clazz.getAnnotation(SerializeFieldOrder.class);
        return order == null ? null : order.value();
    }

    /**
     * 
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Map<String, Field> findSerializeFields(Class<T> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> all = new LinkedHashMap<>(fields.length * 2);
        for (Field f : fields) {
            if (f.getAnnotation(SerializeField.class) != null) {
                all.put(f.getName(), f);
            }
        }
        Map<String, Field> ordered = new LinkedHashMap<>(fields.length * 2);
        String[] fieldOrder = ReflectionUtil.findSerializeFieldOrder(clazz);
        if (fieldOrder != null) {
            for (String name : fieldOrder) {
                Field f = all.remove(name);
                if (f != null) {
                    ordered.put(name, f);
                }
            }
            ordered.putAll(all);
        } else {
            Map<Integer, Field> byIndex = new TreeMap<>();
            Iterator<Map.Entry<String, Field>> it = all.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Field> entry = it.next();
                Field f = entry.getValue();
                int index = f.getAnnotation(SerializeField.class).index();
                if (index != -1) {
                    byIndex.put(index, f);
                    it.remove();
                }
            }
            for (Field f : byIndex.values()) {
                ordered.put(f.getName(), f);
            }
            ordered.putAll(all);
        }
        return ordered;
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> objClass) {
        T t = null;
        if (objClass != null) {
            try {
                if (objClass.isPrimitive()) {
                    if (objClass == Byte.class) {
                        t = (T) Byte.valueOf((byte) 0);
                    } else if (objClass == Short.class) {
                        t = (T) Short.valueOf((short) 0);
                    } else if (objClass == Integer.class) {
                        t = (T) Integer.valueOf(0);
                    } else if (objClass == Long.class) {
                        t = (T) Long.valueOf(0);
                    }
                } else {
                    Constructor<T> c = objClass.getDeclaredConstructor();
                    if (c != null) {
                        boolean flag = c.canAccess(null);
                        c.setAccessible(true);
                        t = c.newInstance();
                        c.setAccessible(flag);
                    }
                }
            } catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            } catch (IllegalArgumentException e) {
            } catch (InvocationTargetException e) {
            } catch (NoSuchMethodException e) {
            } catch (SecurityException e) {
            }
        }
        return t;
    }

}
