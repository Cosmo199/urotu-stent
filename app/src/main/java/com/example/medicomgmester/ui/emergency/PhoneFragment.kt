package com.example.medicomgmester.ui.emergency

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.medicomgmester.JsonMockUtility
import com.example.medicomgmester.databinding.FragmentPhoneBinding
import com.example.medicomgmester.model.ListEmergency
import com.example.medicomgmester.ui.emergency.adapter.AdapterListEmergency
import kotlinx.android.synthetic.main.activity_emergency.*

class PhoneFragment : Fragment() {

    private var _binding: FragmentPhoneBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhoneBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mockUpJson()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun mockUpJson(){
        JsonMockUtility().apply {
            val dataMock = getJsonToMock("emergency.json", ListEmergency::class.java)
            val en: AdapterListEmergency by lazy { AdapterListEmergency(listOf()) }
            list_emergency?.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            list_emergency?.isNestedScrollingEnabled = false
            list_emergency?.adapter = en
            dataMock.results?.let { en.setItem(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}