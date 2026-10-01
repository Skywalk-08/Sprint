package framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Nom explicite du parametre de la requete associe au parametre de la methode.
 * Utilisee seulement si la compilation n'a pas ete faite avec -parameters.
 *
 *   public ModelView save(@Param("nom") String n, int age) { ... }
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Param {
    String value();
}
