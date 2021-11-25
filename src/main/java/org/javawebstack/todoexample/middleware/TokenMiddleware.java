package org.javawebstack.todoexample.middleware;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.handler.RequestInterceptor;
import org.javawebstack.orm.Repo;
import org.javawebstack.todoexample.TodoExample;
import org.javawebstack.todoexample.model.User;

public class TokenMiddleware implements RequestInterceptor {

    public boolean intercept(Exchange exchange) {
        String bearer = exchange.bearerAuth();
        if(bearer != null) {
            try {
                DecodedJWT jwt = JWT.require(TodoExample.INSTANCE.getJwtAlgorithm()).acceptExpiresAt(System.currentTimeMillis()).build().verify(bearer);
                User user = Repo.get(User.class).where("name", jwt.getSubject()).first();
                if(user != null)
                    exchange.attrib("user", user);
            } catch (JWTVerificationException ex) {}
        }
        return false;
    }

}
