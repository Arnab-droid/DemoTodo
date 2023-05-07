package com.demo.todocrudapp.data.network.model

import java.io.Serializable

data class TodoDetailTasks(
    val task_id: String = "",
    val name: String = "",
    val titleDetails:String = "",
    val time: String ="",
    val createdBy: User = User(),
    val type: Int = 0,
): Serializable