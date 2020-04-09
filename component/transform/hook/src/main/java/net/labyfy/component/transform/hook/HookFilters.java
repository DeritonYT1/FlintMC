package net.labyfy.component.transform.hook;

import com.google.inject.Key;
import com.google.inject.name.Names;
import javassist.CtClass;
import net.labyfy.component.inject.InjectionHolder;

import java.util.*;

public enum HookFilters {
  SUBCLASS_OF {
    public boolean test(CtClass source, String value) {
      return HookFilters.collectSuperClassesRecursive(source).stream()
          .anyMatch(clazz -> clazz.getName().equals(value));
    }
  };


  private static Collection<CtClass> collectSuperClassesRecursive(CtClass ctClass) {
    try {

      Collection<CtClass> classes = new HashSet<>();
      if (ctClass.getSuperclass() != null) {
        classes.add(ctClass.getSuperclass());
        classes.addAll(collectSuperClassesRecursive(ctClass.getSuperclass()));
      }
      classes.addAll(Arrays.asList(ctClass.getInterfaces()));
      Arrays.stream(ctClass.getInterfaces())
          .map(HookFilters::collectSuperClassesRecursive)
          .forEach(classes::addAll);

      return Collections.unmodifiableCollection(classes);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return Collections.emptyList();
  }

  public abstract boolean test(CtClass source, String value);
}