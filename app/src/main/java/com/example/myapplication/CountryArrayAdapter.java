package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CountryArrayAdapter extends RecyclerView.Adapter {

    private final JSONArray items;
    private final Context context;

    public CountryArrayAdapter(Context context, JSONArray objects) {
        this.items = objects;
        this.context = context;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CountryArrayAdapter.ViewHolder v = (CountryArrayAdapter.ViewHolder) holder;
        JSONObject o = null;
        try {
            o = (JSONObject) items.get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (o != null) {
            try {
                String flag_url = o.getJSONObject("flags").getString("png");
                String name = o.getJSONObject("name").getString("common");
                String capital = o.getJSONArray("capital").getString(0);
                String population = String.valueOf(o.getLong("population"));

                Glide.with(context).load(flag_url).into(v.imageView);
                v.country_name.setText(name);
                v.country_capital.setText(capital);
                v.population.setText(population);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Remove item at position
     *
     * @param position index
     */
    public void removeData(int position) {
        items.remove(position);
    }


    @Override
    public int getItemCount() {
        return items.length();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView country_name;
        TextView country_capital;
        TextView population;
        ImageView imageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.flag);
            country_name = itemView.findViewById(R.id.country_name);
            country_capital = itemView.findViewById(R.id.country_capital);
            population = itemView.findViewById(R.id.population);
        }
    }

}