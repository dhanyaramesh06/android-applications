package com.example.expenseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditExpenseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditExpenseFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    EditText name,amount;
    Spinner spinner;
    String category, expense_name;
    double amt;
    int pos = 0;

    public EditExpenseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_expense, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("demo","In EditExpense Fragment");
        name = getActivity().findViewById(R.id.expense_name);
        spinner = getActivity().findViewById(R.id.spinner);
        String[] categories = getResources().getStringArray(R.array.Categories);
        for(int i = 0; i<categories.length; i++)
        {
            Log.d("demo","categories "+categories[i]);
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),R.array.Categories,android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        amount = getActivity().findViewById(R.id.amount);
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            Expense expense = (Expense)bundle.getSerializable("object");
            Log.d("demo","Expense: "+expense);
            name.setText(expense.name);
            amount.setText(String.valueOf(expense.amount));

            for(int i =0; i<categories.length; i++)
            {
                if(categories[i].equals(expense.category))
                {
                    pos = i;
                    break;
                }

            }
           spinner.setSelection(pos);
        }

        else
            Log.d("demo","No value in bundle");

        getActivity().findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = spinner.getSelectedItem().toString();
               // spinner_pos = spinner.getSelectedItemPosition();
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
                    mListener.updateFirebase(expense_name,amt,category);
                }
            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
        void updateFirebase(String name, double amount, String category);
    }
}
