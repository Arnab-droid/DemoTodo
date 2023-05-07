package com.demo.todocrudapp.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.todocrudapp.R
import com.demo.todocrudapp.data.network.model.Response
import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.databinding.FragmentTodoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoEvents {

    private var todoFragBinding: FragmentTodoBinding? = null
    private val binding get() = todoFragBinding!!
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val viewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todoFragBinding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
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
    private fun String.toDate(): Date {
        return SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault()).parse(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var listofDataSorted = listOf<TodoTasks>()
        setToolbar()
        setRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            if (response.data.isEmpty()) {
                                binding.tvEmptyList.visibility = View.VISIBLE
                            } else {
                                val listData=response.data
//String formated to Long and sorted for date used
                                listofDataSorted= listData.sortedByDescending {
                                    getTimeDate(it.time)?.toDate()
                                }
                                todoAdapter.submitList(listofDataSorted)
                                binding.tvEmptyList.visibility = View.GONE
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currTodo = todoAdapter.currentList[position]
                viewModel.deleteTodo(currTodo.id)
                Snackbar.make(view, "Article deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.insertTodo(currTodo.name)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(listRecyclerView)
        }

        binding.btnAddTodo.setOnClickListener {
            val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
                getString(R.string.add), TodoTasks()
            )
            findNavController().navigate(action)
        }
    }

    private fun showTodoDialog(todo: TodoTasks) {
        val options = arrayOf("Edit","Delete")
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setItems(options){ _,which ->
                when(options[which]){
                    "Edit" -> onTodoEdit(todo)
                    "Delete" -> viewModel.deleteTodo(todo.id)
                }
            }
            .show()
    }

    private fun setRecyclerView(){
        todoAdapter = TodoAdapter(this)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.apply{
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun setToolbar() {
        binding.todoFragmentToolbar.title = findNavController().currentDestination?.label
    }

    private fun onTodoEdit(todos: TodoTasks) {
        val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
            getString(R.string.edit),todos
        )
        findNavController().navigate(action)
    }

    override fun onTodoUpdate(todo: TodoTasks) {
        viewModel.updateTodo(todo)
    }
    override fun callTodoDialog(todo: TodoTasks) {
        showTodoDialog(todo)
    }
    override fun moveToDetails(todo: TodoTasks) {
        Toast.makeText(context, "$todo", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        todoFragBinding = null
    }
}