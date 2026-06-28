package com.projet_test.controllers;

import framework.annotations.Controller;

/**
 * Controller avec @Controller mais SANS @UrlMapping sur ses méthodes
 * → les méthodes ne doivent PAS apparaître dans la liste
 */
@Controller
public class ThirdController {

    public void nonMapped() {
        System.out.println("Pas de @UrlMapping ici");
    }
}
