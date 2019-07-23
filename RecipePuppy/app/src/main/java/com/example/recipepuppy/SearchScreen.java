package com.example.recipepuppy;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchScreen extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<String> ingredientList = new ArrayList<>();
    SearchInterface object;
    EditText dish;
    Button search_btn;
    public SearchScreen() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_screen, container, false);
        try {
            search_btn = view.findViewById(R.id.bt_search);
            mRecyclerView = view.findViewById(R.id.add_ingredients);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            data.add("adding random");
            mAdapter = new AddIngredientAdapter(data);
            Log.d("demo", "Search screen - on activity created");
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            Log.d("demo", "Search screen - after set adapter");

            search_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dish.getText().toString().isEmpty()) {
                        dish.setError("Dish name required");
                    } else {
                        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
                            Log.d("demo", "no. of items in recycler view: " + mRecyclerView.getChildCount());
                            AddIngredientAdapter.ViewHolder holder = (AddIngredientAdapter.ViewHolder) mRecyclerView.getChildViewHolder(mRecyclerView.getChildAt(i));
                            Log.d("demo", "" + holder.et_ingredient.getText().toString());
                            if(holder.et_ingredient.getText().toString().isEmpty() == false) {
                                ingredientList.add(holder.et_ingredient.getText().toString());
                            }
                            Log.d("demo", "Ingredient List: " + ingredientList);
                        }
                        object.searchData(dish.getText().toString(), ingredientList);
                    }
                }
            });
        }catch(Exception e)
        {
            Log.d("demo","exception: "+e);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            object = (SearchInterface) context;
        }catch (Exception e)
        {
            Log.d("demo", "search screen on attach:"+e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        data = new ArrayList<>();
        ingredientList = new ArrayList<>();

        dish = getActivity().findViewById(R.id.dish);
        mAdapter.notifyDataSetChanged();
    }

    public interface SearchInterface{
        void searchData(String dish, ArrayList<String> ingredients);
    }
}
