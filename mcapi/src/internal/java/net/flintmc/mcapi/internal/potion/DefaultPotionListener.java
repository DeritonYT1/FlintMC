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

package net.flintmc.mcapi.internal.potion;

import com.google.inject.Singleton;
import java.util.Iterator;
import net.flintmc.framework.eventbus.event.subscribe.PreSubscribe;
import net.flintmc.mcapi.potion.effect.StatusEffect;
import net.flintmc.mcapi.potion.effect.StatusEffectInstance;
import net.flintmc.mcapi.potion.event.PotionAddEvent;
import net.flintmc.mcapi.potion.event.PotionRemoveEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent;
import net.flintmc.mcapi.potion.event.PotionStateUpdateEvent.State;
import net.flintmc.mcapi.potion.event.PotionUpdateEvent;

@Singleton
public class DefaultPotionListener {

  @PreSubscribe
  public void potionAdd(PotionAddEvent event) {
    event.getLivingEntity().addPotionEffect(event.getStatusEffectInstance());
  }

  @PreSubscribe
  public void potionRemove(PotionRemoveEvent event) {
    event.getLivingEntity().removePotionEffect(event.getStatusEffect());
  }

  @PreSubscribe
  public void potionState(PotionStateUpdateEvent event) {
    if (event.getState() == State.FINISHED) {
      event.getLivingEntity().removePotionEffect(event.getStatusEffectInstance().getPotion());
    }
  }

  @PreSubscribe
  public void potionUpdate(PotionUpdateEvent event) {
    Iterator<StatusEffect> iterator =
        event.getLivingEntity().getActivePotions().keySet().iterator();

    while (iterator.hasNext()) {
      StatusEffect statusEffect = iterator.next();
      StatusEffectInstance statusEffectInstance =
          event.getLivingEntity().getActivePotions().get(statusEffect);

      if (!statusEffectInstance.update()) {
        iterator.remove();
      }
    }
  }
}
