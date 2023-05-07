package com.demo.todocrudapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.todocrudapp.data.network.model.Response
import com.demo.todocrudapp.data.network.model.TodoDetailTasks
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
    private val _todosDetails = MutableStateFlow<Response<TodoDetailTasks>>(Response.Loading)
    val todosDetails = _todosDetails.asStateFlow()
    init {
        viewModelScope.launch {
            todosUseCase.getTodos().collect{ todos ->
                _todos.value = todos
            }
        }
    }
    fun getTodoDetails(taskId:String)= viewModelScope.launch {
        todosUseCase.getTodoDetails(taskId).collect{ todosDetails ->
            _todosDetails.value = todosDetails as Response<TodoDetailTasks>
        }
    }
    fun insertTodo(todoName: String) = viewModelScope.launch {
        todosUseCase.insertTodo(todoName)
    }

    fun insertTodoDetails(todoId: String,todoName: String,todoDetails:String,txt_type:Int) = viewModelScope.launch {
        todosUseCase.insertTodoDetails(todoId,todoName,todoDetails,txt_type)
    }
    fun updateTodo(updatedTodo: TodoTasks) = viewModelScope.launch {
        todosUseCase.updateTodo(updatedTodo)
    }

    fun deleteTodo(todoId: String) = viewModelScope.launch {
        todosUseCase.deleteTodo(todoId)
    }

}