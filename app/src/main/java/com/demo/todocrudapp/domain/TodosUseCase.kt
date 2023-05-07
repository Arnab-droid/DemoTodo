package com.demo.todocrudapp.domain

import com.demo.todocrudapp.data.network.model.TodoTasks
import com.demo.todocrudapp.data.repositories.TodoRepository
import javax.inject.Inject

class TodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
) {
    fun getTodos() = todoRepository.getTodos()

    suspend fun deleteTodo(todoId: String) = todoRepository.deleteTodo(todoId)

    suspend fun insertTodo(todoName: String) = todoRepository.insertTodo(todoName)

    suspend fun updateTodo(updatedTodo: TodoTasks) = todoRepository.updateTodo(updatedTodo)
}