package net.labyfy.component.transform.hook;

import javassist.CtClass;
import javassist.NotFoundException;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Deprecated
public enum HookFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String value) throws NotFoundException {
      Collection<CtClass> classes = HookFilters.collectSuperClassesRecursive(source);

      for (CtClass ctClass : classes) {
        if (value.equals(ctClass.getName())) {
          return true;
        }
      }

      return false;
    }
  };

  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass) throws NotFoundException {
    Collection<CtClass> classes = new HashSet<>();

    if (ctClass.getSuperclass() != null) {
      classes.add(ctClass.getSuperclass());
      classes.addAll(collectSuperClassesRecursive(ctClass.getSuperclass()));
    }

    classes.addAll(Arrays.asList(ctClass.getInterfaces()));

    for (CtClass value : ctClass.getInterfaces()) {
      classes.addAll(HookFilters.collectSuperClassesRecursive(value));
    }

    return Collections.unmodifiableCollection(classes);
  }

  public abstract boolean test(CtClass source, String value) throws NotFoundException;
}
