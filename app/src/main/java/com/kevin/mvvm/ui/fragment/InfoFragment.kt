package com.kevin.mvvm.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.InfoFragmentBinding
import com.kevin.mvvm.ui.adapter.InfoFragmentAdapter

class InfoFragment : BaseFragment() {
    private var binding: InfoFragmentBinding? = null

    /**
     * 标题数组
     */
    private val titles = arrayOf("新闻", "视频")
    private val fragmentList: MutableList<Fragment> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.info_fragment, container, false)
        return binding!!.root
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fragmentList.add(NewsFragment())
        fragmentList.add(VideoFragment())
        binding!!.vp.adapter = InfoFragmentAdapter(childFragmentManager, fragmentList, titles)
        binding!!.tab.setupWithViewPager(binding!!.vp)
    }

}
