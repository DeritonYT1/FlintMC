package net.labyfy.component.tasks;

import com.google.inject.Key;

import java.lang.reflect.Method;
import java.util.Map;

public interface TaskExecutor {
  void register(Task task, double priority, Method method);

  void execute(String name);

  void execute(String name, Map<Key<?>, ?> arguments);
}
