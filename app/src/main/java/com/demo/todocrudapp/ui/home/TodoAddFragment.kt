package com.demo.todocrudapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.databinding.FragmentTodoAddBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoAddFragment : Fragment() {

    private var todo_Addbinding: FragmentTodoAddBinding? = null
    private val binding get() = todo_Addbinding!!
    private val viewModel: TodoViewModel by activityViewModels()

    private val navigationArgs: TodoAddFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        todo_Addbinding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar(title = navigationArgs.title)
        val todos = navigationArgs.todos
        if(todos.id != ""){
            bind(todos)
        } else {
            binding.btnSaveTodo.setOnClickListener {
                addNewTodo()
            }
        }

    }

    private fun addNewTodo(){
        binding.apply {
            if (txtEnterTodo.text.toString().isEmpty()) {
                txtEnterTodo.text = null
            } else {
                val todoName = txtEnterTodo.text.toString()
                viewModel.insertTodo(todoName)
                findNavController().navigateUp()
            }
        }
    }

    private fun bind(todo: TodoTasks) {
        binding.apply {
            txtEnterTodo.setText(todo.name)
            btnSaveTodo.setOnClickListener {
                viewModel.updateTodo(
                    TodoTasks(
                        id = todo.id,
                        name = txtEnterTodo.text.toString(),
                        done = todo.done
                    )
                )
                findNavController().navigateUp()
            }
        }
    }

    private fun setToolbar(title: String) {
        binding.todoAddFragmentToolbar.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        todo_Addbinding = null
    }
}