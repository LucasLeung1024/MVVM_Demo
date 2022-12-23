package com.kevin.mvvm.ui.activity

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.core.os.EnvironmentCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kevin.mvvm.R
import com.kevin.mvvm.databinding.ActivityHomeBinding
import com.kevin.mvvm.databinding.DialogEditBinding
import com.kevin.mvvm.databinding.DialogModifyUserInfoBinding
import com.kevin.mvvm.databinding.NavHeaderBinding
import com.kevin.mvvm.db.bean.User
import com.kevin.mvvm.utils.*
import com.kevin.mvvm.utils.EasyDate.milliSecond
import com.kevin.mvvm.ui.view.dialog.AlertDialog
import com.kevin.mvvm.viewmodel.HomeViewModel
import java.io.File


class HomeActivity : BaseActivity() {

    private val TAG = HomeActivity::class.java.simpleName
    private var localUser: User? = null

    //可输入弹窗
    private var editDialog: AlertDialog? = null
    //修改用户信息弹窗
    private var modifyUserInfoDialog: AlertDialog? = null
    //是否显示修改头像的两种方式
    private var isShow = false
    //用于保存拍照图片的uri
    private var mCameraUri: Uri? = null
    // 用于保存图片的文件路径，Android 10以下使用图片路径访问图片
    private var mCameraImagePath: String? = null

    //viewBinding
    private lateinit var binding: ActivityHomeBinding
    private lateinit var headerBinding: NavHeaderBinding
    //ViewModel
    private val vm by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //显示加载弹窗
        showLoading()
        initView()
        requestLocation()
    }

    /**
     * 初始化
     */
    private fun initView() {
        //获取navController
        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.news_fragment -> {
                    binding.tvTitle.text = "头条新闻"
                    navController.navigate(R.id.news_fragment)
                }
                R.id.video_fragment -> {
                    binding.tvTitle.text = "热门视频"
                    navController.navigate(R.id.video_fragment)
                }
                R.id.map_fragment -> {
                    binding.tvTitle.text = "地图天气"
                    navController.navigate(R.id.map_fragment)
                }
                else -> {}
            }
            true
        }

        binding.ivAvatar.setOnClickListener { binding.drawerLayout.open() }
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_setting -> {}
                R.id.item_logout -> logout()
                else -> {}
            }
            true
        }

        //获取NavigationView的headerLayout视图
        val headerView = binding.navView.getHeaderView(0)
        headerBinding = NavHeaderBinding.bind(headerView)
        headerView.setOnClickListener { v -> showModifyUserInfoDialog() }
        //获取headerLayout视图的Binding
        val headerBinding: NavHeaderBinding = DataBindingUtil.bind(headerView)!!
        //获取本地用户信息
        vm.getUser()
        //用户信息发生改变时给对应的xml设置数据源也就是之前写好的ViewModel。
        vm.user!!.observe(this) { user ->
            localUser = user
            //修改头像
            if(localUser!!.getAvatar()!!.isNotEmpty()){
                binding.ivAvatar.setLocalUrl(binding.ivAvatar, localUser!!.getAvatar())
                headerBinding.ivAvatar.setLocalUrl(headerBinding.ivAvatar, localUser!!.getAvatar())
            }
            //修改昵称
            if(localUser!!.getNickname()!!.isNotEmpty()){
                headerBinding.tvName.text = localUser!!.getNickname()
            } else {
                headerBinding.tvName.text = vm.defaultName
            }
            //修改Introduction
            if(localUser!!.getIntroduction()!!.isNotEmpty()){
                headerBinding.tvTip.text = localUser!!.getIntroduction()
            } else {
                headerBinding.tvTip.text = vm.defaultIntroduction
            }
            //隐藏加载弹窗
            dismissLoading()
        }
    }

    /**
     * 显示修改用户弹窗
     */
    private fun showModifyUserInfoDialog() {
        val binding: DialogModifyUserInfoBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this),
                R.layout.dialog_modify_user_info,
                null,
                false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .addDefaultAnimation()
            .setCancelable(true)
            .setContentView(binding.root)
            .setWidthAndHeight(SizeUtils.dp2px(this, 300f), LinearLayout.LayoutParams.WRAP_CONTENT)
            .setOnClickListener(R.id.tv_modify_avatar, View.OnClickListener { v: View? ->
                //修改头像，点击显示修改头像的方式，再次点击隐藏修改方式
                binding.layModifyAvatar.visibility = if (isShow) View.GONE else View.VISIBLE
                isShow = !isShow
            })
            .setOnClickListener(R.id.tv_album_selection,
                View.OnClickListener { albumSelection() }) //相册选择
            .setOnClickListener(R.id.tv_camera_photo,
                View.OnClickListener { cameraPhoto() }) //相机拍照
            .setOnClickListener(R.id.tv_modify_nickname, View.OnClickListener {
                showEditDialog(0)
            }) //修改昵称
            .setOnClickListener(R.id.tv_modify_Introduction, View.OnClickListener {
                showEditDialog(1)
            }) //修改简介
            .setOnClickListener(R.id.tv_close,
                View.OnClickListener { modifyUserInfoDialog!!.dismiss() }) //关闭弹窗
            .setOnDismissListener(DialogInterface.OnDismissListener {
                isShow = false
            })
        modifyUserInfoDialog = builder.create()
        modifyUserInfoDialog!!.show()
    }

    /**
     * 显示可输入文字弹窗
     * @param type 0 修改昵称  1  修改简介
     */
    private fun showEditDialog(type: Int) {
        modifyUserInfoDialog!!.dismiss()
        val binding: DialogEditBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_edit, null, false)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            .addDefaultAnimation()
            .setCancelable(true)
            .setText(R.id.tv_title, if (type == 0) "修改昵称" else "修改简介")
            .setContentView(binding.root)
            .setWidthAndHeight(SizeUtils.dp2px(this, 300F), LinearLayout.LayoutParams.WRAP_CONTENT)
            .setOnClickListener(R.id.tv_cancel) { editDialog!!.dismiss() }
            .setOnClickListener(R.id.tv_sure) {
                val content: String = binding.etContent.text.toString().trim()
                if (content.isEmpty()) {
                    showMsg(if (type == 0) "请输入昵称" else "请输入简介")
                    return@setOnClickListener
                }
                if (type == 0 && content.length > 10) {
                    showMsg("昵称过长，请输入8个以内汉字或字母")
                    return@setOnClickListener
                }
                editDialog!!.dismiss()
                showLoading()
                //保存输入的值
                modifyContent(type, content)
            }
        editDialog = builder.create()
        binding.etContent.hint = if (type == 0) "请输入昵称" else "请输入简介"
        editDialog!!.show()
    }

    /**
     * 修改内容
     *
     * @param type    类型 0：昵称 1：简介 2: 头像
     * @param content 修改内容
     */
    private fun modifyContent(type: Int, content: String) {
        if (type == 0) {
            localUser!!.setNickname(content)
        } else if (type == 1) {
            localUser!!.setIntroduction(content)
        } else if (type == 2) {
            localUser!!.setAvatar(content)
        }
        vm.updateUser(localUser)
    }

    /**
     * 相册拍照
     */
    private fun cameraPhoto() {
        modifyUserInfoDialog!!.dismiss()
        if (!isAndroid6()) {
            //打开相机
            openCamera()
            return
        }
        if (!hasPermission(PermissionUtils.CAMERA)) {
            requestPermission(PermissionUtils.CAMERA)
            return
        }
        //打开相机
        openCamera()
    }

    /**
     * 调起相机拍照
     */
    private fun openCamera() {
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // 判断是否有相机
        if (captureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            var photoUri: Uri? = null
            if (isAndroid10()) {
                // 适配android 10 创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
                photoUri =
                    contentResolver.insert(if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) MediaStore.Images.Media.EXTERNAL_CONTENT_URI else MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                        ContentValues())
            } else {
                photoFile = createImageFile()
                if (photoFile != null) {
                    mCameraImagePath = photoFile.absolutePath
                    photoUri = if (isAndroid7()) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        FileProvider.getUriForFile(this, "$packageName.fileprovider", photoFile)
                    } else {
                        Uri.fromFile(photoFile)
                    }
                }
            }
            mCameraUri = photoUri
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                startActivityForResult(captureIntent, TAKE_PHOTO_CODE)
            }
        }
    }

    /**
     * 创建保存图片的文件
     */
    private fun createImageFile(): File? {
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!storageDir!!.exists()) {
            storageDir.mkdir()
        }
        val tempFile = File(storageDir, milliSecond + "")
        return if (Environment.MEDIA_MOUNTED != EnvironmentCompat.getStorageState(tempFile)) {
            null
        } else tempFile
    }

    /**
     * 相册选择
     */
    private fun albumSelection() {
        modifyUserInfoDialog!!.dismiss()
        if (isAndroid11()) {
            //请求打开外部存储管理
            requestManageExternalStorage()
        } else {
            if (!isAndroid6()) {
                //打开相册
                openAlbum()
                return
            }
            if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE)
                return
            }
        }
        //打开相册
        openAlbum()
    }

    /**
     * 打开相册
     */
    private fun openAlbum() {
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO_CODE)
    }

    /**
     * 修改头像
     */
    private fun modifyAvatar(imagePath: String) {
        if (!TextUtils.isEmpty(imagePath)) {
            //保存到数据表中
            modifyContent(2, imagePath)
            Log.d(TAG, "modifyAvatar: $imagePath")
        } else {
            showMsg("图片获取失败")
        }
    }

    /**
     * 退出登录
     */
    private fun logout() {
        showMsg("退出登录")
        MVUtils.put(Constant.IS_LOGIN, false)
        jumpActivityFinish(LoginActivity::class.java)
    }

    /**
     * 权限请求结果
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PermissionUtils.REQUEST_STORAGE_CODE -> {
                //文件读写权限
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showMsg("您拒绝了读写文件权限，无法打开相册，抱歉。")
                    return
                }
                openAlbum()
            }
            PermissionUtils.REQUEST_CAMERA_CODE -> {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showMsg("您拒绝了相机权限，无法打开相机，抱歉。")
                    return
                }
                openCamera()
            }
            else -> {}
        }
    }

    /**
     * 页面返回结果
     */
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) {
            showMsg("未知原因")
            return
        }
        when (requestCode) {
            PermissionUtils.REQUEST_MANAGE_EXTERNAL_STORAGE_CODE -> {
                //从外部存储管理页面返回
                if (!isStorageManager()) {
                    showMsg("未打开外部存储管理开关，无法打开相册，抱歉")
                    return
                }
                if (!hasPermission(PermissionUtils.READ_EXTERNAL_STORAGE)) {
                    requestPermission(PermissionUtils.READ_EXTERNAL_STORAGE)
                    return
                }
                //打开相册
                openAlbum()
            }
            SELECT_PHOTO_CODE -> //相册中选择图片返回
                modifyAvatar(CameraUtils.getImageOnKitKatPath(data, this))
            TAKE_PHOTO_CODE ->   //相机中拍照返回
                modifyAvatar((if (isAndroid10()) mCameraUri.toString() else mCameraImagePath)!!)
            else -> {}
        }
    }

    /**
     * 请求定位权限
     */
    private fun requestLocation() {
        if (isAndroid6()) {
            if (!hasPermission(PermissionUtils.LOCATION)) {
                requestPermission(PermissionUtils.LOCATION)
            }
        } else {
            showMsg("您无需动态请求权限")
        }
    }


}
