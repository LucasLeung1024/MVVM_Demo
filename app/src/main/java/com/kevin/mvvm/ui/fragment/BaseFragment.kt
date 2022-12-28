package com.kevin.mvvm.ui.fragment

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kevin.mvvm.ui.view.dialog.LoadingDialog




open class BaseFragment : Fragment() {
    protected var context: AppCompatActivity? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            this.context = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        context = null
    }

    protected fun showMsg(msg: String?) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    private var loadingDialog: LoadingDialog? = null

    /**
     * 显示加载弹窗
     */
    protected open fun showLoading() {
        loadingDialog = LoadingDialog(context)
        loadingDialog!!.show()
    }

    /**
     * 显示加载弹窗
     *
     * @param isClose true 则点击其他区域弹窗关闭， false 不关闭。
     */
    protected open fun showLoading(isClose: Boolean) {
        loadingDialog = LoadingDialog(context, isClose)
    }

    /**
     * 隐藏加载弹窗
     */
    protected open fun dismissLoading() {
        if (loadingDialog != null) {
            loadingDialog!!.dismiss()
        }
    }

    protected open fun isNight(): Boolean {
        val uiModeManager = requireContext().getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
    }

}

