package com.zeina.BUMPCodeChallenge.search

import com.zeina.BUMPCodeChallenge.data.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {
    @GET("/v1/gifs/search")
    fun fetchByField(@Query("q") query: String, @Query("limit") limit: Int, @Query("offset") offset: Int, @Query("api_key") api_key: String): Call<SearchModel>
}
