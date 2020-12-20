package net.flintmc.framework.inject.internal;

import com.google.common.base.Preconditions;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.framework.inject.InjectionUtils;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;

/** {@inheritDoc} */
@Implement(InjectedFieldBuilder.class)
public class DefaultInjectedFieldBuilder implements InjectedFieldBuilder {

  private final InjectionUtils injectionUtils;

  private CtClass target;
  private String injectedTypeName;
  private String fieldName;
  private boolean singletonInstance;
  private boolean notStatic;

  @AssistedInject
  private DefaultInjectedFieldBuilder(InjectionUtils injectionUtils) {
    this.injectionUtils = injectionUtils;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder target(CtClass target) {
    this.target = target;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(Class<?> type) {
    return this.inject(type.getName());
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(CtClass type) {
    return this.inject(type.getName());
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder inject(String typeName) {
    this.injectedTypeName = typeName;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder fieldName(String fieldName) {
    this.fieldName = fieldName;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder multipleInstances() {
    this.singletonInstance = false;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public InjectedFieldBuilder notStatic() {
    this.notStatic = true;
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public CtField generate() throws CannotCompileException {
    Preconditions.checkNotNull(this.target, "No target defined");
    Preconditions.checkNotNull(this.injectedTypeName, "No inject type defined");

    String fieldName =
        this.fieldName != null ? this.fieldName : this.injectionUtils.generateInjectedFieldName();

    return this.injectionUtils.addInjectedField(
        this.target, fieldName, this.injectedTypeName, this.singletonInstance, !this.notStatic);
  }
}