package framework.binding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Conversion d'une valeur texte de la requete (request parameter) vers le type
 * Java du parametre de la methode. Les types "simples" sont conversionnables,
 * tout le reste est considere comme un objet (non bindable).
 */
public final class TypeConverter {

    private static final Set<Class<?>> SIMPLE_TYPES = new HashSet<>();

    static {
        SIMPLE_TYPES.add(String.class);
        SIMPLE_TYPES.add(Character.class);
        SIMPLE_TYPES.add(Boolean.class);
        SIMPLE_TYPES.add(Byte.class);
        SIMPLE_TYPES.add(Short.class);
        SIMPLE_TYPES.add(Integer.class);
        SIMPLE_TYPES.add(Long.class);
        SIMPLE_TYPES.add(Float.class);
        SIMPLE_TYPES.add(Double.class);
        SIMPLE_TYPES.add(BigInteger.class);
        SIMPLE_TYPES.add(BigDecimal.class);
    }

    private TypeConverter() {
    }

    public static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() || SIMPLE_TYPES.contains(type);
    }

    /**
     * @param rawValue valeur brute lue dans la requete (peut etre null)
     * @param type     type cible du parametre
     * @return la valeur convertie, ou null si rawValue est null
     * @throws BindingException si la valeur ne peut pas etre convertie
     */
    public static Object convert(String rawValue, Class<?> type) throws BindingException {
        if (rawValue == null) {
            return null;
        }
        String value = rawValue.trim();
        try {
            if (type == String.class || type == Object.class) {
                return value;
            }
            if (type == int.class || type == Integer.class) {
                return Integer.valueOf(value);
            }
            if (type == long.class || type == Long.class) {
                return Long.valueOf(value);
            }
            if (type == double.class || type == Double.class) {
                return Double.valueOf(value);
            }
            if (type == float.class || type == Float.class) {
                return Float.valueOf(value);
            }
            if (type == short.class || type == Short.class) {
                return Short.valueOf(value);
            }
            if (type == byte.class || type == Byte.class) {
                return Byte.valueOf(value);
            }
            if (type == boolean.class || type == Boolean.class) {
                return parseBoolean(value);
            }
            if (type == char.class || type == Character.class) {
                if (value.length() != 1) {
                    throw new BindingException("Un seul caractere est attendu pour le type 'char', recu : \"" + value + "\"");
                }
                return value.charAt(0);
            }
            if (type == BigInteger.class) {
                return new BigInteger(value);
            }
            if (type == BigDecimal.class) {
                return new BigDecimal(value);
            }
        } catch (NumberFormatException e) {
            throw new BindingException("Valeur invalide \"" + rawValue + "\" pour le type " + type.getSimpleName(), e);
        }
        throw new BindingException("Type non convertible : " + type.getName());
    }

    private static Boolean parseBoolean(String value) {
        return value.equalsIgnoreCase("true") || value.equals("1") || value.equalsIgnoreCase("on");
    }
}
