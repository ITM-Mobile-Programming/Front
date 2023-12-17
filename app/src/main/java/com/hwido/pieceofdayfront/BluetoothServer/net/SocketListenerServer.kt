package com.hwido.pieceofdayfront.BluetoothClient.net

interface SocketListenerServer {
    fun onConnect()
    fun onDisconnect()
    fun onError(e: Exception?)
    fun onReceive(msg: String?)
    fun onSend(msg: String?)

    fun onLogPrint(msg: String?)
}