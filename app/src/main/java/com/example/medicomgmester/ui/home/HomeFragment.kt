package com.example.medicomgmester.ui.home
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicomgmester.LoginActivity
import com.example.medicomgmester.databinding.FragmentHomeBinding
import com.example.medicomgmester.model.ListAppointment
import com.example.medicomgmester.model.RememberToken
import com.example.medicomgmester.network.ApiService
import com.example.medicomgmester.notification.Utils
import com.example.medicomgmester.ui.home.adapter.AdapterListHome
import com.tommasoberlose.progressdialog.ProgressDialogFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.load_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        apiService = ApiService()
        val preferences = this.activity?.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)
        var getToken: String? = preferences?.getString("remember_token", "ไม่มี Token")
        text_list.visibility = View.GONE
        when (getToken) {
            "tester_token" -> {
                load_activity.visibility = View.GONE
                text_list.visibility = View.VISIBLE
            }
            else -> { callApi(getToken) }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun callApi(text:String?){
        val call = apiService.appointmentCall(RememberToken(text))
        call.enqueue(object : Callback<ListAppointment> {
            override fun onFailure(call: Call<ListAppointment>, t: Throwable) {
                intentOnClick()
            }
            override fun onResponse(call: Call<ListAppointment>, response: Response<ListAppointment>) {
                val data = response.body()
                if(data?.results?.size == 0){
                    load_activity.visibility = View.GONE
                    text_list.visibility = View.VISIBLE
                } else {
                    load_activity.visibility = View.GONE
                    text_list.visibility = View.GONE
                    val fd: AdapterListHome by lazy { AdapterListHome(listOf()) }
                    list_data_appointment?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    list_data_appointment?.isNestedScrollingEnabled = false
                    list_data_appointment?.adapter = fd
                    data?.results?.let { fd.setItem(it) }
                    setTimeNotificationDefault()
                }

            }
        })
    }

    private fun intentOnClick() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity?.startActivity(intent)
    }

    private fun setTimeNotificationDefault() {
        /*preferencesTimeHolder*/
        val preferencesTimeHolder = context?.getSharedPreferences("TIME_HOLDER", Context.MODE_PRIVATE)
        var getTwoDateIn: String? = preferencesTimeHolder?.getString("twoDateIn", "noDate")
        //var getSevenDateIn: String? = preferencesTimeHolder?.getString("sevenDateIn", "noDate")
        var getInsertTime: String? = preferencesTimeHolder?.getString("timeIn", "noTime")
        var getTwoDateOut: String? = preferencesTimeHolder?.getString("twoDateOut", "noDate")
        //var getSevenDateOut: String? = preferencesTimeHolder?.getString("sevenDateOut", "noDate")
        var getOutTime: String? = preferencesTimeHolder?.getString("timeOut", "noTime")

       /*Check getIn*/
        if (getTwoDateIn.equals("noDate")) {

        }  else {
            var timeInMilliSecondsTwoDateIn: Long = 0
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val twoDateInData = sdf.parse(getTwoDateIn+ getInsertTime)
            //val sevenDateInData = sdf.parse(getSevenDateIn+ getInsertTime)
            timeInMilliSecondsTwoDateIn = twoDateInData.time
            context?.let { Utils.inTwoDayAdvanceNotice(it, timeInMilliSecondsTwoDateIn) }
           //context?.let { Utils.inSevenDayAdvanceNotice(it, timeInMilliSeconds) }
        }

        /*Check getOut*/
        if (getTwoDateOut.equals("noDate")) {

        }  else {
            var timeInMilliSecondsTwoDateOut: Long = 0
            val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            val twoDateOutData = sdf.parse(getTwoDateOut+ getOutTime)
            //val sevenDateOutData = sdf.parse(getSevenDateOut+ getInsertTime)
            timeInMilliSecondsTwoDateOut = twoDateOutData.time
            context?.let { Utils.outTwoDayAdvanceNotice(it, timeInMilliSecondsTwoDateOut ) }
            //context?.let { Utils.outSevenDayAdvanceNotice(it, timeInMilliSeconds) }
        }
    }
}