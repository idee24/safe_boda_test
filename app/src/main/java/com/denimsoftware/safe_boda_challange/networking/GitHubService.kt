package com.denimsoftware.safe_boda_challange.networking

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

/**
 *Created by Yerimah on 11/21/2020.
 */
interface GitHubService {

    @GET(Routes.SEARCH_END_POINT)
    fun retrieveUsers(@Query(APIParameters.SEARCH_PARAM) searchKey: String,
                      @Query(APIParameters.PAGE_PARAM) page: Int): Call<SearchResponse>

    @GET
    fun retrieveUser(@Header(APIParameters.ACCEPT_PARAM) key: String,
                     @Url url: String):Call<UserModel>

    @GET
    fun retrieveFollow(@Header(APIParameters.ACCEPT_PARAM) key: String,
                       @Url url: String): Call<List<SearchModel>>
}