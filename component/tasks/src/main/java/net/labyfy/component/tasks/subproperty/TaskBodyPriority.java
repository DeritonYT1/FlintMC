package net.labyfy.component.tasks.subproperty;

import net.labyfy.base.structure.identifier.Identifier;
import net.labyfy.base.structure.annotation.Transitive;
import net.labyfy.component.tasks.Task;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Identifier(parents = Task.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Transitive
public @interface TaskBodyPriority {

  double value() default 0;

}