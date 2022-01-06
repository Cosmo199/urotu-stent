package com.example.medicomgmester.ui.chat

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicomgmester.databinding.FragmentChatBinding
import com.example.medicomgmester.model.ListChat
import com.example.medicomgmester.model.ListMessage
import com.example.medicomgmester.model.RememberToken
import com.example.medicomgmester.model.SendChat
import com.example.medicomgmester.network.ApiService
import com.example.medicomgmester.ui.chat.adapter.AdapterChatList
import kotlinx.android.synthetic.main.activity_chat.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apiService = ApiService()
        val preferences = context?.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        var getToken: String? = preferences?.getString("remember_token", "ไม่มี Token")
        callApi(getToken)
        sendMessage(getToken)
        super.onViewCreated(view, savedInstanceState)


    }

    private fun callApi(token: String?) {
        val call = apiService.chatListCall(RememberToken(token))
        call.enqueue(object : Callback<ListChat> {
            override fun onFailure(call: Call<ListChat>, t: Throwable) {}
            override fun onResponse(call: Call<ListChat>, response: Response<ListChat>) {
                val data = response.body()
                val fd: AdapterChatList by lazy { AdapterChatList(listOf()) }
                list_meg?.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                list_meg?.isNestedScrollingEnabled = false
                list_meg?.adapter = fd
                data?.results?.let { fd.setItem(it) }
                data?.results?.size?.minus(1)?.let { list_meg.scrollToPosition(it) }
                val handler = Handler()
                handler.postDelayed({
                    autoReload()
                }, 10000)
            }
        })
    }

    private fun sendMessage(token: String?) {
        btnSend.setOnClickListener {
            when (text_input.text.toString()) {
                "" -> {
                    Toast.makeText(context, "กรุณากรอกข้อความ", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val data: String = text_input.text.toString()
                    callApiSendMessage(token, data)
                }
            }
        }
    }

    private fun callApiSendMessage(token: String?, editMessage: String?) {
        val call = apiService.sendMessage(SendChat(token, editMessage))
        call.enqueue(object : Callback<ListMessage> {
            override fun onFailure(call: Call<ListMessage>, t: Throwable) {}

            override fun onResponse(call: Call<ListMessage>, response: Response<ListMessage>) {
                if (response.isSuccessful) {
                    text_input.setText("")
                    val preferences =
                        context?.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
                    var getToken: String? = preferences?.getString("remember_token", "ไม่มี Token")
                    callApi(getToken)
                    val handler = Handler()
                    handler.postDelayed({
                        autoReload()
                    }, 10000)
                }
            }
        })
    }

    private fun autoReload() {
        val preferences = context?.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        var getToken: String? = preferences?.getString("remember_token", "ไม่มี Token")
        callApi(getToken)
    }

    private fun loop(action: () -> Unit) {
        while (true)
            action()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}