/*
 * Copyright (C) 2000 - 2018 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "https://www.silverpeas.org/legal/floss_exception.html"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.silverpeas.core.admin.component.model;

import org.silverpeas.core.SilverpeasRuntimeException;
import org.silverpeas.core.ui.DisplayI18NHelper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupOfParametersType", propOrder = { "label", "description", "help", "order", "parameters" })
public class GroupOfParameters implements Cloneable {

  @XmlElement(required = true)
  @XmlJavaTypeAdapter(MultilangHashMapAdapter.class)
  protected Map<String, String> label;

  @XmlElement
  @XmlJavaTypeAdapter(MultilangHashMapAdapter.class)
  protected Map<String, String> description;

  @XmlElement
  @XmlJavaTypeAdapter(MultilangHashMapAdapter.class)
  protected Map<String, String> help;

  @XmlElement
  protected int order;

  @XmlElementWrapper(name = "parameters")
  @XmlElement(name = "parameter")
  protected List<Parameter> parameters;

  /**
   * Gets the value of the label property.
   * @return possible object is {@link Multilang }
   */
  public Map<String, String> getLabel() {
    if (label == null) {
      label = new HashMap<>();
    }
    return label;
  }

  public String getLabel(String lang) {
    if (getLabel().containsKey(lang)) {
      return getLabel().get(lang);
    }
    return getLabel().get(DisplayI18NHelper.getDefaultLanguage());
  }

  /**
   * Sets the value of the label property.
   * @param value allowed object is {@link Multilang }
   */
  public void setLabel(Map<String, String> value) {
    this.label = value;
  }

  /**
   * Gets the value of the description property.
   * @return possible object is {@link Multilang }
   */
  public Map<String, String> getDescription() {
    if (description == null) {
      description = new HashMap<>();
    }
    return description;
  }

  public String getDescription(String lang) {
    if (getDescription().containsKey(lang)) {
      return getDescription().get(lang);
    }
    return getDescription().get(DisplayI18NHelper.getDefaultLanguage());
  }

  /**
   * Sets the value of the description property.
   * @param value allowed object is {@link Multilang }
   */
  public void setDescription(Map<String, String> value) {
    this.description = value;
  }

  /**
   * Gets the value of the help property.
   * @return possible object is {@link Multilang }
   */
  public Map<String, String> getHelp() {
    if (help == null) {
      help = new HashMap<>();
    }
    return help;
  }

  /**
   * Sets the value of the help property.
   * @param value allowed object is {@link Multilang }
   */
  public void setHelp(Map<String, String> value) {
    this.help = value;
  }

  /**
   * Gets the value of the order property.
   */
  public int getOrder() {
    return order;
  }

  /**
   * Sets the value of the order property.
   */
  public void setOrder(int value) {
    this.order = value;
  }

  /**
   * Gets the value of the parameters property.
   * @return list of {@link Parameter}
   */
  public List<Parameter> getParameters() {
    if (parameters == null) {
      parameters = new ArrayList<Parameter>();
    }
    return parameters;
  }

  /**
   * Sets the value of the parameters property.
   * @param parameters list of {@link Parameter}
   */
  public void setParameters(List<Parameter> parameters) {
    this.parameters = parameters;
  }

  public ParameterList getParameterList() {
    return new ParameterList(getParameters());
  }

  public boolean isVisible() {
    return getParameterList().isVisible();
  }

  public LocalizedGroupOfParameters localize(String lang) {
    return new LocalizedGroupOfParameters(this, lang);
  }

  public GroupOfParameters clone() {
    GroupOfParameters clone;
    try {
      clone = (GroupOfParameters) super.clone();
    } catch (CloneNotSupportedException e) {
      throw new SilverpeasRuntimeException(e);
    }
    clone.setLabel(new HashMap<>(getLabel()));
    clone.setDescription(new HashMap<>(getDescription()));
    clone.setHelp(new HashMap<>(getHelp()));
    clone.setOrder(getOrder());
    clone.setParameters(getParameterList().clone());
    return clone;
  }

}