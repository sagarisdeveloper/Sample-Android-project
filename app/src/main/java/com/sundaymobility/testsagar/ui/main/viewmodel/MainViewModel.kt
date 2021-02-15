package com.sundaymobility.testsagar.ui.main.viewmodel

import androidx.lifecycle.*
import com.sundaymobility.testsagar.data.model.Result
import com.sundaymobility.testsagar.data.repository.MainRepository
import com.sundaymobility.testsagar.utils.NetworkHelper
import com.sundaymobility.testsagar.utils.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(private val mainRepository: MainRepository, private val networkHelper: NetworkHelper) : ViewModel() {

    private val userMLData = MutableLiveData<Resource<Result>>()
    val users: LiveData<Resource<Result>>
        get() = userMLData


     fun fetchUsers(page:Int) {
        viewModelScope.launch {
            userMLData.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers(page).let {
                    if (it.isSuccessful) {
                        userMLData.postValue(Resource.success(it.body()))
                    } else userMLData.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else userMLData.postValue(Resource.error("No internet connection", null))
        }
    }
}