/*
Homework 04 - Group 4
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh - 801073179
 */

package com.example.recipepuppy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchScreen.SearchInterface, RecipesScreen.RecipeInterface {

    RecipesScreen recipesScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Recipe Puppy");
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity,new SearchScreen(),"search").commit();
    }

    @Override
    public void searchData(String dish, ArrayList<String> ingredients)
    {
        Log.d("demo","In main activity - dish name: "+dish);
        Log.d("demo","In main activity - ingredients: "+ingredients);
        recipesScreen = new RecipesScreen();
        Bundle b = new Bundle();
        b.putSerializable("list",ingredients);
        b.putSerializable("dish",dish);
        recipesScreen.setArguments(b);
        setTitle("Recipes");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,recipesScreen,"recipe").addToBackStack("search").commit();
    }

    @Override
    public void onBackPressed() {
        Log.d("demo", "on Back Pressed");
        if(getSupportFragmentManager().getBackStackEntryCount()>0)
        {
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void backToActivity() {
        Log.d("demo","backToActivity");
        setTitle("Recipe Puppy`");
        onBackPressed();
    }
}
