package com.example.login_register_me1;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class JSONDataViewHolder extends RecyclerView.ViewHolder{

    View myview;

    public JSONDataViewHolder(View itemView){
        super(itemView);
        myview = itemView;
    }

    public void setType(String type){
        TextView mType = myview.findViewById(R.id.type);
        mType.setText(type);
    }

    public void setNote(String note){
        TextView mNote = myview.findViewById(R.id.note);
        mNote.setText(note);
    }

    public void setDate(String date){
        TextView mDate = myview.findViewById(R.id.date);
        mDate.setText(date);
    }

    public void setAmount(String amount){
        TextView mAmount = myview.findViewById(R.id.amount);
        mAmount.setText(amount);
    }
}
