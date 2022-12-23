package com.kevin.mvvm.ui.adapter

import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class InfoFragmentAdapter(fm: FragmentManager?, list: List<Fragment>?, titleArr: Array<String>) :
    FragmentPagerAdapter(fm!!) {
    var titleArr: Array<String>
    var mFragmentList: List<Fragment>?

    init {
        mFragmentList = list
        this.titleArr = titleArr
    }

    override fun getItem(i: Int): Fragment {
        return mFragmentList!![i]
    }

    override fun getCount(): Int {
        return if (mFragmentList != null) mFragmentList!!.size else 0
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return titleArr[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, object);
    }
}
