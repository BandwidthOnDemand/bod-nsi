package ietf.params.xml.ns.vcard4_0;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "n")
public class NameType {

  private String surname;
  private String given;

  public NameType withSurname(String surname) {
    this.surname = surname;
    return this;
  }

  public NameType withGiven(String given) {
    this.given = given;
    return this;
  }

  public String getSurname() {
    return surname;
  }

  public String getGiven() {
    return given;
  }
}
