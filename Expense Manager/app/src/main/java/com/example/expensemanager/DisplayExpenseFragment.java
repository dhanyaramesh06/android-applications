package com.example.expensemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayExpenseFragment extends Fragment {
    TextView name, cost, date;
    ImageView image;
    Expense expenses;
    DisplayInterface object;

    public DisplayExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_expense, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (DisplayInterface) getActivity();
         }catch (Exception e)
        {
            Log.d("demo","Error: "+e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        name = getActivity().findViewById(R.id.name);
        cost = getActivity().findViewById(R.id.amount);
        date = getActivity().findViewById(R.id.date);
        image = getActivity().findViewById(R.id.imageView);

        Bundle b = this.getArguments();
        if(b!=null)
        {
            expenses = (Expense)(b.getSerializable("expense"));
            name.setText(expenses.getName());
            cost.setText("$ "+expenses.getCost());
            date.setText(expenses.getDate());
            Picasso.get().load(expenses.getImage_url()).into(image);
        }

        getActivity().findViewById(R.id.finish_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.goToMainScreen();
            }
        });
    }

    public interface DisplayInterface{
        void goToMainScreen();
    }
}
