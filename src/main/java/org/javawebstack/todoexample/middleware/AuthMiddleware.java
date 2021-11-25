package org.javawebstack.todoexample.middleware;

import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestHandler;

public class AuthMiddleware implements RequestHandler {

    public Object handle(Exchange exchange) {
        if(exchange.attrib("user") == null) {
            exchange.status(401);
            return new AbstractObject()
                    .set("success", false)
                    .set("error", "Authentication required");
        }
        return null;
    }

}
