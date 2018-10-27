package com.albertjsoft.portainerapp.data.api

/**
 * Created by albertj on 18/10/2018.
 */
data class Resource<ResultType>(var status: Status, var data: ResultType? = null, var message: String? = null) {

    companion object {
        fun <ResultType> success(data: ResultType): Resource<ResultType> = Resource(Status.SUCCESS, data = data)

        fun <ResultType> loading(): Resource<ResultType> = Resource(Status.LOADING)

        fun <ResultType> error(message: String?): Resource<ResultType> = Resource(Status.ERROR, message = message)
    }
}