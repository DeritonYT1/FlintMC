/*
 * FlintMC
 * Copyright (C) 2020-2021 LabyMedia GmbH and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.flintmc.mcapi.world.math;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedFactory;

/**
 * Represents a three-dimensional vector based on {@link Integer}.
 */
public interface Vector3I extends Comparable<Vector3I> {

  /**
   * Retrieves the X coordinate of this vector.
   *
   * @return The X coordinate.
   */
  int getX();

  /**
   * Retrieves the Y coordinate of this vector.
   *
   * @return The Y coordinate.
   */
  int getY();

  /**
   * Retrieves the Z coordinate of this vector.
   *
   * @return The Z coordinate.
   */
  int getZ();

  @Override
  default int compareTo(Vector3I compareTo) {
    if (this.getY() == compareTo.getY()) {
      return this.getZ() == compareTo.getZ()
          ? this.getX() - compareTo.getX()
          : this.getZ() - compareTo.getZ();
    } else {
      return this.getY() - compareTo.getY();
    }
  }

  /**
   * Calculates the cross product of this and the given {@link Vector3I}
   *
   * @param vector The vector for the calculation.
   * @return The calculated cross product.
   */
  Vector3I crossProduct(Vector3I vector);

  /**
   * Whether the given distance is larger than the square vector.
   *
   * @param vector   The vector to calculate the square distance.
   * @param distance The distance.
   * @return {@code true} if the distance larger than the square distance, otherwise {@code false}.
   */
  default boolean withinDistance(Vector3I vector, double distance) {
    return this.distanceSq(vector.getX(), vector.getY(), vector.getZ())
        < distance * distance;
  }

  /**
   * The square of the distance from this {@link Vector3I} to a specified point.
   *
   * @param vector The vector of the specified point to be measured against this {@link Vector3I}.
   * @return The square of the distance from this {@link Vector3I} to a specified point.
   */
  default int distanceSq(Vector3I vector) {
    return this.distanceSq(vector.getX(), vector.getY(), vector.getZ());
  }

  /**
   * The square of the distance from this {@link Vector3I} to a specified point.
   *
   * @param x The X coordinate of the specified point to be measured against this {@link Vector3I}.
   * @param y The Y coordinate of the specified point to be measured against this {@link Vector3I}.
   * @param z The Z coordinate of the specified point to be measured against this {@link Vector3I}.
   */
  int distanceSq(int x, int y, int z);

  /**
   * Calculates the distance between three points.
   *
   * @param vector The vector for the calculation.
   * @return The calculated distance between three points.
   */
  int manhattanDistance(Vector3I vector);

  /**
   * A factory class for the {@link Vector3I}.
   */
  @AssistedFactory(Vector3I.class)
  interface Factory {

    /**
     * Creates a new {@link Vector3I} with the given parameters.
     *
     * @param x The x position of the vector.
     * @param y The y position of the vector.
     * @param z The z position of the vector.
     * @return A created vector.
     */
    Vector3I create(@Assisted("x") int x, @Assisted("y") int y, @Assisted("z") int z);

    /**
     * Creates a new {@link Vector3I} with the given vector.
     *
     * @param vector The vector.
     * @return A created vector.
     */
    Vector3I create(@Assisted("vector") Vector3I vector);
  }
}
