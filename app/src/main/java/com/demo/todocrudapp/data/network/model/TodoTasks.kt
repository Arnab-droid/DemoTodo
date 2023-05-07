package com.demo.todocrudapp.data.network.model

import java.io.Serializable

data class TodoTasks(
    val id: String = "",
    val name: String = "",
    val time: String ="",
    val createdBy: User = User(),
    val done: Boolean = false
):Serializable