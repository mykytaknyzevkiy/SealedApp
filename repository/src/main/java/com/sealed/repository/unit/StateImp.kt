package com.sealed.repository.unit

sealed class StateImp<T> {
    class Loading<T>: StateImp<T>()

    data class Data<T>(val value: T): StateImp<T>()
}