package net.flintmc.mcapi.internal.chat.event;

import net.flintmc.framework.inject.assisted.Assisted;
import net.flintmc.framework.inject.assisted.AssistedInject;
import net.flintmc.framework.inject.implement.Implement;
import net.flintmc.mcapi.chat.event.ChatSendEvent;

@Implement(ChatSendEvent.class)
public class DefaultChatSendEvent extends DefaultChatMessageEvent implements ChatSendEvent {

  private String message;
  private boolean cancelled;

  @AssistedInject
  private DefaultChatSendEvent(@Assisted("message") String message) {
    super(Type.SEND);
    this.message = message;
  }

  /** {@inheritDoc} */
  @Override
  public String getMessage() {
    return this.message;
  }

  /** {@inheritDoc} */
  @Override
  public void setMessage(String message) {
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