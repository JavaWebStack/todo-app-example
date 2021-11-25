package org.javawebstack.todoexample.controller;

import com.auth0.jwt.JWT;
import org.javawebstack.abstractdata.AbstractObject;
import org.javawebstack.httpserver.Exchange;
import org.javawebstack.httpserver.router.annotation.PathPrefix;
import org.javawebstack.httpserver.router.annotation.verbs.Post;
import org.javawebstack.orm.Repo;
import org.javawebstack.todoexample.TodoExample;
import org.javawebstack.todoexample.model.User;
import org.javawebstack.todoexample.request.LoginRequest;
import org.javawebstack.todoexample.request.RegisterRequest;

import java.time.Instant;
import java.util.Date;

@PathPrefix("api")
public class AuthController extends Controller {

    @Post("login")
    public AbstractObject login(Exchange exchange, LoginRequest request) {
        User user = Repo.get(User.class).where("name", request.getUsername()).first();
        if(user == null || !user.checkPassword(request.getPassword())) {
            exchange.status(400);
            return new AbstractObject().set("success", false).set("error", "Wrong credentials");
        }
        return new AbstractObject()
                .set("success", true)
                .set("token", JWT.create()
                        .withSubject(user.getName())
                        .withExpiresAt(Date.from(Instant.now().plusSeconds(TodoExample.INSTANCE.getConfig().getInt("jwt.lifetime", 3600))))
                        .sign(TodoExample.INSTANCE.getJwtAlgorithm())
                );
    }

    @Post("register")
    public AbstractObject register(Exchange exchange, RegisterRequest request) {
        if(Repo.get(User.class).where("name", request.getUsername()).hasRecords()) {
            exchange.status(400);
            return new AbstractObject().set("success", false).set("error", "Username already exists");
        }
        User user = new User()
                .setName(request.getUsername())
                .setPassword(request.getPassword());
        user.save();
        return new AbstractObject().set("success", true);
    }

}
