package com.example.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShowExpensesFragment extends Fragment {

    ShowExpenses object;
    Expense expense;

    public ShowExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (ShowExpenses) context;
        }catch(Exception e)
        {
            Log.d("demo",""+e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_expenses, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("demo","In ShowExpense Fragment");
        TextView name = getActivity().findViewById(R.id.name);
        TextView category = getActivity().findViewById(R.id.category);
        TextView amount = getActivity().findViewById(R.id.amount);
        TextView date = getActivity().findViewById(R.id.date);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            expense = (Expense)bundle.getSerializable("object");
            Log.d("demo","Expense: "+expense);
            name.setText(expense.name);
            category.setText(expense.category);
            amount.setText("$ "+String.valueOf(expense.amount));
            date.setText(expense.date);
        }
        else
            Log.d("demo","No value in bundle");

        getActivity().findViewById(R.id.close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.backToMainFragment();
            }
        });

        getActivity().findViewById(R.id.edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.goToEditExpense(expense);
            }
        });
    }

    public static interface ShowExpenses{
        public void backToMainFragment();
        public void goToEditExpense(Expense e);
    }
}
