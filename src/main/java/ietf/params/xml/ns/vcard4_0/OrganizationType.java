package ietf.params.xml.ns.vcard4_0;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "org")
public class OrganizationType {

  private String text;

  public OrganizationType() {
  }

  public OrganizationType withText(String text) {
    this.text = text;
    return this;
  }

  public String getText() {
    return text;
  }
}
