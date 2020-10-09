package net.labyfy.internal.component.event;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.component.event.TickEvent;
import net.labyfy.component.eventbus.EventBus;
import net.labyfy.component.processing.autoload.AutoLoad;
import net.labyfy.component.transform.hook.Hook;

@Singleton
@AutoLoad
public class TickEventInjector {

  private final EventBus eventBus;
  private final TickEvent generalTickEvent;
  private final TickEvent gameRenderTickEvent;
  private final TickEvent worldRenderTickEvent;

  @Inject
  public TickEventInjector(EventBus eventBus, TickEvent.Factory factory) {
    this.eventBus = eventBus;
    this.generalTickEvent = factory.create(TickEvent.Type.GENERAL);
    this.gameRenderTickEvent = factory.create(TickEvent.Type.GAME_RENDER);
    this.worldRenderTickEvent = factory.create(TickEvent.Type.WORLD_RENDER);
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.Minecraft",
      methodName = "runTick"
  )
  public void handleGeneralTick(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.generalTickEvent, executionTime);
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.GameRenderer",
      methodName = "tick"
  )
  public void handleGameRenderTick(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.gameRenderTickEvent, executionTime);
  }

  @Hook(
      executionTime = {Hook.ExecutionTime.BEFORE, Hook.ExecutionTime.AFTER},
      className = "net.minecraft.client.renderer.WorldRenderer",
      methodName = "tick"
  )
  public void hookWorldRenderTick(Hook.ExecutionTime executionTime) {
    this.eventBus.fireEvent(this.worldRenderTickEvent, executionTime);
  }
}
