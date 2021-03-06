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

package net.flintmc.framework.config.internal.generator.method.defaults;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import net.flintmc.framework.config.generator.GeneratingConfig;
import net.flintmc.framework.config.generator.ParsedConfig;
import net.flintmc.framework.config.internal.generator.method.DefaultConfigMethod;
import net.flintmc.framework.config.serialization.ConfigSerializationService;

import java.lang.reflect.Type;

public class ConfigGetterSetter extends DefaultConfigMethod {

  public ConfigGetterSetter(
      ConfigSerializationService serializationService,
      GeneratingConfig config,
      CtClass declaringClass,
      String name,
      CtClass methodType) {
    super(serializationService, config, declaringClass, name, methodType);
  }

  /** {@inheritDoc} */
  @Override
  public String getGetterName() {
    CtClass type = super.getStoredType();

    String prefix =
        type.equals(CtClass.booleanType) || type.getName().equals(Boolean.class.getName())
            ? "is"
            : "get";
    return prefix + super.getConfigName();
  }

  /** {@inheritDoc} */
  @Override
  public String getSetterName() {
    return "set" + super.getConfigName();
  }

  /** {@inheritDoc} */
  @Override
  public String[] getMethodNames() {
    return new String[] {this.getGetterName(), this.getSetterName()};
  }

  /** {@inheritDoc} */
  @Override
  public CtClass[] getTypes() {
    return new CtClass[] {super.getStoredType()};
  }

  /** {@inheritDoc} */
  @Override
  public Type getSerializedType() {
    return super.loadImplementationOrDefault(super.getStoredType());
  }

  /** {@inheritDoc} */
  @Override
  public void generateMethods(CtClass target) throws CannotCompileException {
    if (!this.hasMethod(target, this.getGetterName())) {
      this.insertGetter(target);
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      this.insertSetter(target);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void implementExistingMethods(CtClass target)
      throws CannotCompileException, NotFoundException {
    // validate if the methods exist or throw NotFoundException otherwise
    target.getDeclaredMethod(this.getGetterName());

    // add the write method to the setter
    try {
      this.insertSaveConfig(target.getDeclaredMethod(this.getSetterName()));
    } catch (NotFoundException e) {
      if (!super.getStoredType().isInterface()
          && !super.serializationService.hasSerializer(super.getStoredType())) {
        // ignore if it is an interface that will never get overridden by anything
        throw e;
      }
    }

    super.implementedMethods = true;
  }

  /** {@inheritDoc} */
  @Override
  public void addInterfaceMethods(CtClass target) throws CannotCompileException {
    if (!this.hasMethod(target, this.getGetterName())) {
      target.addMethod(
          CtNewMethod.make(this.methodType.getName() + " " + this.getGetterName() + "();", target));
    }

    if (!this.hasMethod(target, this.getSetterName())) {
      target.addMethod(
          CtNewMethod.make(
              "void " + this.getSetterName() + "(" + this.methodType.getName() + " arg);", target));
    }

    super.addedInterfaceMethods = true;
  }

  private void insertGetter(CtClass target) throws CannotCompileException {
    target.addMethod(CtNewMethod.getter(this.getGetterName(), super.generateOrGetField(target)));
  }

  private void insertSetter(CtClass target) throws CannotCompileException {
    target.addMethod(
        CtNewMethod.make(
            "public void "
                + this.getSetterName()
                + "("
                + this.methodType.getName()
                + " arg) { "
                + "this."
                + this.configName
                + " = arg;"
                + "configStorageProvider.write(("
                + ParsedConfig.class.getName()
                + ") this.config);"
                + "}",
            target));
  }
}
