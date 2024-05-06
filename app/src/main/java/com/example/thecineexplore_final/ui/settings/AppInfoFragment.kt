package com.example.thecineexplore_final.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.thecineexplore_final.databinding.FragmentAppInfoBinding

class AppInfoFragment : Fragment() {

    private var _binding: FragmentAppInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val appInfoViewModel = ViewModelProvider(this).get(AppInfoViewModel::class.java)

        _binding = FragmentAppInfoBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSlideshow
        appInfoViewModel.text.observe(viewLifecycleOwner, { textView.text = it })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}