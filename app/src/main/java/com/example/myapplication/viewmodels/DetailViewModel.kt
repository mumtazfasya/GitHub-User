package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.api.ApiConfig
import com.example.myapplication.database.Favorite
import com.example.myapplication.repository.UserRepository
import com.example.myapplication.response.DetailUserResponse
import com.example.myapplication.response.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _profile = MutableLiveData<DetailUserResponse>()
    val profile: LiveData<DetailUserResponse> = _profile

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFavorite = MutableLiveData<Favorite>()
    val isFavorite: MutableLiveData<Favorite> = _isFavorite

    private val mUserRepository: UserRepository = UserRepository(application)

    fun insert(user: Favorite) {
        mUserRepository.insert(user)
    }

    fun delete(user: Favorite) {
        mUserRepository.delete(user)
    }

    fun getFavoriteByUsername(user: String): LiveData<Favorite> =
        mUserRepository.getFavoriteUserByUsername(user)

    fun getProfile(username: String) {
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
                        _profile.value = responseBody!!
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApi().getUserConnectionResponse(username, "followers")
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowers.value = responseBody!!
                        val updatedList = mutableListOf<ItemsItem>()
                        for (userItem in _listFollowers.value.orEmpty()) {
                            getName(userItem.login) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listFollowers.value?.size) {
                                    _listFollowers.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApi().getUserConnectionResponse(username, "following")
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listFollowing.value = responseBody!!
                        val updatedList = mutableListOf<ItemsItem>()
                        for (userItem in _listFollowing.value.orEmpty()) {
                            getName(userItem.login) { name ->
                                val updatedUserItem = if (name != null) {
                                    userItem.copy(name = name)
                                } else {
                                    userItem.copy(name = "Unknown")
                                }
                                updatedList.add(updatedUserItem)
                                if (updatedList.size == _listFollowing.value?.size) {
                                    _listFollowing.value = updatedList
                                }
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
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
        private const val TAG = "DetailVM"
    }

}