package com.sundaymobility.testsagar.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sundaymobility.testsagar.data.model.Result
import com.sundaymobility.testsagar.data.repository.MainRepository
import com.sundaymobility.testsagar.utils.NetworkHelper
import com.sundaymobility.testsagar.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val userMLData = MutableLiveData<Resource<Result>>()
    val users: LiveData<Resource<Result>>
        get() = userMLData

    /**
     * hit API
     */
    fun fetchUsers(page: Int) {
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