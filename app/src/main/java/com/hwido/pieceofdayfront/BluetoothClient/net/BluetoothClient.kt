package com.hwido.pieceofdayfront.BluetoothClient.net

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

@SuppressLint("MissingPermission")
class BluetoothClient {

    private var btAdapter = BluetoothAdapter.getDefaultAdapter()
    private var clientThread: ClientThread? = null
    private var commThread: CommThread? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var socketListener: SocketListener? = null

    fun setOnSocketListener(listener: SocketListener?) {
        socketListener = listener
    }

    fun onConnect() {
        socketListener?.onConnect()
    }

    fun onDisconnect() {
        socketListener?.onDisconnect()
    }

    fun onLogPrint(message: String?) {
        socketListener?.onLogPrint(message)
    }

    fun onError(e: Exception) {
        socketListener?.onError(e)
    }

    fun onReceive(msg: String) {
        socketListener?.onReceive(msg)
    }

    fun onSend(msg: String) {
        socketListener?.onSend(msg)
    }

    fun getPairedDevices(): ArrayList<BluetoothDevice> {
        val deviceList = ArrayList<BluetoothDevice>()
        val pairedDevices = btAdapter.bondedDevices
        if (pairedDevices.size > 0) {
            for (device in pairedDevices) {
                deviceList.add(device)
            }
        }
        return deviceList
    }

    fun connectToServer(device: BluetoothDevice) {
        disconnectFromServer()
        if (clientThread != null) {
            clientThread?.stopThread()
        }

        onLogPrint("Connect to server.")
        clientThread = ClientThread(device)
        clientThread?.start()
    }

    fun disconnectFromServer() {
        if (clientThread == null) return

        try {
            clientThread?.let {
                onDisconnect();

                it.stopThread()
                it.join(1000)
                it.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ClientThread(device: BluetoothDevice) : Thread() {
        private var socket: BluetoothSocket? = null

        override fun run() {
            btAdapter.cancelDiscovery()
//            try {
//                onLogPrint("Try to connect to server..")
//                socket?.connect()
//            } catch (e: Exception) {
//                onError(e)
//
//                e.printStackTrace()
//                disconnectFromServer()
//            }

            var attempts = 0
            val maxAttempts = 3 // 연결 시도 횟수
            val delayBetweenAttempts = 5000L // 연결 시도 사이의 지연 시간 (밀리초)

            while (attempts < maxAttempts) {
                try {
                    onLogPrint("Try to connect to server. Attempt ${attempts + 1}")
                    socket?.connect()
                    onConnect()
                    commThread = CommThread(socket)
                    commThread?.start()
                    return // 연결 성공 시 루프 종료
                } catch (e: Exception) {
                    onError(e)
                    e.printStackTrace()
                    attempts++
                    if (attempts >= maxAttempts) {
                        onLogPrint("Failed to connect after $maxAttempts attempts")
                        disconnectFromServer()
                        return // 연결 실패 시 루프 종료
                    }
                    // 재시도 전에 딜레이
                    try {
                        sleep(delayBetweenAttempts)
                    } catch (ie: InterruptedException) {
                        ie.printStackTrace()
                    }
                }
            }

            if (socket != null) {
                onConnect()

                commThread = CommThread(socket)
                commThread?.start()
            }
        }

        fun stopThread() {
            try {
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        init {
            try {
                socket =
                    device.createRfcommSocketToServiceRecord(BTConstantServer.BLUETOOTH_UUID_INSECURE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    //보낸다 받는다
    internal inner class CommThread(private val socket: BluetoothSocket?) : Thread() {

        override fun run() {
            try {
                outputStream = socket?.outputStream
                inputStream = socket?.inputStream
            } catch (e: Exception) {
                e.printStackTrace()
            }

            var len: Int
            val buffer = ByteArray(1024)
            val byteArrayOutputStream = ByteArrayOutputStream()

            while (true) {
                try {
                    len = socket?.inputStream?.read(buffer)!!
                    val data = buffer.copyOf(len)
                    byteArrayOutputStream.write(data)

                    socket.inputStream?.available()?.let { available ->

                        if (available == 0) {
                            val dataByteArray = byteArrayOutputStream.toByteArray()
                            val dataString = String(dataByteArray)
                            onReceive(dataString)

                            byteArrayOutputStream.reset()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    stopThread()
                    disconnectFromServer()
                    break
                }
            }
        }

        fun stopThread() {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //데이터 보내는 부분
    //그런데 안할 거임
    fun sendData(msg: String) {
        if (outputStream == null) return

        object : Thread() {
            override fun run() {
                try {
                    outputStream?.let {
                        onSend(msg)

                        it.write(msg.toByteArray())
                        it.flush()
                    }
                } catch (e: Exception) {
                    onError(e)
                    e.printStackTrace()
                    disconnectFromServer()
                }
            }
        }.start()
    }
}