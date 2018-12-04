package com.ricardocenteno.searchrepos.view;

import android.app.SearchManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ricardocenteno.searchrepos.R;
import com.ricardocenteno.searchrepos.adapters.ReposAdapter;
import com.ricardocenteno.searchrepos.data.GitHubAPI;
import com.ricardocenteno.searchrepos.models.GitHubResponse;
import com.ricardocenteno.searchrepos.models.Repository;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<GitHubResponse> {
    private static final String BASE_URL = "https://api.github.com";
    private ReposAdapter adapter;
    private GitHubAPI api;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView rvRepos = findViewById(R.id.rvRepos);
        progress = findViewById(R.id.progress);
        rvRepos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new ReposAdapter(new ArrayList<Repository>());
        rvRepos.setAdapter(adapter);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(GitHubAPI.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query!=null && !query.equals("")) {
                    Call<GitHubResponse> call = api.loadRepos(query);
                    call.enqueue(MainActivity.this);
                    progress.setVisibility(View.VISIBLE);
                }else{
                    progress.setVisibility(View.GONE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return false;
            }
        });

        return true;
    }

    @Override
    public void onResponse(Call<GitHubResponse> call, Response<GitHubResponse> response) {
        progress.setVisibility(View.GONE);
        GitHubResponse gitHubResponse = response.body();
        if (gitHubResponse!=null) {
            if (gitHubResponse.getItems()!=null && gitHubResponse.getItems().size()==0)
                Toast.makeText(this, getString(R.string.no_results),Toast.LENGTH_SHORT).show();
            adapter.setObjects(gitHubResponse.getItems());
        }

    }

    @Override
    public void onFailure(Call<GitHubResponse> call, Throwable t) {
        progress.setVisibility(View.GONE);
        t.printStackTrace();
        Toast.makeText(this,t.getMessage(),Toast.LENGTH_SHORT).show();

    }
}
