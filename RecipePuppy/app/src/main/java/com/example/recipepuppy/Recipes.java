package com.example.recipepuppy;

public class Recipes {

    String title, ingredients, recipe_img, recipe_url;

    public Recipes() {
    }

    @Override
    public String toString() {
        return "Recipes{" +
                "title='" + title + '\'' +
                ", ingredients='" + ingredients + '\'' +
                ", recipe_img='" + recipe_img + '\'' +
                ", recipe_url='" + recipe_url + '\'' +
                '}';
    }
}
