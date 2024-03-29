package com.hwido.pieceofdayfront.BluetoothClient.net

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*

@SuppressLint("MissingPermission")
class BluetoothServer {

    private var btAdapter = BluetoothAdapter.getDefaultAdapter()
    private var acceptThread: AcceptThread? = null
    private var commThread: CommThread? = null
    private var inputStream: InputStream? = null
    private var outputStream: OutputStream? = null
    private var socketListener: SocketListenerServer? = null

    //콜백 메소드
    fun setOnSocketListener(listener: SocketListenerServer?) {
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

    fun accept() {
        stop()

        //여기서 적는 내용이 있어야 한다
        acceptThread = AcceptThread()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                onLogPrint("Waiting for accept the client..\n")
                acceptThread?.start()
            }
        }, 500)
    }

    fun stop() {
        if (acceptThread == null) return

        try {
            acceptThread?.let {
                onLogPrint("Stop accepting")

                it.stopThread()
                it.join(500)
                it.interrupt()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class AcceptThread : Thread() {
        private var acceptSocket: BluetoothServerSocket? = null
        private var socket: BluetoothSocket? = null

        override fun run() {
            while (true) {
                socket = try {
                    acceptSocket?.accept()
                } catch (e: Exception) {
                    e.printStackTrace()
                    break
                }

                if (socket != null) {
                    onConnect()

                    commThread = CommThread(socket)
                    commThread?.start()
                    break
                }
            }
        }

        fun stopThread() {
            try {
                acceptSocket?.close()
                socket?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        init {
            try {
                acceptSocket = btAdapter.listenUsingRfcommWithServiceRecord(
                    "bluetoothTest",
                    BTConstantServer.BLUETOOTH_UUID_INSECURE
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

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
                    accept()
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
                    stop()
                }
            }
        }.start()
    }
}