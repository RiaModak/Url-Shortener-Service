package com.example.urlshortener.controller;

// Import the GetMapping annotation. This is the new part we are adding.
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A REST Controller is a core component in Spring for building web services.
 * The @RestController annotation combines two other annotations: @Controller and @ResponseBody.
 *
 * @Controller: Marks this class as a Spring MVC controller, a component that handles web requests.
 * @ResponseBody: Indicates that the return value of the methods in this controller
 *                should be written directly to the HTTP response body, rather than being
 *                interpreted as a view name. This is perfect for APIs that return data (like JSON or text).
 *
 * This controller will handle basic health checks for our service.
 */
@RestController
public class HealthCheckController {

    /**
     * This method handles HTTP GET requests to the "/ping" endpoint.
     * The @GetMapping annotation maps this method to that specific URL path.
     *
     * When a client (like a web browser) sends a GET request to http://localhost:8080/ping,
     * Spring's DispatcherServlet will route the request to this method.
     *
     * The method returns a String "Pong". Because this class is annotated with @RestController,
     * Spring automatically takes this return value and writes it as the body of the HTTP response
     * with a content type of "text/plain".
     *
     * @return The string "Pong" as the response.
     */
    @GetMapping("/ping")
    public String ping() {
        return "Pong";
    }
}