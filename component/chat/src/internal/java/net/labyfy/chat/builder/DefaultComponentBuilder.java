package net.labyfy.chat.builder;

import net.labyfy.chat.component.ChatComponent;
import net.labyfy.chat.component.DefaultTextComponent;
import net.labyfy.chat.component.TextComponent;
import net.labyfy.chat.component.event.ClickEvent;
import net.labyfy.chat.component.event.HoverEvent;
import net.labyfy.chat.format.ChatColor;
import net.labyfy.chat.format.ChatFormat;

import java.util.function.Supplier;

public abstract class DefaultComponentBuilder<B extends ComponentBuilder<B>, C extends ChatComponent>
    implements ComponentBuilder<B> {

  private final B builder;
  private final Supplier<C> componentSupplier;
  protected C currentComponent;
  private TextComponent baseComponent;

  @SuppressWarnings("unchecked")
  public DefaultComponentBuilder(Supplier<C> componentSupplier) {
    this.componentSupplier = componentSupplier;
    this.currentComponent = componentSupplier.get();
    this.builder = (B) this;
  }

  @Override
  public ChatComponent build() {
    if (this.baseComponent == null || this.baseComponent.extras().length == 0) {
      return this.currentComponent;
    }
    if (!this.currentComponent.isEmpty()) {
      this.baseComponent.append(this.currentComponent);
    }
    return this.baseComponent;
  }

  @Override
  public B format(ChatFormat format) {
    this.currentComponent.toggleFormat(format, true);
    return this.builder;
  }

  @Override
  public ChatFormat[] enabledFormats() {
    return this.currentComponent.formats();
  }

  @Override
  public B color(ChatColor color) {
    this.currentComponent.color(color);
    return this.builder;
  }

  @Override
  public ChatColor color() {
    return this.currentComponent.color();
  }

  @Override
  public B clickEvent(ClickEvent event) {
    this.currentComponent.clickEvent(event);
    return this.builder;
  }

  @Override
  public ClickEvent clickEvent() {
    return this.currentComponent.clickEvent();
  }

  @Override
  public B hoverEvent(HoverEvent event) {
    this.currentComponent.hoverEvent(event);
    return this.builder;
  }

  @Override
  public HoverEvent hoverEvent() {
    return this.currentComponent.hoverEvent();
  }

  @Override
  public B append(ChatComponent component) {
    this.currentComponent.append(component);
    return this.builder;
  }

  @Override
  public B nextComponent() {
    if (this.currentComponent.isEmpty()) {
      return this.builder;
    }

    if (this.baseComponent == null) {
      this.baseComponent = new DefaultTextComponent();
      this.baseComponent.text("");
    }
    this.baseComponent.append(this.currentComponent);
    this.currentComponent = this.componentSupplier.get();
    return this.builder;
  }
}