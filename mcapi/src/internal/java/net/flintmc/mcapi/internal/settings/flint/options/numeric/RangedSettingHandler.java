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

package net.flintmc.mcapi.internal.settings.flint.options.numeric;

import com.google.gson.JsonObject;
import net.flintmc.mcapi.chat.annotation.ComponentAnnotationSerializer;
import net.flintmc.mcapi.chat.serializer.ComponentSerializer;
import net.flintmc.mcapi.settings.flint.options.numeric.Range;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplay;
import net.flintmc.mcapi.settings.flint.options.numeric.display.NumericDisplays;
import net.flintmc.mcapi.settings.flint.registered.RegisteredSetting;

public class RangedSettingHandler {

  private final ComponentSerializer.Factory serializerFactory;
  private final ComponentAnnotationSerializer annotationSerializer;

  protected RangedSettingHandler(
      ComponentSerializer.Factory serializerFactory,
      ComponentAnnotationSerializer annotationSerializer) {
    this.serializerFactory = serializerFactory;
    this.annotationSerializer = annotationSerializer;
  }

  protected boolean inRange(Range range, Object value) {
    if (!(value instanceof Number)) {
      return false;
    }

    Number number = (Number) value;
    double d = number.doubleValue();
    int decimals = range.decimals();

    if (decimals == 0 && d != (long) d) {
      return false;
    }

    int decimalLength = String.valueOf(d - (long) d).length() - 2; // 2 = '0.'
    if (decimalLength > decimals) {
      return false;
    }

    return d >= range.min() && d <= range.max();
  }

  protected JsonObject serialize(Number value, Range range, RegisteredSetting setting) {
    JsonObject object = new JsonObject();

    object.addProperty("value", value);

    JsonObject rangeObject = new JsonObject();
    object.add("range", rangeObject);

    if (range.min() != Double.MIN_VALUE) {
      rangeObject.addProperty("min", range.min());
    }
    if (range.max() != Double.MAX_VALUE) {
      rangeObject.addProperty("max", range.max());
    }
    rangeObject.addProperty("decimals", range.decimals());

    NumericDisplays repeatable =
        setting.getReference().findLastAnnotation(NumericDisplays.class);
    if (repeatable != null) {
      JsonObject displays = new JsonObject();
      object.add("displays", displays);

      for (NumericDisplay display : repeatable.value()) {
        displays.add(
            String.valueOf(display.value()),
            this.serializerFactory
                .gson()
                .getGson()
                .toJsonTree(this.annotationSerializer.deserialize(display.display())));
      }
    }

    return object;
  }
}
