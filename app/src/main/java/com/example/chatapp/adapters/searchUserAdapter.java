package com.example.chatapp.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.ActivityEmpPage.MasterPage;
import com.example.chatapp.R;
import com.example.chatapp.utilities.UserDetails;
import com.example.chatapp.utilities.UserDetailsRecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class searchUserAdapter extends RecyclerView.Adapter<searchUserAdapter.MyViewHolder>{
    Context context;
    ArrayList<UserDetailsRecyclerView> usersArrayList;
    private final RecyclerViewInterface recyclerViewInterface;
    UserDetails userDetails = new UserDetails();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public searchUserAdapter(Context context, ArrayList<UserDetailsRecyclerView> usersArrayList, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.usersArrayList = usersArrayList;
        this.recyclerViewInterface = recyclerViewInterface;
    }
    public void setFilteredList(ArrayList<UserDetailsRecyclerView> filteredList){
        this.usersArrayList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public searchUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_container_user,parent,false);
        return new MyViewHolder(view, recyclerViewInterface);
    }

    //holder for display data
    @Override
    public void onBindViewHolder(@NonNull searchUserAdapter.MyViewHolder holder, int position) {
        UserDetailsRecyclerView userDetailsRecyclerView = usersArrayList.get(position);
        holder.name.setText("Name: "+userDetailsRecyclerView.getName());
        holder.meterSerialNumber.setText("Serial Number: "+userDetailsRecyclerView.getMeterSerialNumber());
        String imageUrl = null;
        imageUrl = userDetailsRecyclerView.getImage();
        Picasso.get().load(imageUrl).into(holder.image);
        holder.readImage.setVisibility(View.GONE);
        holder.unreadImage.setVisibility(View.GONE);
        if(userDetails.getUserType().equalsIgnoreCase("Meter Reader")){
            if(userDetailsRecyclerView.getRemarks().equalsIgnoreCase("Read")){
                holder.readImage.setVisibility(View.VISIBLE);
            }else if(userDetailsRecyclerView.getRemarks().equalsIgnoreCase("Unread")){
                holder.unreadImage.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, meterSerialNumber;
        ImageView image, readImage, unreadImage;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.textName);
            meterSerialNumber = itemView.findViewById(R.id.textSerialNumber);
            image = itemView.findViewById(R.id.imageProfile);
            unreadImage = itemView.findViewById(R.id.unreadImage);
            readImage = itemView.findViewById(R.id.readImage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}


