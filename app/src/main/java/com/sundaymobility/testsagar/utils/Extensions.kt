package com.sundaymobility.testsagar.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Extensions {
    fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: () -> Unit) {

        this.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            var loading = true
            var pastVisibleItems: Int = 0
            var visibleItemCount: Int = 0
            var totalItemCount: Int = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                with(layoutManager as LinearLayoutManager) {
                    if (dy > 0) {//check for scroll down
                        visibleItemCount = childCount
                        totalItemCount = itemCount
                        pastVisibleItems = findFirstVisibleItemPosition()
                        if (loading) {
                            if (visibleItemCount + pastVisibleItems >= totalItemCount) {
                                loading = false
                                //Do pagination.. i.e. fetch new data
                                onScrolledToEnd()
                            }
                        }
                    }
                }
            }
        })
    }
}