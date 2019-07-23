/*
Group 4 - InClass 07
Chella Archana Kandaswamy - 801085762
Dhanya Ramesh - 801073179
 */

package com.example.inclass07;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.GotoNextFragment, AddExpenseFragment.AddExpenseList, ShowExpensesFragment.ShowExpenses {

    ArrayList<Expense> expense_list = new ArrayList<>();
    ExpenseAppFragment expenseAppFragment;
    ShowExpensesFragment showExpensesFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity,new ExpenseAppFragment(),"expense").commit();
    }

    @Override
    public void addExpense() {
        Log.d("demo","In Add Expense");
        setTitle("Add Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new AddExpenseFragment(),"add").commit();
    }

    @Override
    public void showExpense(Expense e) {
        Log.d("demo","In Show Expense");
        showExpensesFragment = new ShowExpensesFragment();
        Bundle b = new Bundle();
        b.putSerializable("object",e);
        showExpensesFragment.setArguments(b);
        setTitle("Show Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,showExpensesFragment,"show").commit();
    }

    @Override
    public void addExpenseList(String name, double amount, String category, String date) {
        Log.d("demo","In add expense list method");
        Log.d("demo","Amount: "+amount);
        Expense e = new Expense();
        e.name = name;
        e.amount = amount;
        e.category = category;
        e.date = date;
        expense_list.add(e);
        Log.d("demo","Expense List: "+expense_list);

        expenseAppFragment = new ExpenseAppFragment();
        Bundle b = new Bundle();
        b.putSerializable("array_list",expense_list);
        expenseAppFragment.setArguments(b);
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,expenseAppFragment,"expense").commit();
    }

    @Override
    public void goToNextFragment() {
        setTitle("Expense App");
        if(expenseAppFragment!=null)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,expenseAppFragment,"expense").commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,new ExpenseAppFragment(),"expense").commit();
    }

    @Override
    public void backToMainFragment() {
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,expenseAppFragment,"expense").commit();
    }
}
