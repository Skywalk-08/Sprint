package com.projet_test.controllers;

import framework.annotations.Controller;
import framework.annotations.UrlMapping;

@Controller
public class FirstController {

    @UrlMapping("/home")
    public String home() {
        return "Bienvenue sur la page Home !";
    }

    @UrlMapping("/about")
    public String about() {
        return "Page About — Mini Framework Sprint 2";
    }

    // Méthode sans @UrlMapping → ignorée par le framework
    public void methodeIgnoree() {
        System.out.println("Je ne suis pas mappée");
    }
}
