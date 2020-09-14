package net.labyfy.internal.component.player.v1_16_3.overlay;

import com.google.inject.Inject;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.component.ChatComponent;
import net.labyfy.component.inject.implement.Implement;
import net.labyfy.component.mappings.ClassMappingProvider;
import net.labyfy.component.player.overlay.TabOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;

/**
 * 1.16.3 implementation of {@link TabOverlay}
 */
@Implement(value = TabOverlay.class, version = "1.16.3")
public class VersionedTabOverlay implements TabOverlay {

  private final ClassMappingProvider classMappingProvider;
  private final MinecraftComponentMapper minecraftComponentMapper;

  @Inject
  public VersionedTabOverlay(
          ClassMappingProvider classMappingProvider,
          MinecraftComponentMapper minecraftComponentMapper
  ) {
    this.classMappingProvider = classMappingProvider;
    this.minecraftComponentMapper = minecraftComponentMapper;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getHeader() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    return this.minecraftComponentMapper.fromMinecraft(
            this.classMappingProvider
                    .get("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
                    .getField("header")
                    .getValue(Minecraft.getInstance().ingameGUI.getTabList())
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateHeader(ChatComponent header) {
    Minecraft.getInstance().ingameGUI.getTabList().setHeader(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(header)
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent getFooter() throws IllegalAccessException, NoSuchFieldException, ClassNotFoundException {
    return this.minecraftComponentMapper.fromMinecraft(
            this.classMappingProvider
                    .get("net.minecraft.client.gui.overlay.PlayerTabOverlayGui")
                    .getField("footer")
                    .getValue(Minecraft.getInstance().ingameGUI.getTabList())
    );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateFooter(ChatComponent footer) {
    Minecraft.getInstance().ingameGUI.getTabList().setFooter(
            (ITextComponent) this.minecraftComponentMapper.toMinecraft(footer)
    );
  }
}
