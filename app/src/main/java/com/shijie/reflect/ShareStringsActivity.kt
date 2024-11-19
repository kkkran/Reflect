package com.shijie.reflect

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.shijie.reflect.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStream
import java.net.Socket

class ShareStringsActivity : BaseActivity(true) {

    private lateinit var etMessage: EditText
    private lateinit var etIpAddress: EditText
    private lateinit var etPort: EditText
    private lateinit var btnSend: Button
    private lateinit var btnClear: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        etMessage = findViewById(R.id.shareStrings_edit)
        etIpAddress = findViewById(R.id.shareStrings_ip)
        etPort = findViewById(R.id.shareStrings_port)
        btnSend = findViewById(R.id.shareStrings_send)
        btnClear = findViewById(R.id.shareStrings_clear)
        btnClear.setOnClickListener {
            if (etMessage.text.length != 0) {
                etMessage.setText("")
            } else {
                return@setOnClickListener
            }
        }
        btnSend.setOnClickListener {
            val message = etMessage.text.toString()
            val ipAddress = etIpAddress.text.toString()
            val port = etPort.text.toString().toInt()
            if (message.isNotBlank() && message.length <= 100 && ipAddress.isNotBlank() && port != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    sendMessage(message, ipAddress, port)
                }

            }
        }
    }

    override fun getLayoutResId(): Int = R.layout.activity_sharestrings

    private fun sendMessage(message: String, ipAddress: String = "", port: Int = 0) {
        try {
            val socket = Socket(ipAddress, port)
            val outputStream: OutputStream = socket.getOutputStream()
            outputStream.write(message.toByteArray())
            outputStream.flush()
            socket.close()
            runOnUiThread {
                SimpleToast(this).createShortToast(content = "发送成功，请在PC端查收消息")
            }
        } catch (e: Exception) {
            Log.e("ShareStringsActivity", "Error:${e.message}", e)
            e.printStackTrace()
            runOnUiThread {
                SimpleToast(this).createShortToast(content = "发送失败，Error:${e.message}")
            }
        }
    }
}