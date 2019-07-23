package com.example.recipepuppy;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DisplayRecipesAdapter extends RecyclerView.Adapter<DisplayRecipesAdapter.ViewHolder> {

    ArrayList<Recipes> recipe_list;
    Recipes r;

    public DisplayRecipesAdapter(ArrayList<Recipes> recipe_list) {
        this.recipe_list = recipe_list;
    }

    @NonNull
    @Override
    public DisplayRecipesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_recipes,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DisplayRecipesAdapter.ViewHolder viewHolder, int i) {
        r = recipe_list.get(i);
        viewHolder.tv_title.setText("Title:");
        viewHolder.tv_ingredient.setText("Ingredients:");
        viewHolder.tv_url.setText("URL:");
        viewHolder.title.setText(r.title);
        viewHolder.ingredient.setText(r.ingredients);
        viewHolder.url.setText(r.recipe_url);
        try {
            if (r.recipe_img.isEmpty()) {
                viewHolder.img.setImageResource(R.drawable.no_image);
            } else {

                Picasso.get().load(r.recipe_img).into(viewHolder.img);
            }
        }catch(Exception e)
        {
            Log.d("demo","error:"+e);
        }

    }

    @Override
    public int getItemCount() {
        return recipe_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_ingredient, tv_url;
        TextView title, ingredient, url;
        ImageView img;

        public ViewHolder(final View itemView) {
            super(itemView);
            Log.d("demo","Display Recipe Adapter - View Holder class");
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_ingredient = itemView.findViewById(R.id.tv_ingredients);
            tv_url = itemView.findViewById(R.id.tv_url);
            title = itemView.findViewById(R.id.title);
            ingredient = itemView.findViewById(R.id.ingredients);
            url = itemView.findViewById(R.id.url);
            img = itemView.findViewById(R.id.imageView);

            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(r.recipe_url));
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }

}
