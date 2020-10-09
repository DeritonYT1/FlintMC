package net.labyfy.internal.component.server.event.shadow;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;
import net.minecraft.util.text.ITextComponent;

/**
 * Shadow implementation of the {@link net.minecraft.client.gui.screen.DisconnectedScreen} in minecraft with a public
 * getter to get the message component.
 */
@Shadow("net.minecraft.client.gui.screen.DisconnectedScreen")
public interface AccessibleDisconnectedScreen {

  /**
   * Retrieves the message that will be displayed in this screen.
   *
   * @return The non-null message of this screen
   */
  @FieldGetter("message")
  ITextComponent getMessage();

}
