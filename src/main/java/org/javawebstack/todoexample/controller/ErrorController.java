package org.javawebstack.todoexample.controller;

import org.javawebstack.abstractdata.AbstractArray;
import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.ExceptionHandler;
import org.javawebstack.validator.ValidationException;
import org.javawebstack.validator.ValidationResult;

public class ErrorController implements ExceptionHandler {

    public Object handle(Exchange exchange, Throwable throwable) {
        if(throwable instanceof ValidationException) {
            ValidationResult result = ((ValidationException) throwable).getResult();
            AbstractObject errors = new AbstractObject();
            result.getErrorMap().forEach((field, errorList) -> {
                AbstractArray messages = new AbstractArray();
                errorList.forEach(err -> messages.add(err.getMessage()));
                errors.set(field, messages);
            });
            exchange.status(400);
            return new AbstractObject().set("success", false).set("error", "Invalid request").set("fields", errors);
        }
        throwable.printStackTrace();
        exchange.status(500);
        return new AbstractObject().set("success", false).set("error", "Server error");
    }

}
