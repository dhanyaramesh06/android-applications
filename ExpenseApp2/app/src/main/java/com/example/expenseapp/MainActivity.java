/*
    InClass 08
    Dhanya Ramesh - 801073179
 */

package com.example.expenseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ExpenseAppFragment.GotoNextFragment, AddExpenseFragment.AddExpenseList, ShowExpensesFragment.ShowExpenses , EditExpenseFragment.OnFragmentInteractionListener{

    ArrayList<Expense> expense_list;
    ExpenseAppFragment expenseAppFragment;
    ShowExpensesFragment showExpensesFragment;
    EditExpenseFragment editExpenseFragment;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int expense_pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity, new ExpenseAppFragment(), "expense").commit();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("expenses");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expense_list = new ArrayList<>();
                for(DataSnapshot data:dataSnapshot.getChildren())
                {
                    try{
                        expense_list.add(data.getValue(Expense.class));
                    }catch (Exception e)
                    {
                        Log.d("demo","error "+e);
                    }

                }
                Log.d("demo","from Firebase database:"+expense_list);
                Log.d("demo","before bundle:"+expense_list);
                expenseAppFragment = new ExpenseAppFragment();
                Bundle b = new Bundle();
                b.putSerializable("array_list", expense_list);
                expenseAppFragment.setArguments(b);
                setTitle("Expense App");
                getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, expenseAppFragment, "expense").commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addExpense() {
        Log.d("demo", "In Add Expense");
        setTitle("Add Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, new AddExpenseFragment(), "add").commit();
    }

    @Override
    public void showExpense(Expense e) {
        Log.d("demo", "In Show Expense");
        showExpensesFragment = new ShowExpensesFragment();
        Bundle b = new Bundle();
        b.putSerializable("object", e);
        showExpensesFragment.setArguments(b);
        setTitle("Show Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, showExpensesFragment, "show").commit();
    }

    @Override
    public void addExpenseList(String name, double amount, String category, String date) {
        Log.d("demo", "In add expense list method");
        Log.d("demo", "Amount: " + amount);
        Expense e = new Expense();
        e.name = name;
        e.amount = amount;
        e.category = category;
        e.date = date;
        e.setKey(""+(expense_list.size()));
        expense_list.add(e);
        Log.d("demo", "Expense List: " + expense_list);

        myRef.setValue(expense_list);
    }

    @Override
    public void goToNextFragment() {
        setTitle("Expense App");
        if (expenseAppFragment != null)
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, expenseAppFragment, "expense").commit();
        else
            getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, new ExpenseAppFragment(), "expense").commit();
    }

    @Override
    public void backToMainFragment() {
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, expenseAppFragment, "expense").commit();
    }

    @Override
    public void goToEditExpense(Expense e)
    {
        Log.d("demo", "In Edit Expense");
        editExpenseFragment = new EditExpenseFragment();
        Bundle b = new Bundle();
        Log.d("demo","Expense from show:"+e);
        b.putSerializable("object", e);
        editExpenseFragment.setArguments(b);
        setTitle("Edit Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, editExpenseFragment, "edit").commit();
    }

    @Override
    public void onFragmentInteraction() {
        setTitle("Show Expense");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, showExpensesFragment, "show").commit();
    }

    @Override
    public void updateFirebase(String name, double amount, String category) {
        Log.d("demo", "In update firebase db method");
        Expense e = new Expense();
        e.name = name;
        e.amount = amount;
        e.category = category;
        e.date = expense_list.get(expense_pos).date;
        expense_list.remove(expense_pos);
        expense_list.add(expense_pos,e);
        myRef.setValue(expense_list);
        setTitle("Expense App");
        getSupportFragmentManager().beginTransaction().replace(R.id.main_activity, expenseAppFragment, "edit").commit();
    }

    @Override
    public void expenseSavePosition(int pos) {
        expense_pos = pos;
    }

    @Override
    public void expenseDelPosition(int pos) {
        Log.d("demo","Deleting entry in firebase db");
        myRef = FirebaseDatabase.getInstance().getReference("expenses").child(String.valueOf(pos));
        myRef.removeValue();
        myRef = database.getReference("expenses");
    }


}
