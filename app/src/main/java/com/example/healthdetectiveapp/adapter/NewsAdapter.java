package com.example.healthdetectiveapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.healthdetectiveapp.DieasesInformation_Activity;
import com.example.healthdetectiveapp.R;
import com.example.healthdetectiveapp.model.ModelClass;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    Context context;
    ArrayList<ModelClass> modelClassArrayList;
    String title=null;
    String description=null;
    String author=null;
    String img=null;

    public NewsAdapter(Context context, ArrayList<ModelClass> modelClassArrayList){
        this.context = context;
        this.modelClassArrayList = modelClassArrayList;

    }
    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_card,parent,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mNewsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DieasesInformation_Activity.class);
                i.putExtra("url",modelClassArrayList.get(position).getUrl());
                i.putExtra("requestCode","HealthNews");
                context.startActivity(i);
            }
        });

        title= modelClassArrayList.get(position).getTitle();
        description = modelClassArrayList.get(position).getDescription();
        author = modelClassArrayList.get(position).getAuthor();
        img = modelClassArrayList.get(position).getUrlToImage();
        if (title==null || description==null || img==null){
            modelClassArrayList.remove(position);

            if (author==null){
                holder.mAuthor.setVisibility(View.GONE);
            } else {
                holder.mAuthor.setText("Author: "+author);
            }
        }
        /*if (img==null){

        }*/
        if (title!=null && title.length()>40){
            title = title.substring(0,40) + "...";
        }
        if (description!=null && description.length()>40){
            description = description.substring(0,40) + "...";
        }

        if (author!=null && author.length()>15){
            author = author.substring(0,15) + "...";
        }

        holder.mDescription.setText(description);
        holder.mTitle.setText(title);
        Glide.with(context).load(img).into(holder.mimageView);
    }

    @Override
    public int getItemCount() {
        return modelClassArrayList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle, mDescription,mAuthor;
        CardView mNewsContainer;
        ImageView mimageView;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitle = itemView.findViewById(R.id.txt_title);
            mAuthor = itemView.findViewById(R.id.txt_author);
            mDescription = itemView.findViewById(R.id.txt_description);
            mNewsContainer = itemView.findViewById(R.id.news_container_card);
            mimageView = itemView.findViewById(R.id.img);
        }
    }
}
