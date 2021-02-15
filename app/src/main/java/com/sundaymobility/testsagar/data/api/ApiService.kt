package com.sundaymobility.testsagar.data.api

import com.sundaymobility.testsagar.data.model.Result
import com.sundaymobility.testsagar.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("users/")
    suspend fun getUsers( @Query("page")page :Int): Response<Result>

}