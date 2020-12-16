package net.flintmc.framework.inject;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Builder for fields generated by the {@link InjectionUtils}.
 *
 * <p>The necessary values to be provided in this builder are the following:
 *
 * <ul>
 *   <li>{@link #target(CtClass)} as as the class where the field should be located
 *   <li>One of {@link #inject(CtClass)}, {@link #inject(Class)} or {@link #inject(String)}
 * </ul>
 *
 * For generating a field, you can use {@link #generate()} after the necessary values have been set.
 *
 * @see InjectionUtils#addInjectedField(CtClass, String, String, boolean, boolean)
 */
public interface InjectedFieldBuilder {

  /**
   * Sets the class where the generated field should be declared. The class must not be an
   * interface.
   *
   * @param target The non-null class where the new field should be declared
   * @return This builder for chaining
   */
  InjectedFieldBuilder target(CtClass target);

  /**
   * Sets the type of the field which will also be injected from the Injector.
   *
   * @param type The non-null type of the class that should be injected
   * @return This builder for chaining
   */
  InjectedFieldBuilder inject(Class<?> type);

  /**
   * Sets the type of the field which will also be injected from the Injector.
   *
   * @param type The non-null type of the class that should be injected
   * @return This builder for chaining
   */
  InjectedFieldBuilder inject(CtClass type);

  /**
   * Sets the type of the field which will also be injected from the Injector.
   *
   * @param typeName The non-null name of the type of the class that should be injected
   * @return This builder for chaining
   */
  InjectedFieldBuilder inject(String typeName);

  /**
   * Sets the name of the field that should be generated.
   *
   * @param fieldName The non-null name of the field that should be generated
   * @return This builder for chaining
   */
  InjectedFieldBuilder fieldName(String fieldName);

  /**
   * Sets that multiple fields should be injected when the field is {@link #generate() generated}.
   * Otherwise, only one field with the given {@link #inject(String) type} will be injected per
   * type.
   *
   * @return This builder for chaining
   */
  InjectedFieldBuilder multipleInstances();

  /**
   * Sets that the generated field should not be static. If not called, the field will be static.
   *
   * @return This builder for chaining
   */
  InjectedFieldBuilder notStatic();

  /**
   * Generates the field from this builder and adds it to the given {@link #target(CtClass)}. If not
   * otherwise set via {@link #notStatic()}, the field will be static. This method may also be
   * called multiple times per builder instance, but if {@link #fieldName(String)} has been set and
   * {@link #multipleInstances()} has been called, the {@link #fieldName(String)} needs to be
   * changed before because the same field cannot exist multiple times.
   *
   * @return The new non-null field
   * @throws CannotCompileException If a compiler error occurred while generating the field
   * @see InjectedFieldBuilder
   */
  CtField generate() throws CannotCompileException;

  /** Factory for the {@link InjectedFieldBuilder}. */
  @AssistedFactory(InjectedFieldBuilder.class)
  interface Factory {

    /**
     * Creates a new {@link InjectedFieldBuilder} without any parameters.
     *
     * @return The new non-null {@link InjectedFieldBuilder}
     */
    InjectedFieldBuilder create();
  }
}
