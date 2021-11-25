package org.javawebstack.todoexample.middleware;

import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestHandler;

public class ModelBindCheckMiddleware implements RequestHandler {

    public Object handle(Exchange exchange) {
        for(String key : exchange.getPathVariables().keySet()) {
            if(exchange.getPathVariables().get(key) == null) {
                exchange.status(404);
                return new AbstractObject().set("success", false).set("error", "Not found");
            }
        }
        return null;
    }

}
