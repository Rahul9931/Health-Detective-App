package com.example.healthdetectiveapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.example.healthdetectiveapp.R;
import com.example.healthdetectiveapp.adapter.NewsAdapter;
import com.example.healthdetectiveapp.api.ApiUtilities;
import com.example.healthdetectiveapp.model.MainNews;
import com.example.healthdetectiveapp.model.ModelClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthNews extends Fragment {
    RecyclerView recyclerView;
    NewsAdapter adapter;
    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    public HealthNews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health_news, container, false);
        recyclerView = view.findViewById(R.id.rv_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsAdapter(getContext(),modelClassArrayList);
        recyclerView.setAdapter(adapter);
        fetchNews();
        return view;
    }
    private void fetchNews() {
        ApiUtilities.getApiInterface().getCategoryNews("in","health",100,"29337f0874714e63b8c7e29abe9c28a7").enqueue(new Callback<MainNews>() {
            @Override
            public void onResponse(Call<MainNews> call, Response<MainNews> response) {
                if (response.isSuccessful()){
                    modelClassArrayList.clear();
                    modelClassArrayList.addAll(response.body().getArticles());
                    adapter.notifyDataSetChanged();
                    Log.d("check_data", String.valueOf(modelClassArrayList));
                    for (int i=0; i<modelClassArrayList.size();i++)
                    {
                        Log.d("check_data1", String.valueOf(modelClassArrayList.get(i)));
                        Log.d("check_data2", String.valueOf(modelClassArrayList.get(i).getTitle()));

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