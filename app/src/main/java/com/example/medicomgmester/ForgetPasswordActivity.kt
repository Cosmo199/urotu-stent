package com.example.medicomgmester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.awesomedialog.*
import com.example.medicomgmester.model.ForgetPassword
import com.example.medicomgmester.model.ListForgetPassword
import com.example.medicomgmester.network.ApiService
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import kotlinx.android.synthetic.main.activity_forget_password.*
import kotlinx.android.synthetic.main.view_action_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        apiService = ApiService()
        setEvent()
        setButtonEvent()
    }

    private fun setEvent(){
        text_bar.text = "ลืมรหัสผ่าน"
        arrow_back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setButtonEvent(){
        btnSaveProfile.setOnClickListener {
            val userName: String = user_name.text.toString()
            val userPhone: String = enter_phone.text.toString()
            val newPassword: String = new_password.text.toString()
            when {
                userName == "" -> {
                    showMessageFail()
                }
                userPhone == "" -> {
                    showMessageFail()
                }
                newPassword == "" -> {
                    showMessageFail()
                }
                else -> {
                    callApi(userPhone,userName,newPassword)
                }
            }
        }
    }

    private fun callApi(userPhone: String,userName: String,newPassword: String) {
        val call = apiService.forgetPassword(ForgetPassword(userPhone, userName,newPassword))
        call.enqueue(object : Callback<ListForgetPassword> {
            override fun onFailure(call: Call<ListForgetPassword>, t: Throwable) {
                    showMessageFail()
            }
            override fun onResponse(call: Call<ListForgetPassword>, response: Response<ListForgetPassword>) {
                if (response.isSuccessful) {
                    val list = response.body()
                    when (list?.results?.get(0)?.message) {
                        "SUCESS" -> {
                            showMessagePass()
                        }
                        else -> {
                            showMessageFail()
                        }
                    }
                }
            }
        })
    }

    private fun showMessageFail() {
        ProgressDialogFragment.hideProgressBar(this)
        AwesomeDialog.build(this)
            .title("เปลี่ยนรหัสผ่านไม่สำเร็จ")
            .position(AwesomeDialog.POSITIONS.CENTER)
            .body("กรุณากรอกข้อมูลให้ถูกต้อง")
            .onNegative(
                "ปิด",
                buttonBackgroundColor = R.drawable.side_button,
            ) {}
    }

    private fun showMessagePass() {
        ProgressDialogFragment.hideProgressBar(this)
        AwesomeDialog.build(this)
            .title("เปลี่ยนรหัสผ่านสำเร็จ")
            .position(AwesomeDialog.POSITIONS.CENTER)
            .body("คุณได้เปลี่ยนรหัสผ่านสำเร็จ")
            .onNegative(
                "ปิด",
                buttonBackgroundColor = R.drawable.side_button,
            ) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
    }
}