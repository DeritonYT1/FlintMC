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

package net.flintmc.mcapi.settings.flint.annotation.ui;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.flintmc.framework.config.annotation.Config;
import net.flintmc.framework.config.generator.method.ConfigObjectReference;
import net.flintmc.mcapi.settings.flint.annotation.ApplicableSetting;

/**
 * Sets a category for a specific {@link ApplicableSetting} in a {@link Config}. It can be used on
 * any method or class that is associated with the given value, see {@link
 * ConfigObjectReference#findLastAnnotation(Class)} for more information.
 *
 * <p>For this to work, a category with the {@link #value() given name} needs to exist, to define
 * one, {@link DefineCategory} may be used.
 *
 * @see Config
 * @see DefineCategory
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Category {

  /**
   * Retrieves the name of the category to get the information like the DisplayName and Description
   * from the registry.
   *
   * @return The name of this category
   */
  String value();
}
