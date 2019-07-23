package com.example.expenseapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExpenseAppFragment extends Fragment {

    GotoNextFragment object;
    ArrayList<Expense> expenses = new ArrayList<>();
    ExpenseAdapter adapter;

    public ExpenseAppFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense_app, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (GotoNextFragment) context;
        }catch(Exception e)
        {
            Log.d("demo",""+e);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = getActivity().findViewById(R.id.expenseList);
        final TextView no_expense = getActivity().findViewById(R.id.no_expense);
        no_expense.setText("There is no expense to show, Please add your expenses from the menu");
        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            expenses = (ArrayList)(bundle.getSerializable("array_list"));
            no_expense.setVisibility(View.INVISIBLE);
            adapter = new ExpenseAdapter(getContext(),R.layout.listview,expenses);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        else
        {
            Log.d("demo","Bundle null in expense app fragment");
        }
        getActivity().findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("demo","In Add Button OnClick");
                object.addExpense();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Expense e = (Expense) parent.getItemAtPosition(position);
                object.showExpense(e);
                object.expenseSavePosition(position);
            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Expense obj = adapter.getItem(position);
                expenses.remove(position);

                Log.d("demo","ArrayList after deletion: "+expenses);
                adapter.notifyDataSetChanged();
                object.expenseDelPosition(Integer.parseInt(obj.getKey()));
                Toast.makeText(getContext(), "Expense Deleted", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if(expenses.isEmpty())
        {
            no_expense.setVisibility(View.VISIBLE);
        }
    }

    public interface GotoNextFragment{
        void addExpense();
        void showExpense(Expense e);
        void expenseSavePosition(int pos);
        void expenseDelPosition(int pos);
    }
}
