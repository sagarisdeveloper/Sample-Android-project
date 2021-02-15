package com.sundaymobility.testsagar.data.api

import com.sundaymobility.testsagar.data.model.Result
import com.sundaymobility.testsagar.data.model.User
import retrofit2.Response

interface ApiHelper {

    suspend fun getUsers(page:Int): Response<Result>
}