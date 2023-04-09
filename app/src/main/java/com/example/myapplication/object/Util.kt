package com.example.myapplication.`object`

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.UserAdapter
import com.example.myapplication.response.ItemsItem

object Util {
    fun showLoading(isLoading: Boolean, progressBar: ProgressBar) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setUserData(User: List<ItemsItem>, recyclerView: RecyclerView, context: Context) {
        val adapter = UserAdapter(User)
        recyclerView.adapter = adapter
    }
}
