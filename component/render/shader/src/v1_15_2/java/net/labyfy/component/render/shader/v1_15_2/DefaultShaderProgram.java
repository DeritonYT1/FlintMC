package net.labyfy.component.render.shader.v1_15_2;

import com.google.inject.assistedinject.AssistedInject;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.render.shader.ShaderException;
import net.labyfy.component.render.shader.ShaderProgram;
import net.labyfy.component.render.shader.ShaderUniform;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/** {@inheritDoc} */
@Implement(ShaderProgram.class)
public class DefaultShaderProgram implements ShaderProgram {

  private int shaderProgram;

  private int vertexShader;
  private int fragmentShader;

  private boolean linked;

  private final List<ShaderUniform> providedShaderUniforms;

  @AssistedInject
  private DefaultShaderProgram() {
    this.providedShaderUniforms = new ArrayList<>();
    this.shaderProgram = 0;
    this.vertexShader = 0;
    this.fragmentShader = 0;
    this.linked = true;
  }

  /** {@inheritDoc} */
  @Override
  public void addVertexShader(InputStream shader) throws ShaderException {
    try {
      addVertexShader(IOUtils.toString(shader, StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ShaderException("Couldn't load shader source.", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addFragmentShader(InputStream shader) throws ShaderException {
    try {
      addFragmentShader(IOUtils.toString(shader, StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ShaderException("Couldn't load shader source.", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void addVertexShader(String shader) throws ShaderException {
    if (this.vertexShader != 0)
      throw new ShaderException("Only one vertex shader can be added to a shader program.");
    this.vertexShader = addShader(shader, GL_VERTEX_SHADER);
  }

  /** {@inheritDoc} */
  @Override
  public void addFragmentShader(String shader) throws ShaderException {
    if (this.fragmentShader != 0)
      throw new ShaderException("Only one fragment shader can be added to a shader program.");
    this.fragmentShader = addShader(shader, GL_FRAGMENT_SHADER);
  }

  private int addShader(String shaderSource, int type) throws ShaderException {
    int shaderId = glCreateShader(type);
    glShaderSource(shaderId, shaderSource);
    glCompileShader(shaderId);

    if (glGetShaderi(shaderId, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new ShaderException(glGetShaderInfoLog(shaderId));
    }

    return shaderId;
  }

  /** {@inheritDoc} */
  @Override
  public void link() throws ShaderException {
    this.shaderProgram = glCreateProgram();

    if (this.vertexShader != 0) glAttachShader(this.shaderProgram, this.vertexShader);
    if (this.fragmentShader != 0) glAttachShader(this.shaderProgram, this.fragmentShader);

    glLinkProgram(this.shaderProgram);

    if (glGetProgrami(this.shaderProgram, GL_LINK_STATUS) != GL_TRUE) {
      throw new ShaderException(glGetShaderInfoLog(this.vertexShader));
    }

    this.linked = true;

    if (this.vertexShader != 0) glDeleteShader(this.vertexShader);
    if (this.fragmentShader != 0) glDeleteShader(this.fragmentShader);
  }

  /** {@inheritDoc} */
  @Override
  public void useShader() {
    if (this.shaderProgram <= 0)
      throw new IllegalStateException("Shader program has not been successfully linked yet.");
    glUseProgram(shaderProgram);
    this.updateProvidedUniforms();
  }

  /** {@inheritDoc} */
  @Override
  public void stopShader() {
    glUseProgram(0);
  }

  /** {@inheritDoc} */
  @Override
  public void updateProvidedUniforms() {
    providedShaderUniforms.forEach(ShaderUniform::updateFromValueProvider);
  }

  /** {@inheritDoc} */
  @Override
  public void addProvidedUniform(ShaderUniform uniform) {
    this.providedShaderUniforms.add(uniform);
  }

  /** {@inheritDoc} */
  @Override
  public int getProgramID() {
    return this.shaderProgram;
  }

  /** {@inheritDoc} */
  @Override
  public int getVertexShaderID() {
    return this.vertexShader;
  }

  /** {@inheritDoc} */
  @Override
  public int getFragmentShaderID() {
    return this.fragmentShader;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isLinked() {
    return this.linked;
  }
}