package com.demo.todocrudapp.ui.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.databinding.TodoViewBinding
import java.text.DateFormat
import java.text.DateFormat.getDateTimeInstance
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter(private val listener: TodoFragment):
    ListAdapter<TodoTasks, TodoAdapter.TodoViewHolder>(DiffCallback) {

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoTasks>() {
            override fun areItemsTheSame(oldItem: TodoTasks, newItem: TodoTasks): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoTasks, newItem: TodoTasks): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class TodoViewHolder(private val binding: TodoViewBinding) : RecyclerView.ViewHolder(binding.root) {

        private var currTodo: TodoTasks? = null

        init{
            binding.apply{
                chkIsDone.setOnClickListener {
                    currTodo?.let { todo ->

                        if (chkIsDone.isChecked) {
                            listener.onTodoUpdate(
                                TodoTasks(id = todo.id, name = todo.name, done = true)
                            )
                        } else {
                            listener.onTodoUpdate(
                                TodoTasks(id = todo.id, name = todo.name, done = false)
                            )
                        }
                    }

                }
                root.rootView.setOnClickListener{
                    currTodo?.let { todo ->
                        listener.moveToDetails(todo)

                    }

                }
                root.rootView.setOnLongClickListener {
                    currTodo?.let { todo ->
                        listener.callTodoDialog(todo)
                    }
                    true
                }
            }
        }

        fun getTimeDate(timestamp: String): String? {
            return try {
                val dateFormat: DateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val netDate = Date(timestamp)
                dateFormat.format(netDate)
            } catch (e: Exception) {
                "date"
            }
        }
        fun bind(todo: TodoTasks){
            currTodo = todo
            binding.apply {
                tvTodo.text = todo.name
                //tvTime.text = getTimeDate(todo.time)
                chkIsDone.isChecked = todo.done
                if(todo.done){
                    tvTodo.paintFlags = tvTodo.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
                else{
                    tvTodo.paintFlags = 0
                }


            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = TodoViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currTodo = getItem(position)
        holder.bind(currTodo)
    }
}
interface TodoEvents{
    fun onTodoUpdate(todo: TodoTasks)
    fun callTodoDialog(todo: TodoTasks)
    fun moveToDetails(todo: TodoTasks)
}