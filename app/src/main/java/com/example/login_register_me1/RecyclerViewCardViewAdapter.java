package com.example.login_register_me1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.login_register_me1.Model.Data;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewCardViewAdapter extends RecyclerView.Adapter<JSONDataViewHolder> {

    ArrayList<Data> list;
    Context context;

    public RecyclerViewCardViewAdapter() {
    }

    public RecyclerViewCardViewAdapter(ArrayList<Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public JSONDataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.data_from_db,viewGroup,false);
        final JSONDataViewHolder jsonDataViewHolder = new JSONDataViewHolder(view);
        return jsonDataViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull JSONDataViewHolder jsonDataViewHolder, int i) {
        Data currentData = list.get(i);
        jsonDataViewHolder.setType(currentData.getType());
        jsonDataViewHolder.setDate(currentData.getDate());
        jsonDataViewHolder.setAmount(currentData.getAmount());
        jsonDataViewHolder.setNote(currentData.getNote());
        //Add an imageview if you want
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

