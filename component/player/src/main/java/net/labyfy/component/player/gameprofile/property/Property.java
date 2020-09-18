package net.labyfy.component.player.gameprofile.property;

import com.google.inject.assistedinject.Assisted;
import net.labyfy.component.inject.assisted.AssistedFactory;

import java.security.PublicKey;

/**
 * Represents the property of a game profile.
 */
public interface Property {

  /**
   * Retrieves the name of this property
   *
   * @return The property name
   */
  String getName();

  /**
   * Retrieves the value of this property
   *
   * @return The property value
   */
  String getValue();

  /**
   * Retrieves the signature of this property
   *
   * @return The property signature
   */
  String getSignature();

  /**
   * Whether this property has a signature.
   *
   * @return {@code true} if this property has a signature, otherwise {@code false}
   */
  boolean hasSignature();

  /**
   * Whether this property has a valid signature.
   *
   * @param publicKey The public key to signature verification
   * @return {@code true} if this property has a valid signature, otherwise {@code false}
   */
  boolean isSignatureValid(PublicKey publicKey);

  /**
   * A factory class for {@link Property}
   */
  @AssistedFactory(Property.class)
  interface Factory {

    /**
     * Creates a new {@link Property} with the given parameters.
     *
     * @param name  The name of this property.
     * @param value The value of this property.
     * @return The created property.
     */
    Property create(
            @Assisted("name") String name,
            @Assisted("value") String value
    );

    /**
     * Creates a new {@link Property} with the given parameters.
     *
     * @param name      The name of this property.
     * @param value     The value of this property.
     * @param signature The signature of this property.
     * @return The created property.
     */
    Property create(
            @Assisted("name") String name,
            @Assisted("value") String value,
            @Assisted("signature") String signature
    );

  }
}
