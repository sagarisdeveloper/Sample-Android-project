package com.sundaymobility.testsagar.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object Extensions {
    fun RecyclerView.addOnScrolledToEnd(onScrolledToEnd: () -> Unit){

        this.addOnScrollListener(object: RecyclerView.OnScrollListener(){

            private val VISIBLE_THRESHOLD = 6

            private var loading = true
            private var previousTotal = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                with(layoutManager as LinearLayoutManager){

                    val visibleItemCount = childCount
                    val totalItemCount = itemCount
                    val firstVisibleItem = findFirstVisibleItemPosition()

                    if (loading && totalItemCount > previousTotal){

                        loading = false
                        previousTotal = totalItemCount
                    }

                    if(!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)){

                        onScrolledToEnd()
                        loading = true
                    }
                }
            }
        })
    }
}