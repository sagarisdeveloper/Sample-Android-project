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
import com.sundaymobility.testsagar.utils.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private var currentPage = 1 // default
    private var mainUserList: MutableList<User> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupObserver()
        progressBar.isVisible = true
        mainViewModel.fetchUsers(currentPage)
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(this, mainUserList, object : MainAdapter.OnItemClickListener {
            override fun onClick(user: User) =
                BottomSheetDialog(user).show(supportFragmentManager, "DetailBottomSheet")
        })

        adapter.loadMoreListener = object : MainAdapter.OnLoadMoreListener {
            override fun onLoadMore() {
                recyclerView.post {
                    currentPage += 1
                    mainUserList.add(User());
                    adapter.notifyItemInserted(mainUserList.size - 1);

                    // mocking network delay for API call
                    Handler(Looper.getMainLooper()).postDelayed({
                        mainViewModel.fetchUsers(
                            currentPage
                        )
                    }, 2000)
                }
                // Calling loadMore function in Runnable to fix the
                // java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling error
            }
        }

        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        mainViewModel.users.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {//remove loading view
                    hideLoader()
                    it.data?.let { renderList(it.users ?: ArrayList()) }
                    recyclerView.isVisible = true
                }
                Status.ERROR -> { //Handle Error
                    hideLoader()
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                }
            }
        })
    }

    private fun hideLoader() {
        progressBar.isVisible = false
        if (mainUserList.isNotEmpty()) mainUserList.removeAt(mainUserList.size - 1)
    }

    private fun renderList(users: List<User>) {
        if (users.isNotEmpty()) { //add loaded data
            mainUserList.addAll(users)
        } else { //result size 0 means there is no more data available at server
            adapter.isMoreDataAvailable = false
            //telling adapter to stop calling load more as no more server data available
            Toast.makeText(this, "No More Data Available", Toast.LENGTH_LONG).show()
        }
        adapter.notifyDataChanged() //should call the custom method adapter.notifyDataChanged here to get the correct loading status
    }
}
