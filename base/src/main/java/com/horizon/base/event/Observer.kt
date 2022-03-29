package com.horizon.base.event

interface Observer {
    fun onEvent(event: Int, vararg args : Any?)
    fun listenEvents(): IntArray
}