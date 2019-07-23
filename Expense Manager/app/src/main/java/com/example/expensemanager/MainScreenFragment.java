package com.example.expensemanager;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainScreenFragment extends Fragment implements ExpenseAdapter.OnItemClick, ExpenseAdapter.SendRecyclerViewPosition {

    GotoAddFragment object;
    ArrayList<Expense> expenses = new ArrayList<>();
    View view;
    TextView total;
    double tot = 0.0;


    private RecyclerView mrecyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public MainScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_main_screen, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            object = (GotoAddFragment)context;
        }
        catch (Exception e)
        {
            Log.d("demo","Exception Occurred" +e);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        total = getActivity().findViewById(R.id.total);
        mrecyclerView = getActivity().findViewById(R.id.recyclerView);
        mrecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        mrecyclerView.setLayoutManager(layoutManager);

        //total.setText("$0.0");

        Bundle bundle = this.getArguments();
        if(bundle!=null)
        {
            Log.d("demo","In main fragment");
            tot = 0.0;
            expenses = (ArrayList)(bundle.getSerializable("list"));

            for(int i =0;i<expenses.size();i++)
            {
                Log.d("demo","Cost "+expenses.get(i).getCost());
                tot +=expenses.get(i).getCost();
            }
            //tot = (double)bundle.getSerializable("total");
            total.setText("$ "+tot);
            Log.d("demo","total value"+tot);
            adapter = new ExpenseAdapter(expenses,this,this);
            mrecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        getActivity().findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.addExpense();
            }
        });

    }

    @Override
    public void onClick(int pos) {
        //editExpenseFragment = new EditExpenseFragment();

        //ArrayList<Expense> expense_list = new ArrayList<>();
        Expense e = expenses.get(pos);
        object.onEdit(e);
        //expense_list.add(expenses.get(pos));

    }

    @Override
    public void positionToDisplay(Expense e) {
        object.display(e);
    }


    public interface GotoAddFragment{
        void addExpense();
        void onEdit(Expense e);
        void display(Expense e);
}
}
