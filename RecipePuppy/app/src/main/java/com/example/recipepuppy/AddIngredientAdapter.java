package com.example.recipepuppy;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddIngredientAdapter extends RecyclerView.Adapter<AddIngredientAdapter.ViewHolder> {

    ArrayList<String> count_list;
    int pos;
    int i;

    public AddIngredientAdapter(ArrayList<String> data) {
        this.count_list = data;
        this.i = 0;
        Log.d("demo","In Add Ingredient Adapter");
    }

    @NonNull
    @Override
    public AddIngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_ingredients,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        Log.d("demo","Add Ingredient Adapter - on create view holder");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int position) {
    }

    @Override
    public int getItemCount() {
        //Log.d("demo","Add Ingredient Adapter - get item count");
        return count_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText et_ingredient;
        FloatingActionButton add;

        public ViewHolder(View itemView) {
            super(itemView);
            Log.d("demo","Add Ingredient Adapter - View Holder class");
            et_ingredient = itemView.findViewById(R.id.ingredient);
            add = itemView.findViewById(R.id.add);
            add.setTag(R.drawable.add_icon);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(R.drawable.add_icon == (Integer) add.getTag())
                    {
                        add.setImageResource(R.drawable.cancel_icon);
                        add.setTag(R.drawable.cancel_icon);
                        Log.d("demo", "edittext value" + et_ingredient.getText().toString());
                        Log.d("demo", "count list: " + count_list);
                        if (i >= 4) {
                            Toast.makeText(v.getContext(), "Maximum of 5 ingredients only", Toast.LENGTH_SHORT).show();
                        } else {
                            i++;
                            count_list.add("New Edit Text");
                            notifyItemInserted(getAdapterPosition());
                        }
                    }
                    else
                    {
                        if(i>0)
                        {
                            add.setImageResource(R.drawable.add_icon);
                            add.setTag(R.drawable.add_icon);
                            et_ingredient.setText("");
                             pos= getAdapterPosition();
                            count_list.remove(pos);
                            i--;
                            notifyItemRemoved(pos);
                        }
                        else
                        {
                            add.setImageResource(R.drawable.add_icon);
                            add.setTag(R.drawable.add_icon);
                            et_ingredient.setText("");
                        }

                    }
                    Log.d("demo","i value: "+i);
                }

            });

        }
    }
}
