package org.restlet.example.book.restlet.ch05.sec2.sub3;

import java.io.IOException;

import org.restlet.data.Reference;
import org.restlet.ext.xml.SaxRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Resource corresponding to a mail received or sent with the parent mail
 * account.
 */
public class MailServerResource extends ServerResource {

    @Override
    protected Representation get() throws ResourceException {
        SaxRepresentation result;

        // Create a new SAX representation
        result = new SaxRepresentation() {

            public void write(org.restlet.ext.xml.XmlWriter writer)
                    throws IOException {
                try {
                    // Start document
                    writer.startDocument();

                    // Append the root node
                    writer.startElement("mail");

                    // Append the child nodes and set their text content
                    writer.startElement("status");
                    writer.characters("received");
                    writer.endElement("status");

                    writer.startElement("subject");
                    writer.characters("Message to self");
                    writer.endElement("subject");

                    writer.startElement("content");
                    writer.characters("Doh!");
                    writer.endElement("content");

                    writer.startElement("accountRef");
                    writer.characters(new Reference(getReference(), "..")
                            .getTargetRef().toString());
                    writer.endElement("accountRef");

                    // End the root node
                    writer.endElement("mail");

                    // End the document
                    writer.endDocument();
                } catch (SAXException e) {
                    throw new IOException(e.getMessage());
                }
            };
        };

        return result;
    }

    @Override
    protected Representation put(Representation representation)
            throws ResourceException {
        // Wraps the XML representation in a SAX representation
        SaxRepresentation mailRep = new SaxRepresentation(representation);

        try {
            mailRep.parse(new DefaultHandler() {

                @Override
                public void startElement(String uri, String localName,
                        String qName, Attributes attributes)
                        throws SAXException {
                    // Output the XML element values
                    if ("status".equals(localName)) {
                        System.out.print("Status: ");
                    } else if ("subject".equals(localName)) {
                        System.out.print("Subject: ");
                    } else if ("content".equals(localName)) {
                        System.out.print("Content: ");
                    } else if ("accountRef".equals(localName)) {
                        System.out.print("Account URI: ");
                    }
                }

                @Override
                public void characters(char[] ch, int start, int length)
                        throws SAXException {
                    // Output the XML element values
                    System.out.print(new String(ch, start, length));
                }

                @Override
                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                    // Output a new line
                    System.out.println();
                }

            });
        } catch (IOException e) {
            throw new ResourceException(e);
        }

        return null;
    }
}
