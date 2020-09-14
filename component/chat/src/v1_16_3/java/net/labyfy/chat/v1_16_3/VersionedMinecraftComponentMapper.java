package net.labyfy.chat.v1_16_3;

import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.labyfy.chat.EntitySelector;
import net.labyfy.chat.Keybind;
import net.labyfy.chat.MinecraftComponentMapper;
import net.labyfy.chat.builder.*;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.component.*;
import net.labyfy.chat.component.event.ClickEvent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.component.event.content.HoverContent;
import net.labyfy.chat.component.event.content.HoverText;
import net.labyfy.chat.exception.ComponentDeserializationException;
import net.labyfy.chat.exception.ComponentMappingException;
import net.labyfy.chat.exception.InvalidSelectorException;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.chat.format.ChatFormat;
import net.labyfy.chat.serializer.ComponentSerializer;
import net.labyfy.component.inject.implement.Implement;
import net.minecraft.util.text.*;

import java.util.function.Supplier;

/**
 * 1.16.3 implementation of the labyfy {@link MinecraftComponentMapper}
 */
@Singleton
@Implement(value = MinecraftComponentMapper.class, version = "1.16.3")
public class VersionedMinecraftComponentMapper implements MinecraftComponentMapper {

  static {
    DefaultKeybindComponent.nameFunction =
            keybind -> {
              Supplier<ITextComponent> supplier = () -> ITextComponent.func_244388_a(keybind.getKey());
              KeybindTextComponent.func_240696_a_(s -> supplier);

              return supplier.get().getString();
            };
    ChatColor.setRgbSupport(true);
  }

  private final ComponentSerializer.Factory factory;

  @Inject
  public VersionedMinecraftComponentMapper(ComponentSerializer.Factory factory) {
    this.factory = factory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ChatComponent fromMinecraft(Object handle) throws ComponentMappingException {
    // Only the ITextComponent by Minecraft are allowed
    if (!(handle instanceof ITextComponent)) {
      throw new ComponentDeserializationException(handle.getClass().getCanonicalName()
              + " is not an instance of "
              + ITextComponent.class.getName());
    }

    ITextComponent component = (ITextComponent) handle;
    ChatComponent result = this.createLabyComponent(component);

    if (result == null) {
      return null;
    }

    this.applyStyle(result, component.getStyle());

    for (ITextComponent sibling : component.getSiblings()) {
      result.append(this.fromMinecraft(sibling));
    }

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object toMinecraft(ChatComponent component) {
    ITextComponent result = this.createMinecraftComponent(component);

    if (result == null) {
      return null;
    }

    // Set style in absence
    TextComponentUtils.func_240648_a_(
            (IFormattableTextComponent) result,
            this.buildStyle(component, result.getStyle())
    );

    for (ChatComponent extra : component.extras()) {
      ((IFormattableTextComponent) result).append((ITextComponent) this.toMinecraft(extra));
    }

    return result;
  }

  /**
   * Built a new style for the Minecraft text component.
   *
   * @param component The Labyfy chat component
   * @param style The style of the chat component
   * @return The built style
   */
  private Style buildStyle(ChatComponent component, Style style) {
    style = style.setBold(component.hasFormat(ChatFormat.BOLD));
    style = style.setItalic(component.hasFormat(ChatFormat.ITALIC));
    style = style.func_244282_c(component.hasFormat(ChatFormat.UNDERLINED));

    // Whether the component has strike through format
    if (component.hasFormat(ChatFormat.STRIKETHROUGH)) {
      style = style.applyFormatting(TextFormatting.STRIKETHROUGH);
    }

    // Whether the component has obfuscated format
    if (component.hasFormat(ChatFormat.OBFUSCATED)) {
      style = style.applyFormatting(TextFormatting.OBFUSCATED);
    }

    // Whether the color for the component is default
    if (component.color().isDefaultColor()) {
      style = style.setColor(Color.func_240744_a_(TextFormatting.valueOf(component.color().getName())));
    } else {
      style = style.setColor(Color.func_240743_a_(component.color().getRgb()));
    }

    // Whether the component has a click event
    if (component.clickEvent() != null) {
      try {
        style = style.setClickEvent(
                new net.minecraft.util.text.event.ClickEvent(
                        net.minecraft.util.text.event.ClickEvent.Action.valueOf(
                                component.clickEvent().getAction().name()),
                        component.clickEvent().getValue()));
      } catch (IllegalArgumentException ignored) {
      }
    }

    // Whether the component has a hover event
    if (component.hoverEvent() != null && component.hoverEvent().getContents().length > 0) {
      net.minecraft.util.text.event.HoverEvent.Action action = null;
      HoverContent content = component.hoverEvent().getContents()[0];

      try {
        action = net.minecraft.util.text.event.HoverEvent.Action.getValueByCanonicalName(content.getAction().name());
      } catch (IllegalArgumentException ignored) {
      }

      if (action != null) {
        ChatComponent value = content instanceof HoverText ?
                ((HoverText) content).getText() :
                new DefaultTextComponentBuilder().text(this.factory.gson().getGson().toJson(content)).build();

        style = style.setHoverEvent(new net.minecraft.util.text.event.HoverEvent(action, this.toMinecraft(value)));
      }
    }

    style = style.setInsertion(component.insertion());

    return style;

  }

  /**
   * Creates a Minecraft text component.
   *
   * @param component The Labyfy {@link ChatComponent} to be convert
   * @return The created Minecraft text component
   */
  private ITextComponent createMinecraftComponent(ChatComponent component) {
    if (component instanceof KeybindComponent) {

      Keybind keybind = ((KeybindComponent) component).keybind();
      if (keybind != null) {
        return new KeybindTextComponent(keybind.getKey());
      }

    } else if (component instanceof ScoreComponent) {

      String name = ((ScoreComponent) component).name();
      String objective = ((ScoreComponent) component).objective();

      if (name != null && objective != null) {
        return new ScoreTextComponent(name, objective);
      }

    } else if (component instanceof SelectorComponent) {

      EntitySelector selector = ((SelectorComponent) component).selector();

      if (selector != null) {
        return new SelectorTextComponent(component.getUnformattedText());
      }

    } else if (component instanceof net.labyfy.chat.component.TextComponent) {

      String text = ((TextComponent) component).text();
      if (text != null) {
        return new StringTextComponent(text);
      }

    } else if (component instanceof TranslationComponent) {

      String key = ((TranslationComponent) component).translationKey();
      ChatComponent[] arguments = ((TranslationComponent) component).arguments();

      if (key != null) {
        Object[] handleArguments = new Object[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
          ChatComponent argument = arguments[i];
          handleArguments[i] = argument != null ? this.toMinecraft(argument) : "null";
        }

        return new TranslationTextComponent(key, handleArguments);
      }
    }

    return null;
  }

  /**
   * Creates a Labyfy chat component.
   *
   * @param component The Minecraft {@link ITextComponent} to be convert
   * @return The created Labyfy chat component
   */
  private ChatComponent createLabyComponent(ITextComponent component) {
    if (component instanceof KeybindTextComponent) {
      return new DefaultKeybindComponentBuilder()
              .keybind(Keybind.getByKey(((KeybindTextComponent) component).getKeybind()))
              .build();
    } else if (component instanceof ScoreTextComponent) {
      return new DefaultScoreComponentBuilder()
              .name(((ScoreTextComponent) component).getName())
              .objective(((ScoreTextComponent) component).getObjective())
              .build();
    } else if (component instanceof SelectorTextComponent) {
      try {
        return new DefaultSelectorComponentBuilder()
                .parse(((SelectorTextComponent) component).getSelector())
                .build();
      } catch (InvalidSelectorException ignored) {
      }
    } else if (component instanceof StringTextComponent) {
      return new DefaultTextComponentBuilder()
              .text(((StringTextComponent) component).getText())
              .build();
    } else if (component instanceof TranslationTextComponent) {
      Object[] arguments = ((TranslationTextComponent) component).getFormatArgs();
      ChatComponent[] mappedArguments = new ChatComponent[arguments.length];
      for (int i = 0; i < arguments.length; i++) {
        Object argument = arguments[i];

        if (argument instanceof ITextComponent) {
          mappedArguments[i] = this.fromMinecraft(argument);
        }

        if (mappedArguments[i] == null) {
          mappedArguments[i] =
                  new DefaultTextComponentBuilder().text(String.valueOf(argument)).build();
        }
      }

      return new DefaultTranslationComponentBuilder()
              .translationKey(((TranslationTextComponent) component).getKey())
              .arguments(mappedArguments)
              .build();
    }

    return null;
  }

  /**
   * Apply the style for the Labyfy {@link ChatComponent}.
   *
   * @param result The Labyfy chat component whose style is to be set
   * @param style The style for the chat component
   */
  private void applyStyle(ChatComponent result, Style style) {
    if (style.getBold()) {
      result.toggleFormat(ChatFormat.BOLD, true);
    }

    if (style.getItalic()) {
      result.toggleFormat(ChatFormat.ITALIC, true);
    }

    if (style.getObfuscated()) {
      result.toggleFormat(ChatFormat.OBFUSCATED, true);
    }

    if (style.getStrikethrough()) {
      result.toggleFormat(ChatFormat.STRIKETHROUGH, true);
    }

    if (style.getUnderlined()) {
      result.toggleFormat(ChatFormat.UNDERLINED, true);
    }

    if (style.getColor() != null) {
      ChatColor color = ChatColor.parse(style.getColor().func_240747_b_());
      if (color != null) {
        result.color(color);
      }
    }


    if (style.getClickEvent() != null) {
      result.clickEvent(
              ClickEvent.of(
                      ClickEvent.Action.valueOf(style.getClickEvent().getAction().getCanonicalName()),
                      style.getClickEvent().getValue()
              )
      );
    }

    if (style.getHoverEvent() != null) {
      HoverEvent.Action action = HoverEvent.Action.valueOf(style.getHoverEvent().getAction().getCanonicalName());
      ITextComponent component = style.getHoverEvent().getParameter(net.minecraft.util.text.event.HoverEvent.Action.SHOW_TEXT);

      HoverContent content = action == HoverEvent.Action.SHOW_TEXT || action == HoverEvent.Action.SHOW_ACHIEVEMENT ?
              new HoverText(this.fromMinecraft(component)) :
              this.factory.gson()
                      .getGson()
                      .fromJson(
                              JsonParser.parseString(component.getUnformattedComponentText()),
                              action.getContentClass()
                      );

      if (content != null) {
        result.hoverEvent(HoverEvent.of(content));
      }
    }

    result.insertion(style.getInsertion());
  }
}
