package org.javawebstack.todoexample.controller;

import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestHandler;

public class NotFoundController implements RequestHandler {

    public Object handle(Exchange exchange) {
        return new AbstractObject().set("success", false).set("error", "Path not found");
    }

}
