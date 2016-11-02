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
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.w3c.dom.Document;

public final class Validation {
    private Validation() {}

    public static void validate(Document document) throws SAXException, SAXParseException, IOException {
        StreamSource[] schemas = asList(
          "wsdl/soap/soap-envelope-1.1.xsd",
          "wsdl/2.0/ogf_nsi_framework_headers_v2_0.xsd",
          "wsdl/2.0/ogf_nsi_connection_types_v2_0.xsd",
          "wsdl/2.0/saml-schema-assertion-2.0.xsd",
          "wsdl/2.0/gnsbod.xsd"
        ).stream()
            .map(InternalUtils::classpathResource)
            .map(StreamSource::new)
            .collect(toList())
            .toArray(new StreamSource[5]);

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        Schema schema = factory.newSchema(schemas);
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new FailingErrorHandler());

        validator.validate(new DOMSource(document));
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

        private static class NsiValidationException extends RuntimeException {
            public NsiValidationException(Throwable cause) {
                super("NSI message validation error", cause);
            }
        }
    }
}
