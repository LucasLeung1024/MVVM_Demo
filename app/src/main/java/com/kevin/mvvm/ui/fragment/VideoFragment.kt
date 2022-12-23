package com.kevin.mvvm.ui.fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.mvvm.ui.adapter.VideoAdapter
import com.kevin.mvvm.databinding.FragmentVideoBinding
import com.kevin.mvvm.viewmodel.VideoViewModel

class VideoFragment : BaseFragment() {

    private lateinit var viewModel: VideoViewModel

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[VideoViewModel::class.java]
        _binding = FragmentVideoBinding.inflate(inflater,container,false)

        //获取Video数据
        binding.rv.layoutManager = LinearLayoutManager(context)
        //数据刷新
        viewModel.videos.observe(viewLifecycleOwner){ result->
            val x = result.getOrNull()
            if(x!=null) binding.rv.adapter = VideoAdapter(x.result!!)
        }

        return  binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}