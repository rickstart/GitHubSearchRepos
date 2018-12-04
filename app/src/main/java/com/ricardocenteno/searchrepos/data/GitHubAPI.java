package com.ricardocenteno.searchrepos.data;

import com.ricardocenteno.searchrepos.models.GitHubResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubAPI {
    @GET("/search/repositories")
    Call<GitHubResponse> loadRepos(@Query("q") String repoName ,@Query("sort") String type);
}
