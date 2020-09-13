package net.labyfy.chat.v1_16_3;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.controller.ChatController;
import net.labyfy.chat.controller.DefaultChatController;
import net.labyfy.chat.controller.DefaultFilterableChatMessage;
import net.labyfy.chat.controller.filter.FilterableChatMessage;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 1.16.3 implementation of the labyfy {@link ChatController}
 */
@Implement(value = ChatController.class, version = "1.16.3")
public class VersionedDefaultChatController extends DefaultChatController {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean dispatchChatInput(String message) {
    if(message.length() >= this.getChatInputLimit()) {
      // The message is longer than the maximum allowed, servers would kick the player when sending this
      message = message.substring(0 , this.getChatInputLimit());
    }
    Minecraft.getInstance().player.sendChatMessage(message);
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChatInputLimit() {
    return 256;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean displayChatMessage(ChatComponent component, UUID senderUniqueId) {
    if(Minecraft.getInstance().ingameGUI == null) {
      return false;
    }

    FilterableChatMessage message = new DefaultFilterableChatMessage(component, this.getMainChat(), senderUniqueId);
    return super.processMessage(message);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getInputHistory() {
    return Minecraft.getInstance().ingameGUI == null ?
            new ArrayList<>() :
            Minecraft.getInstance().ingameGUI.getChatGUI().getSentMessages();
  }
}
