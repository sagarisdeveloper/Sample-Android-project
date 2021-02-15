package com.sundaymobility.testsagar.data.api

import com.sundaymobility.testsagar.data.model.Result
import com.sundaymobility.testsagar.data.model.User
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper {

    override suspend fun getUsers(page:Int): Response<Result> = apiService.getUsers(page)

}