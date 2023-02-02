package com.example.chatapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.UserDetailsRecyclerView;

import java.util.ArrayList;

public class searchUserAdapter extends RecyclerView.Adapter<searchUserAdapter.MyViewHolder>{
    Context context;
    ArrayList<UserDetailsRecyclerView> usersArrayList;

    public searchUserAdapter(Context context, ArrayList<UserDetailsRecyclerView> usersArrayList) {
        this.context = context;
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public searchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull searchUserAdapter.MyViewHolder holder, int position) {
        UserDetailsRecyclerView userDetailsRecyclerView = usersArrayList.get(position);
        holder.name.setText("Name: "+userDetailsRecyclerView.getName());
        holder.meterSerialNumber.setText("Serial Number: "+userDetailsRecyclerView.getMeterSerialNumber());
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, meterSerialNumber;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            meterSerialNumber = itemView.findViewById(R.id.textSerialNumber);
        }
    }
}
