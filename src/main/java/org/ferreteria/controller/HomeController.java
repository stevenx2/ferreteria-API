package org.ferreteria.controller;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controlador simplemente de prueba
 */
@RestController
@RequestMapping("/api/v1")
public class HomeController {


    @GetMapping
    public String home(){
        return "<H1>Ta bien</H1>";
    }
}
