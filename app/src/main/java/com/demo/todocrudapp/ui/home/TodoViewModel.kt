package com.demo.todocrudapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.todocrudapp.data.network.model.Response
import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.domain.TodosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todosUseCase: TodosUseCase
) : ViewModel() {

    private val _todos = MutableStateFlow<Response<List<TodoTasks>>>(Response.Loading)
    val todos = _todos.asStateFlow()

    init {
        viewModelScope.launch {
            todosUseCase.getTodos().collect{ todos ->
                _todos.value = todos
            }
        }
    }

    fun insertTodo(todoName: String) = viewModelScope.launch {
        todosUseCase.insertTodo(todoName)
    }

    fun updateTodo(updatedTodo: TodoTasks) = viewModelScope.launch {
        todosUseCase.updateTodo(updatedTodo)
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todosUseCase.deleteTodo(todoId)
    }

}