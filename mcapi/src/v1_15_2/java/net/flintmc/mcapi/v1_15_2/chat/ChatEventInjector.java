package net.flintmc.mcapi.v1_15_2.chat;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.flintmc.framework.eventbus.EventBus;
import net.flintmc.framework.eventbus.event.subscribe.Subscribe;
import net.flintmc.framework.inject.InjectedFieldBuilder;
import net.flintmc.mcapi.chat.MinecraftComponentMapper;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.event.ChatReceiveEvent;
import net.flintmc.mcapi.chat.event.ChatSendEvent;
import net.flintmc.transform.javassist.ClassTransform;
import net.flintmc.transform.javassist.ClassTransformContext;
import net.minecraft.util.text.ITextComponent;

@Singleton
public class ChatEventInjector {

  private final EventBus eventBus;
  private final InjectedFieldBuilder.Factory fieldBuilderFactory;
  private final MinecraftComponentMapper componentMapper;

  private final ChatSendEvent.Factory sendFactory;
  private final ChatReceiveEvent.Factory receiveFactory;

  @Inject
  private ChatEventInjector(
      EventBus eventBus,
      InjectedFieldBuilder.Factory fieldBuilderFactory,
      MinecraftComponentMapper componentMapper,
      ChatSendEvent.Factory sendFactory,
      ChatReceiveEvent.Factory receiveFactory) {
    this.eventBus = eventBus;
    this.fieldBuilderFactory = fieldBuilderFactory;
    this.componentMapper = componentMapper;
    this.sendFactory = sendFactory;
    this.receiveFactory = receiveFactory;
  }

  @ClassTransform("net.minecraft.client.gui.NewChatGui")
  public void transformChatGui(ClassTransformContext context)
      throws NotFoundException, CannotCompileException {
    CtClass transforming = context.getCtClass();
    CtField injectedField =
        this.fieldBuilderFactory.create().target(transforming).inject(super.getClass()).generate();

    CtMethod method = transforming.getDeclaredMethod("printChatMessageWithOptionalDeletion");

    method.insertBefore(
        String.format(
            "{ $1 = %s.handleChatReceive($1, %s.PRE); if ($1 == null) { return; } }",
            injectedField.getName(), Subscribe.Phase.class.getName()));
    method.insertAfter(
        String.format(
            "{ %s.handleChatReceive($1, %s.POST); }",
            injectedField.getName(), Subscribe.Phase.class.getName()));
  }

  public ITextComponent handleChatReceive(ITextComponent component, Subscribe.Phase phase) {
    ChatComponent flintComponent = this.componentMapper.fromMinecraft(component);
    ChatReceiveEvent event = this.receiveFactory.create(flintComponent);
    this.eventBus.fireEvent(event, phase);

    if (phase != Subscribe.Phase.PRE || event.isCancelled()) {
      return null;
    }

    return (ITextComponent) this.componentMapper.toMinecraft(event.getMessage());
  }

  @ClassTransform("net.minecraft.client.entity.player.ClientPlayerEntity")
  public void transformClientPlayerEntity(ClassTransformContext context)
      throws CannotCompileException, NotFoundException {
    CtClass transforming = context.getCtClass();
    CtField injectedField =
        this.fieldBuilderFactory.create().target(transforming).inject(super.getClass()).generate();

    CtMethod method = transforming.getDeclaredMethod("sendChatMessage");

    method.insertBefore(
        String.format(
            "{ $1 = %s.handleChatSend($1, %s.PRE); if ($1 == null) { return; } }",
            injectedField.getName(), Subscribe.Phase.class.getName()));

    method.insertAfter(
        String.format(
            " { if ($1 != null) { %s.handleChatSend($1, %s.POST); } }",
            injectedField.getName(), Subscribe.Phase.class.getName()));
  }

  public String handleChatSend(String message, Subscribe.Phase phase) {
    ChatSendEvent event = this.sendFactory.create(message);
    this.eventBus.fireEvent(event, phase);
    return event.isCancelled() ? null : event.getMessage();
  }
}
