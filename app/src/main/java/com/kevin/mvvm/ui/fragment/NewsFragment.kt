package com.kevin.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kevin.mvvm.ui.adapter.NewsAdapter
import com.kevin.mvvm.databinding.FragmentNewsBinding
import com.kevin.mvvm.viewmodel.NewsViewModel

class NewsFragment : BaseFragment() {

    private lateinit var viewModel: NewsViewModel

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        _binding = FragmentNewsBinding.inflate(inflater,container,false)

        //获取新闻数据
        binding.rv.layoutManager = LinearLayoutManager(context)
        //数据刷新
        viewModel.news.observe(viewLifecycleOwner){ result->
            val x = result.getOrNull()
            if(x!=null) binding.rv.adapter = NewsAdapter(x.result?.data!!)
        }

        return  binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}