package com.kg.mobilecomapp.common

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading : Boolean = true) : Resource<T>(data = null)
}
/*
 It is a sealed class that represents a network response.
 It has three subclasses: Success, Error, and Loading. The Success class holds the data,
 the Error class holds the error message, and the Loading class is used to indicate that the data is still loading.
 This pattern is commonly used in Android development to handle network responses in a type-safe way.
*/