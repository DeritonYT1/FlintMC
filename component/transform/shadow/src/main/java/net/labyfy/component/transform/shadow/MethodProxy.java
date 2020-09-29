package net.labyfy.component.transform.shadow;

import net.labyfy.component.stereotype.annotation.Transitive;
import net.labyfy.component.stereotype.identifier.Identifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a method in a shadow that executes a private method that cannot be called from the outside.
 * Method must return the field type and must not have parameters.
 * <p>
 * Example:
 * {@code private void test(){...}}
 * can be accessed with
 * {@code void test();}
 *
 * @see Shadow
 * @see FieldGetter
 * @see FieldSetter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Identifier(requireParent = true)
@Transitive
public @interface MethodProxy {
}