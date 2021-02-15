package com.sundaymobility.testsagar.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.sundaymobility.testsagar.R
import com.sundaymobility.testsagar.data.model.User
import com.sundaymobility.testsagar.ui.main.adapter.MainAdapter
import com.sundaymobility.testsagar.ui.main.viewmodel.MainViewModel
import com.sundaymobility.testsagar.utils.Extensions.addOnScrolledToEnd
import com.sundaymobility.testsagar.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private var isLoading = false
    private var totalPages = 1 // default
    private var currentPage = 1 // default

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupObserver()
        isLoading = true
        mainViewModel.fetchUsers(currentPage)
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = MainAdapter(arrayListOf())
        recyclerView.adapter = adapter

        recyclerView.addOnScrolledToEnd {
            //What you want to do once the end is reached
            if (totalPages > currentPage) {
                progressBar.isVisible = true
                isLoading = true
                currentPage += 1
                // mocking network delay for API call
                Handler(Looper.getMainLooper()).postDelayed( {
                    mainViewModel.fetchUsers(currentPage)
                }, 2000)
            }
        }


    }

    private fun setupObserver() {
        mainViewModel.users.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.isVisible = false
                    it.data?.let { result ->
                        totalPages = result.totalPages ?: 1
                        renderList(result.users ?: ArrayList())
                    }
                    recyclerView.isVisible = true
                    isLoading = false
                }
                Status.LOADING -> {
                    progressBar.isVisible = true
                    recyclerView.isVisible = false
                    isLoading = false
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.isVisible = false
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    isLoading = false
                }
            }
        })
    }

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }
}
