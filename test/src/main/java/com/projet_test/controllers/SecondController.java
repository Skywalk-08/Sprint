package com.projet_test.controllers;

import framework.annotations.Controller;
import framework.annotations.UrlMapping;

@Controller
public class SecondController {

    @UrlMapping("/products")
    public String listProducts() {
        return "Liste des produits (Sprint 2)";
    }

    @UrlMapping("/products/add")
    public String addProduct() {
        return "Formulaire d'ajout de produit";
    }
}
