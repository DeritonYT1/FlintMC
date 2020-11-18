package net.flintmc.render.minecraft.v1_15_2.text;

import net.flintmc.transform.shadow.FieldGetter;
import net.flintmc.transform.shadow.Shadow;
import net.minecraft.client.gui.fonts.Font;

@Shadow("net.minecraft.client.gui.FontRenderer")
public interface ShadowFontRenderer {

  @FieldGetter("font")
  Font getFont();
}
