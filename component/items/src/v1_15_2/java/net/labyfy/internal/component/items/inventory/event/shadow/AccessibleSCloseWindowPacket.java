package net.labyfy.internal.component.items.inventory.event.shadow;

import net.labyfy.component.transform.shadow.FieldGetter;
import net.labyfy.component.transform.shadow.Shadow;

@Shadow("net.minecraft.network.play.server.SCloseWindowPacket")
public interface AccessibleSCloseWindowPacket {

  @FieldGetter("windowId")
  int getWindowId();

}
