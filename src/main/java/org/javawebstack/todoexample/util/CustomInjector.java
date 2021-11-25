package org.javawebstack.todoexample.util;

import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.RouteAutoInjector;
import org.javawebstack.todoexample.TodoExample;
import org.javawebstack.todoexample.model.User;

import java.util.Map;

public class CustomInjector implements RouteAutoInjector {

    public Object getValue(Exchange exchange, Map<String, Object> extraArgs, Class<?> type) {
        if(User.class.equals(type))
            return exchange.attrib("user");
        if(type.getPackage().getName().startsWith(TodoExample.class.getPackage().getName() + ".request"))
            return exchange.body(type);
        return null;
    }

}
