/**
 * Copyright (c) 2012, 2013, 2014, 2015, 2016 SURFnet BV
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *     disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *     disclaimer in the documentation and/or other materials provided with the distribution.
 *   * Neither the name of the SURFnet BV nor the names of its contributors may be used to endorse or promote products
 *     derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package nl.surfnet.bod.nsi;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public final class Validation {
  public static final List<String> NSI_SCHEMAS = asList(
      "wsdl/soap/soap-envelope-1.1.xsd",
      "wsdl/2.0/ogf_nsi_framework_headers_v2_0.xsd",
      "wsdl/2.0/ogf_nsi_connection_types_v2_0.xsd",
      "wsdl/2.0/saml-schema-assertion-2.0.xsd",
      "wsdl/2.0/gnsbod.xsd",
      "wsdl/2.0/ogf_nsi_path_trace_2015_04_30.xsd");

  public static final List<String> PCE_SCHEMAS = asList("xsd/pce-messages.xsd");

  private final Schema schema;

  public Validation(List<String> schemaSources) throws SAXException {
    StreamSource[] schemas = schemaSources
        .stream()
        .map(InternalUtils::classpathResource)
        .map(StreamSource::new)
        .toArray(StreamSource[]::new);

    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    this.schema = factory.newSchema(schemas);
  }

  public Schema getSchema() {
    return schema;
  }

  public void validate(Document document) throws NsiValidationException {
    Validator validator = schema.newValidator();
    validator.setErrorHandler(new FailingErrorHandler());

    try {
      validator.validate(new DOMSource(document));
    } catch (SAXException | IOException e) {
      throw new NsiValidationException(e);
    }
  }

  private static class FailingErrorHandler implements ErrorHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Validation.class);

    @Override
    public void error(SAXParseException ex) {
      throw new NsiValidationException(ex);
    }

    @Override
    public void fatalError(SAXParseException ex) {
      throw new NsiValidationException(ex);
    }

    public void warning(SAXParseException ex) {
      LOGGER.warn("Warning while validating NSI message", ex);
    }
  }

  public static class NsiValidationException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NsiValidationException(Throwable cause) {
      super("NSI message validation error", cause);
    }
  }
}
