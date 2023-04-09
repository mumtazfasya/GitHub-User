package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.response.DetailUserResponse
import com.example.myapplication.response.ItemsItem
import com.example.myapplication.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    private val _listDataProfile = MutableLiveData<List<ItemsItem>>()
    val listdataprofile: LiveData<List<ItemsItem>> = _listDataProfile

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getSearchUsers(USERNAME)
    }

    fun getSearchUsers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApi().getSearchUsers(username)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listDataProfile.value = responseBody.items as List<ItemsItem>?
                        val updatedList = mutableListOf<ItemsItem>()
                        for (userItem in _listDataProfile.value.orEmpty()) {
                            getName(userItem.login) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listDataProfile.value?.size) {
                                    _listDataProfile.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun getName(username: String?, callback: (String?) -> Unit) {
        _isLoading.value = true
        val client = ApiConfig.getApi().getUserProfile(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        callback(responseBody.name)
                    }
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UserViewModel"
        private const val USERNAME = "mumtaz"
    }
}

