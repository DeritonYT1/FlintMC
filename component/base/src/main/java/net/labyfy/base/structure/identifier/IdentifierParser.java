package net.labyfy.base.structure.identifier;

import net.labyfy.base.structure.annotation.AnnotationCollector;
import net.labyfy.base.structure.annotation.LocatedIdentifiedAnnotation;
import net.labyfy.base.structure.property.Property;
import net.labyfy.base.structure.property.PropertyParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.function.Predicate;

@Singleton
public class IdentifierParser {

  private final PropertyParser propertyParser;

  @Inject
  private IdentifierParser(PropertyParser propertyParser) {
    this.propertyParser = propertyParser;
  }

  public Collection<Identifier.Base> parse(Class<?> clazz) {

    Collection<LocatedIdentifiedAnnotation> standaloneIdentifiers = new HashSet<>(this.findStandaloneClassIdentifiers(clazz));

    for (Method declaredMethod : clazz.getDeclaredMethods()) {
      standaloneIdentifiers.addAll(this.findStandaloneMethodIdentifiers(declaredMethod));
    }

    Collection<Identifier.Base> identifiers = new LinkedList<>();

    for (LocatedIdentifiedAnnotation standaloneIdentifier : standaloneIdentifiers) {
      Property.Base parse = this.propertyParser.parse(standaloneIdentifier);
      if (parse != null) identifiers.add(new Identifier.Base(parse));
    }

    return identifiers;
  }

  private Collection<LocatedIdentifiedAnnotation> findStandaloneMethodIdentifiers(Method method) {

    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && ((Identifier) identifierCandidate).parents().length == 0;

    Collection<LocatedIdentifiedAnnotation> identifiedAnnotations = new HashSet<>();

    for (Annotation transitiveAnnotation : AnnotationCollector.getTransitiveAnnotations(method)) {
      boolean found = false;
      for (Annotation annotation :
          AnnotationCollector.getTransitiveAnnotations(
              AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))) {
        if (!standalone.test(annotation)) continue;
        found = true;
      }

      if (found) {
        Annotation annotation =
            AnnotationCollector.getTransitiveAnnotations(
                    AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))
                .stream()
                .findAny()
                .orElse(null);
        identifiedAnnotations.add(
            new LocatedIdentifiedAnnotation(
                (Identifier) annotation,
                transitiveAnnotation,
                method,
                LocatedIdentifiedAnnotation.Type.METHOD,
                LocatedIdentifiedAnnotation.Type.METHOD));
      }
    }

    return identifiedAnnotations;
  }

  private Collection<LocatedIdentifiedAnnotation> findStandaloneClassIdentifiers(Class<?> clazz) {
    Predicate<Annotation> standalone =
        identifierCandidate ->
            AnnotationCollector.getRealAnnotationClass(identifierCandidate).equals(Identifier.class)
                && ((Identifier) identifierCandidate).parents().length == 0;

    Collection<LocatedIdentifiedAnnotation> identifiedAnnotations = new HashSet<>();

    for (Annotation transitiveAnnotation : AnnotationCollector.getTransitiveAnnotations(clazz)) {
      boolean found = false;
      for (Annotation annotation :
          AnnotationCollector.getTransitiveAnnotations(
              AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))) {
        if (!standalone.test(annotation)) continue;
        found = true;
      }
      if (found) {

        Annotation annotation =
            AnnotationCollector.getTransitiveAnnotations(
                    AnnotationCollector.getRealAnnotationClass(transitiveAnnotation))
                .stream()
                .findAny()
                .orElse(null);

        identifiedAnnotations.add(
            new LocatedIdentifiedAnnotation(
                (Identifier) annotation,
                transitiveAnnotation,
                clazz,
                LocatedIdentifiedAnnotation.Type.CLASS,
                LocatedIdentifiedAnnotation.Type.CLASS));
      }
    }

    return identifiedAnnotations;
  }
}
