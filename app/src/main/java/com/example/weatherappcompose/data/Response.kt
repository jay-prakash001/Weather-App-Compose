package com.example.weatherappcompose.data

sealed class Response<T>( val data: T? = null, val msg: String? = null) {
    class Error<T>( data :T? = null,  msg: String= "Error" ) : Response<T>(data = data, msg = msg)
//    class Loading<T>(data: T? = null, msg: String? = "Loading"):Response<T>(data = data, msg = msg)
//    class Success<T>(data: T, msg: String?= "success") :Response<T>(data =data, msg =msg)
//
class Success<T> (data: T?) : Response<T>(data)

}

