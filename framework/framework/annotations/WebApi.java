package framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marque une méthode de contrôleur comme endpoint d'API.
 *
 * La valeur retournée n'est plus interprétée comme une vue : elle est
 * sérialisée en JSON et écrite directement dans la réponse
 * (Content-Type: application/json). Si la méthode retourne un ModelView,
 * ce sont les données du modèle qui sont sérialisées, sans passer par la JSP.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WebApi {
}
