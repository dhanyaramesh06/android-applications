package com.example.expensemanager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private OnItemClick mCallback;
    SendRecyclerViewPosition recyclerViewPosition;
    public View.OnClickListener itemClickListener;
    ArrayList<Expense> expenseArrayList;
    public ExpenseAdapter(ArrayList<Expense> expenseArrayList, OnItemClick listener, SendRecyclerViewPosition recyclerViewPosition) {
        this.expenseArrayList = expenseArrayList;
        this.mCallback = listener;
        this.recyclerViewPosition = recyclerViewPosition;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Expense expense = expenseArrayList.get(i);
        viewHolder.tv_date.setText("Date:");
        viewHolder.tv_cost.setText("Cost:");
        viewHolder.expenses_name.setText(expense.name);
        viewHolder.cost.setText("$"+expense.cost);
        viewHolder.date.setText(expense.date);
        final int pos = i;
        viewHolder.edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    EditExpenseFragment editExpenseFragment = new EditExpenseFragment();
                    mCallback.onClick(pos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return expenseArrayList.size();
    }

    public interface OnItemClick {
        void onClick (int position);
    }

    public interface SendRecyclerViewPosition{
        void positionToDisplay(Expense e);
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView expenses_name,cost,date;
        TextView  tv_cost, tv_date;
        ImageView edit_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expenses_name = (TextView) itemView.findViewById(R.id.expense_name);
            cost = (TextView) itemView.findViewById(R.id.cost);
            date = (TextView) itemView.findViewById(R.id.date);
            tv_cost =(TextView) itemView.findViewById(R.id.tv_cost);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            edit_btn = (ImageButton) itemView.findViewById(R.id.edit_btn);

            itemView.setTag(this);
            itemView.setOnClickListener(itemClickListener);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Expense expense = expenseArrayList.get(position);
                    recyclerViewPosition.positionToDisplay(expense);
                }
            });
//            edit_btn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EditExpenseFragment editExpenseFragment = new EditExpenseFragment();
//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.main_activity,editExpenseFragment,"edit").commit();
//                }
//            });
        }
    }

}