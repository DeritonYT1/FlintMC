package net.flintmc.mcapi.internal.chat.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.component.ChatComponent;
import net.flintmc.mcapi.chat.event.ChatReceiveEvent;

@Implement(ChatReceiveEvent.class)
public class DefaultChatReceiveEvent extends DefaultChatMessageEvent implements ChatReceiveEvent {

  private ChatComponent message;
  private boolean cancelled;

  @AssistedInject
  private DefaultChatReceiveEvent(@Assisted("message") ChatComponent message) {
    super(Type.RECEIVE);
    this.message = message;
  }

  /** {@inheritDoc} */
  @Override
  public ChatComponent getMessage() {
    return this.message;
  }

  /** {@inheritDoc} */
  @Override
  public void setMessage(ChatComponent message) {
    this.message = message;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  /** {@inheritDoc} */
  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }
}