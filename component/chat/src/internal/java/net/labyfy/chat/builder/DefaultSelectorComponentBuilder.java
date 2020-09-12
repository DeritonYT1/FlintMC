package net.labyfy.chat.builder;

import net.labyfy.chat.EntitySelector;
import net.labyfy.chat.component.DefaultSelectorComponent;
import net.labyfy.chat.component.SelectorComponent;
import net.labyfy.chat.exception.InvalidSelectorException;
import net.labyfy.component.inject.implement.Implement;

import java.util.Collection;

@Implement(value = SelectorComponentBuilder.class)
public class DefaultSelectorComponentBuilder
    extends DefaultComponentBuilder<SelectorComponentBuilder, SelectorComponent>
    implements SelectorComponentBuilder {

  public DefaultSelectorComponentBuilder() {
    super(DefaultSelectorComponent::new);
  }

  @Override
  public SelectorComponentBuilder selector(EntitySelector selector) {
    super.currentComponent.selector(selector);
    return this;
  }

  @Override
  public EntitySelector selector() {
    return super.currentComponent.selector();
  }

  @Override
  public SelectorComponentBuilder option(String option, String value) {
    super.currentComponent.selectorOption(option, value);
    return this;
  }

  @Override
  public Collection<String> option(String option) {
    return super.currentComponent.selectorOption(option);
  }

  @Override
  public SelectorComponentBuilder parse(String rawSelector) {
    rawSelector = rawSelector.replace(" ", "");

    if (rawSelector.length() == 0) {
      throw new InvalidSelectorException("Selector has to contain at least one character");
    }
    EntitySelector selector = EntitySelector.getByShortcut(rawSelector.charAt(0));
    if (selector == null) {
      throw new InvalidSelectorException("Unknown selector '" + rawSelector.charAt(0) + "'");
    }

    if (rawSelector.length() > 1
        && rawSelector.charAt(1) == '['
        && rawSelector.charAt(rawSelector.length() - 1) == ']') {
      String rawOptions = rawSelector.substring(2, rawSelector.length() - 1);
      String[] splitOptions = rawOptions.split(",");

      for (String option : splitOptions) {
        String[] keyValuePair = option.split("=", 2);
        if (keyValuePair.length == 2) {
          this.option(keyValuePair[0], keyValuePair[1]);
        }
      }
    }

    this.selector(selector);

    return this;
  }
}
