package org.restlet.example.book.restlet.ch08.sec5.common;

import org.restlet.resource.Get;
import org.restlet.resource.Put;

/**
 * Annotated mail resource interface
 */
public interface MailResource {

    @Get
    public MailRepresentation retrieve();

    @Put
    public void store(MailRepresentation mail);

}
