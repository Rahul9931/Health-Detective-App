package com.example.healthdetectiveapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.healthdetectiveapp.api.ApiUtilities;
import com.example.healthdetectiveapp.fragment.HealthNews;
import com.example.healthdetectiveapp.model.MainNews;
import com.example.healthdetectiveapp.model.ModelClass;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsInformation extends AppCompatActivity {


    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.add(R.id.news_container, new HealthNews());
        tr.commit();
    }

    private void fetchNews() {
        ApiUtilities.getApiInterface().getCategoryNews("in","health",100,"29337f0874714e63b8c7e29abe9c28a7").enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if (response.isSuccessful()){
                    modelClassArrayList.addAll(response.body().getArticles());
                    Log.d("check_data", String.valueOf(modelClassArrayList));
                    for (int i=0; i<modelClassArrayList.size();i++)
                    {
                        Log.d("check_data1", String.valueOf(modelClassArrayList.get(i)));
                        Log.d("check_data1", String.valueOf(modelClassArrayList.get(i).getTitle()));

                    }

                }
            }

            @Override
            public void onFailure(Call<MainNews> call, Throwable t) {
                Log.d("check_data", t.toString());
            }
        });
    }
}