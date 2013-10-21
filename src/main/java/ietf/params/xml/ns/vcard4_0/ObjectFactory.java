package ietf.params.xml.ns.vcard4_0;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

  private final static QName _ORG_QNAME = new QName("urn:ietf:params:xml:ns:vcard-4.0", "org");

  public OrganizationType creatOrganizationType() {
    return new OrganizationType();
  }

  public NameType createNameType() {
    return new NameType();
  }

  public FormattedNameType createFormattedNameType() {
    return new FormattedNameType();
  }

  @XmlElementDecl(namespace = "urn:ietf:params:xml:ns:vcard-4.0", name = "org")
  public JAXBElement<OrganizationType> createOganization(OrganizationType value) {
    return new JAXBElement<OrganizationType>(_ORG_QNAME, OrganizationType.class, null, value);
  }

}
