package com.demo.todocrudapp.data.network.model

data class Auth(
    val password: String,
    val authGoogle: Boolean,
    val authEmail: Boolean
)