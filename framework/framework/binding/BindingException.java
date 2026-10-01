package framework.binding;

/**
 * Levee lorsqu'une valeur de la requete ne peut pas etre associee a un
 * parametre de la methode du controller (mauvais type, parametre manquant,
 * ou tentative de binding d'un objet).
 */
public class BindingException extends Exception {

    public BindingException(String message) {
        super(message);
    }

    public BindingException(String message, Throwable cause) {
        super(message, cause);
    }
}
