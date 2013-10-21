package ietf.params.xml.ns.vcard4_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

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