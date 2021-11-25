package org.javawebstack.todoexample;

import com.auth0.jwt.algorithms.Algorithm;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.javawebstack.httpserver.HTTPServer;
import org.javawebstack.orm.ORM;
import org.javawebstack.orm.ORMConfig;
import org.javawebstack.orm.exception.ORMConfigurationException;
import org.javawebstack.orm.wrapper.MySQL;
import org.javawebstack.orm.wrapper.SQL;
import org.javawebstack.todoexample.controller.Controller;
import org.javawebstack.todoexample.controller.ErrorController;
import org.javawebstack.todoexample.controller.NotFoundController;
import org.javawebstack.todoexample.middleware.AuthMiddleware;
import org.javawebstack.todoexample.middleware.ModelBindCheckMiddleware;
import org.javawebstack.todoexample.middleware.TokenMiddleware;
import org.javawebstack.todoexample.model.Model;
import org.javawebstack.todoexample.model.Task;
import org.javawebstack.todoexample.model.User;
import org.javawebstack.todoexample.observer.UserObserver;
import org.javawebstack.todoexample.util.CustomInjector;
import org.javawebstack.todoexample.util.TaskAccessible;
import org.javawebstack.webutils.config.Config;
import org.javawebstack.webutils.config.EnvFile;
import org.javawebstack.webutils.middleware.CORSPolicy;
import org.javawebstack.webutils.middleware.SerializedResponseTransformer;
import org.javawebstack.webutils.modelbind.ModelBindParamTransformer;
import org.javawebstack.webutils.util.RandomUtil;

import java.io.File;
import java.util.HashMap;

@Getter
@FieldDefaults(makeFinal = true)
public class TodoExample {

    public static TodoExample INSTANCE;

    public static void main(String[] args) throws ORMConfigurationException {
        INSTANCE = new TodoExample(); // Create a new instance of the app
        INSTANCE.start(); // Start the server
    }

    Config config;
    HTTPServer server;
    Algorithm jwtAlgorithm;

    private TodoExample() throws ORMConfigurationException {
        config = new Config().add(new EnvFile(new File(".env")).withVariables(), new HashMap<String, String>() {{
            put("HTTP_PORT", "http.port");
            put("MYSQL_HOST", "mysql.host");
            put("MYSQL_PORT", "mysql.port");
            put("MYSQL_DATABASE", "mysql.database");
            put("MYSQL_USER", "mysql.user");
            put("MYSQL_PASSWORD", "mysql.password");
            put("JWT_SECRET", "jwt.secret");
            put("JWT_LIFETIME", "jwt.lifetime");
        }}); // This will create a config from an .env file with the given mapping
        jwtAlgorithm = Algorithm.HMAC256(config.get("jwt.secret", RandomUtil.string(20)));
        SQL sql = new MySQL(
                config.get("mysql.host", "127.0.0.1"),
                config.getInt("mysql.port", 3306),
                config.get("mysql.database", "todo"),
                config.get("mysql.user", "todo"),
                config.get("mysql.password", "")
        ); // Configuration for the sql connection
        ORM.register(Model.class.getPackage(), sql, new ORMConfig()
                .setDefaultSize(256)
        ); // Registers all database models by searching the given package
        ORM.autoMigrate(); // Automatically migrates all required tables
        ORM.repo(Task.class).setAccessible(new TaskAccessible()); // Add access control to the Task model
        ORM.repo(User.class).observe(new UserObserver()); // Adds an observer to the User model
        server = new HTTPServer()
                .port(config.getInt("http.port", 80)) // Set the http port from the config or else 80
                .routeParamTransformer(new ModelBindParamTransformer().setAccessorAttribName("user")) // Enable model binding in route parameters
                .beforeInterceptor(new CORSPolicy("*")) // Automatically answer OPTIONS requests with the given cors policy
                .routeAutoInjector(new CustomInjector()) // Register a custom route injector to inject the current user and requests automatically
                .responseTransformer(new SerializedResponseTransformer().ignoreStrings()) // Transform responses to json or yaml depending on the Accept header
                .middleware("auth", new AuthMiddleware()) // Register the auth middleware. It can be used for single routes or entire controllers using @With
                .beforeInterceptor(new TokenMiddleware()) // Interceptor that will check the token and fetch the user before anything else is done
                .beforeAny("{*:path}", new ModelBindCheckMiddleware()) // Middleware that checks whether any of the model binds is null
                .exceptionHandler(new ErrorController()) // Register controller that handles exceptions
                .notFound(new NotFoundController()) // Register controller that handles not existing routes
                .staticResourceDirectory("/", TodoExample.class.getClassLoader(), "web") // Serve the web resource directory under /
                .controller(Controller.class, Controller.class.getPackage()); // Search and register controllers in the given package that extend the given parent class
    }

    public void start() {
        server.start().join(); // Starts the server and joins the thread
    }

}
