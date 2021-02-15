package com.sundaymobility.testsagar.data.repository

import com.sundaymobility.testsagar.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getUsers(page:Int) =  apiHelper.getUsers(page)

}