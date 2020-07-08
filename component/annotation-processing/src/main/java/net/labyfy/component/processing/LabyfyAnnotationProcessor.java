package net.labyfy.component.processing;

import com.google.auto.service.AutoService;
import net.labyfy.component.processing.exception.ProcessingException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Root entry point for the java annotation processing environment. The corresponding META-INF/services
 * file is generated by the {@link AutoService} annotation on this class.
 */
@AutoService(Processor.class)
public class LabyfyAnnotationProcessor extends AbstractProcessor {
  // Internal state of the processor
  private final ProcessorState state;

  /**
   * Instantiates the annotation processor. This is called by the java compiler.
   */
  public LabyfyAnnotationProcessor() {
    this.state = new ProcessorState();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    state.init(processingEnv);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    state.round(roundEnv);

    try {
      for (TypeElement element : annotations) {
        state.process(element);
      }
    } catch (ProcessingException exception) {
      // Catch and rethrow, adding an error to the environment
      // This allows the IDE to visually mark the failing element before the compiler crashes
      // due to our exception
      processingEnv
          .getMessager()
          .printMessage(
              Diagnostic.Kind.ERROR,
              "Exception thrown while processing annotations: " + exception.getMessage(),
              exception.getSourceElement());
      throw exception;
    }

    if (roundEnv.processingOver()) {
      // We have reached the last round, finalize the state and write out the generated files
      state.finish();
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SourceVersion getSupportedSourceVersion() {
    // We always support the latest version
    return SourceVersion.latestSupported();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<String> getSupportedAnnotationTypes() {
    // Process every annotation
    return new HashSet<>(Collections.singletonList("*"));
  }
}
