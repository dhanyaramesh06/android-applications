package com.example.expenseapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense>{

    public ExpenseAdapter(@NonNull Context context, int resource, @NonNull List<Expense> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView,  @NonNull ViewGroup parent) {
        Expense expense = getItem(position);
        ViewHolder viewHolder;
        if(convertView ==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
            convertView.setTag(viewHolder);

        }
        else
        {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(expense.name);
        viewHolder.tv_amount.setText("$"+expense.amount);
        return  convertView;
    }



    private static class ViewHolder{
        TextView tv_name,tv_amount;
    }
}