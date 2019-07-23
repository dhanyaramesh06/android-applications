package com.example.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddExpenseFragment extends Fragment {

    AddExpenseList object;
    String category, expense_name;
    EditText name,amount;
    double amt;
    int spinner_pos;
    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_expense, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (AddExpenseList) context;
        }catch(Exception e)
        {
            Log.d("demo",""+e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Spinner spinner = getActivity().findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.Categories,android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //spinner.setSelection(adapter.getCount()-1);
        Log.d("demo","In On Activity Created");
        name = getActivity().findViewById(R.id.expense_name);
        amount = getActivity().findViewById(R.id.amount);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
                //spinner_pos = spinner.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        getActivity().findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || amount.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Fill in the details", Toast.LENGTH_SHORT).show();
                }
                if(category.equals("Select Category"))
                {
                    Toast.makeText(getActivity(), "Choose a category", Toast.LENGTH_SHORT).show();
                }
                else{
                    expense_name = name.getText().toString();
                    amt = Double.parseDouble(amount.getText().toString());
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                    final String date = df.format(d);
                    object.addExpenseList(expense_name,amt,category,date);
                }
            }
        });

        getActivity().findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.goToNextFragment();
            }
        });
    }

    public interface AddExpenseList{
        void addExpenseList(String name, double amount, String category, String date);
        void goToNextFragment();
    }
}
