package net.labyfy.internal.component.config.defval.defaults;

import com.google.inject.Singleton;
import net.labyfy.component.config.defval.annotation.DefaultNumber;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapper;
import net.labyfy.component.config.defval.mapper.DefaultAnnotationMapperHandler;
import net.labyfy.component.config.generator.method.ConfigObjectReference;

@Singleton
@DefaultAnnotationMapper(DefaultNumber.class)
public class NumberDefaultAnnotationMapper implements DefaultAnnotationMapperHandler<DefaultNumber> {
  @Override
  public Object getDefaultValue(ConfigObjectReference reference, DefaultNumber annotation) {
    return annotation.value();
  }
}
