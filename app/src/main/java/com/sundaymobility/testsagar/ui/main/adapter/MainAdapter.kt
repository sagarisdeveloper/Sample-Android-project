package com.sundaymobility.testsagar.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sundaymobility.testsagar.R
import com.sundaymobility.testsagar.data.model.User
import kotlinx.android.synthetic.main.item_layout.view.*

class MainAdapter(private var users: MutableList<User>, val listener: OnItemClickListener) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User, listener: OnItemClickListener) {
            itemView.textViewUserID.text = "User ID - ${user.id}"
            itemView.textViewUserName.text = user.getName()
            itemView.textViewUserEmail.text = user.email

            Glide.with(itemView.imageViewAvatar.context)
                .load(user.avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(itemView.imageViewAvatar)

            itemView.setOnClickListener {
                listener.onClick(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DataViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
    )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(users[position], listener)

    fun addData(list: List<User>) {
        users.addAll(list)
        /**
         * Due to Dark/Light mode Adapter state is getting loss
         */
        users = users.distinctBy { it.id }.sortedBy { it.id }.toMutableList()
    }

    interface OnItemClickListener {
        fun onClick(user: User)
    }
}