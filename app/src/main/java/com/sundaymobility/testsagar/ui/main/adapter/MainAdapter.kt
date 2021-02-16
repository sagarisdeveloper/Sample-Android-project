package com.sundaymobility.testsagar.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sundaymobility.testsagar.R
import com.sundaymobility.testsagar.data.model.User
import kotlinx.android.synthetic.main.item_layout.view.*


class MainAdapter(
    private var context: Context,
    private var users: MutableList<User>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_DATA = 0
    val TYPE_LOAD = 1

    var loadMoreListener: OnLoadMoreListener? = null
    var isLoading = false
    var isMoreDataAvailable: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == TYPE_DATA) DataViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        )
        else LoadHolder(
            LayoutInflater.from(context).inflate(R.layout.item_loader_layout, parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position >= itemCount - 1 && isMoreDataAvailable && !isLoading && loadMoreListener != null) {
            isLoading = true
            loadMoreListener?.onLoadMore()
        }
        if (getItemViewType(position) == TYPE_DATA) (holder as DataViewHolder).bind(
            users[position],
            listener
        )
        //No else part needed as load holder doesn't bind any data
    }

    interface OnItemClickListener {
        fun onClick(user: User)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, listener: OnItemClickListener) {
            itemView.textViewUserID.text = "User ID - ${user.id}"
            itemView.textViewUserName.text = user.getName()
            itemView.textViewUserEmail.text = user.email

            Glide.with(itemView.imageViewAvatar.context)
                .load(user.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.imageViewAvatar)

            itemView.setOnClickListener { listener.onClick(user) }
        }
    }

    internal class LoadHolder(itemView: View?) : RecyclerView.ViewHolder(
        itemView!!
    )


    /** notifyDataSetChanged is final method so we can't override it call adapter.notifyDataChanged(); after update the list**/
    fun notifyDataChanged() {
        notifyDataSetChanged()
        isLoading = false
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    override fun getItemViewType(position: Int): Int =
        if ((users[position].id ?: "").equals("")) TYPE_LOAD else TYPE_DATA

}