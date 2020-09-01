package net.labyfy.component.player.gameprofile;

import net.labyfy.component.player.gameprofile.property.PropertyMap;

import java.util.UUID;

/**
 * Represents the game profile of a player
 */
public interface GameProfile {

    /**
     * Creates a new game profile
     *
     * @param uniqueId   The unique identifier of this game profile
     * @param name       The display name of this game profile
     * @param properties The properties of this game profile
     * @return this profile, for chaining
     */
    GameProfile createProfile(UUID uniqueId, String name, PropertyMap properties);

    /**
     * Retrieves the unique identifier of this game profile.
     * This may be null for partial profile data if constructed manually.
     *
     * @return the unique identifier of the profile
     */
    UUID getUniqueId();

    /**
     * Retrieves the display name of this game profile.
     * This may be null for partial profile data if constructed manually.
     *
     * @return the name of the profile
     */
    String getName();

    /**
     * Retrieves any known properties about this game profile.
     *
     * @return a modifiable map of profile properties.
     */
    PropertyMap getProperties();

    /**
     * Whether this profile is complete.
     * A complete profile has no empty fields. Partial profiles my
     * be constructed manually and used as input to methods.
     *
     * @return {@code true} if this profile is complete (as opposed to partial), otherwise {@code false}
     */
    boolean isComplete();

    /**
     * Whether this profile is legacy.
     *
     * @return {@code true} if this profile is legacy, otherwise {@code false}
     */
    boolean isLegacy();

    /**
     * Builder class for {@link GameProfile}
     */
    interface Builder {

        /**
         * Sets the unique identifier for this game profile
         *
         * @param uniqueId The unique identifier of this game profile
         * @return this builder, for chaining
         */
        Builder setUniqueId(UUID uniqueId);

        /**
         * Sets the display name for this game profile
         *
         * @param name The display name of this game profile
         * @return this builder, for chaining
         */
        Builder setName(String name);

        /**
         * Sets the properties for this game profile
         *
         * @param properties The game profile properies
         * @return this builder, for chaining
         */
        Builder setProperties(PropertyMap properties);

        /**
         * Built the game profile
         *
         * @return the built game profile
         */
        GameProfile build();

    }

}
