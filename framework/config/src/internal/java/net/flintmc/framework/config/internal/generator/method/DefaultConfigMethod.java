/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.framework.config.internal.generator.method;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.generator.method.ConfigMethod;
import net.flintmc.framework.config.serialization.ConfigSerializationService;
import net.flintmc.framework.inject.primitive.InjectionHolder;
import net.flintmc.framework.stereotype.PrimitiveTypeLoader;
import net.flintmc.framework.stereotype.service.CtResolver;
import java.util.HashMap;
import java.util.Map;

public abstract class DefaultConfigMethod implements ConfigMethod {

  protected final ConfigSerializationService serializationService;
  protected final GeneratingConfig config;
  protected final CtClass declaringClass;
  protected final String configName;
  protected final CtClass methodType;

  protected boolean implementedMethods;
  protected boolean addedInterfaceMethods;
  private String[] pathPrefix;

  private final Map<CtField, String> fieldValues;

  public DefaultConfigMethod(
      ConfigSerializationService serializationService,
      GeneratingConfig config,
      CtClass declaringClass,
      String configName,
      CtClass methodType) {
    this.serializationService = serializationService;
    this.config = config;
    this.declaringClass = declaringClass;
    this.configName = configName;
    this.methodType = methodType;

    this.fieldValues = new HashMap<>();
  }

  /** {@inheritDoc} */
  @Override
  public GeneratingConfig getConfig() {
    return this.config;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getDeclaringClass() {
    return this.declaringClass;
  }

  /** {@inheritDoc} */
  @Override
  public String getConfigName() {
    return this.configName;
  }

  /** {@inheritDoc} */
  @Override
  public CtClass getStoredType() {
    return this.methodType;
  }

  /** {@inheritDoc} */
  @Override
  public String[] getPathPrefix() {
    return this.pathPrefix;
  }

  /** {@inheritDoc} */
  @Override
  public void setPathPrefix(String[] pathPrefix) throws IllegalStateException {
    if (this.pathPrefix != null) {
      throw new IllegalStateException("The pathPrefix cannot be set twice");
    }
    this.pathPrefix = pathPrefix;
  }

  protected Class<?> loadImplementationOrDefault(CtClass superClass) {
    CtClass implementation = this.config.getGeneratedImplementation(superClass.getName());

    Class<?> primitiveType = PrimitiveTypeLoader.getWrappedClass(superClass.getName());
    if (primitiveType != null) { // primitive classes can't be loaded with the ClassLoader
      return primitiveType;
    }
    return CtResolver.get(implementation != null ? implementation : superClass);
  }

  /** {@inheritDoc} */
  @Override
  public void requireNoImplementation() {
    this.implementedMethods = true;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasImplementedExistingMethods() {
    return this.implementedMethods;
  }

  /** {@inheritDoc} */
  @Override
  public boolean hasAddedInterfaceMethods() {
    return this.addedInterfaceMethods;
  }

  protected CtField generateOrGetField(CtClass target) throws CannotCompileException {
    return this.generateOrGetField(target, null);
  }

  protected CtField generateOrGetField(CtClass target, String defaultValue)
      throws CannotCompileException {
    try {
      return target.getDeclaredField(this.configName);
    } catch (NotFoundException ignored) {
    }

    String fieldValue = defaultValue;

    if (defaultValue == null
        && this.methodType.isInterface()
        && !this.serializationService.hasSerializer(this.getStoredType())) {
      CtClass implementation = this.config.getGeneratedImplementation(this.methodType.getName());

      if (implementation != null) {
        fieldValue = "new " + implementation.getName() + "(this.config)";
      } else {
        fieldValue =
            InjectionHolder.class.getName()
                + ".getInjectedInstance("
                + this.methodType.getName()
                + ".class)";
      }
    }

    String fieldSrc = "private " + this.methodType.getName() + " " + this.configName + ";";

    CtField field = CtField.make(fieldSrc, target);
    target.addField(field);

    if (fieldValue != null) {
      this.fieldValues.put(field, fieldValue);
    }

    return field;
  }

  /** {@inheritDoc} */
  @Override
  public String getFieldValuesCreator() {
    if (this.fieldValues.isEmpty()) {
      return "";
    }

    StringBuilder builder = new StringBuilder();

    this.fieldValues.forEach(
        (field, value) ->
            builder.append("this.").append(field.getName()).append('=').append(value).append(';'));

    return builder.toString();
  }

  protected boolean hasMethod(CtClass type, String name) {
    try {
      type.getDeclaredMethod(name);
      return true;
    } catch (NotFoundException e) {
      return false;
    }
  }

  protected void insertSaveConfig(CtMethod method) throws CannotCompileException {
    CtClass declaring = method.getDeclaringClass();

    try {
      declaring.getField("config");
    } catch (NotFoundException e) {
      String configName = this.config.getBaseClass().getName();

      String configGetter = "this";
      if (!this.config.getBaseClass().subclassOf(declaring)) {
        configGetter =
            InjectionHolder.class.getName() + ".getInjectedInstance(" + configName + ".class)";
      }

      declaring.addField(
          CtField.make(
              "private final transient " + configName + " config = " + configGetter + ";",
              declaring));
    }

    String src = "configStorageProvider.write((" + ParsedConfig.class.getName() + ") this.config);";
    if (method.getMethodInfo().getCodeAttribute() != null) {
      method.insertAfter(src);
    } else {
      method.setBody(src);
    }
  }
}
