package com.example.cursoandroidclase2.uid.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cursoandroidclase2.R
import com.example.cursoandroidclase2.data.model.Post
class PostAdapter(
    private val onDeleteClick: ((Post) -> Unit)? = null,
    private val onEditClick: ((Post) -> Unit)? = null
) : ListAdapter<Post, PostAdapter.ViewHolder>(DiffCallback()) {



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val text = view.findViewById<TextView>(R.id.titleTextView)
        private val body = view.findViewById<TextView>(R.id.bodyTextView)
        private val deleteBtn = view.findViewById<Button>(R.id.deleteButton)
        private val inputBtn = view.findViewById<Button>(R.id.inputButton)

        fun bind(post: Post, onDeleteClick: ((Post) -> Unit)?, onEditClick: ((Post) -> Unit)?) {
            text.text = post.title
            body.text = post.body
            deleteBtn.setOnClickListener {
                onDeleteClick?.invoke(post)
            }
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailActivity::class.java).apply {
                    putExtra("title", post.title)
                    putExtra("body", post.body)
                }
                context.startActivity(intent)
                if (context is Activity) {
                    // context.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    // context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    context.overridePendingTransition(R.anim.zoom_fade_in, R.anim.zoom_fade_out)
                }
            }
            inputBtn.setOnClickListener {
//                val context = itemView.context
//                val intent = Intent(context, InputActivity::class.java).apply {
//                    putExtra("title", post.title)
//                    putExtra("body", post.body)
//                }
//                context.startActivity(intent)
                onEditClick?.invoke(post)
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //holder.bind(getItem(position))
        val post = getItem(position)
        holder.bind(post, onDeleteClick, onEditClick)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return ViewHolder(view)
    }
    class DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }

}