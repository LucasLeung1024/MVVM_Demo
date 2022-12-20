package com.kevin.mvvm.utils

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.TranslateAnimation


object EasyAnimation {
    /**
     * 开始眨眼动画
     *
     * @param view 需要设置动画的View
     */
    fun startBlink(view: View) {
        val alphaAnimation = AlphaAnimation(0.0f, 1.0f)
        alphaAnimation.duration = 500
        alphaAnimation.startOffset = 20
        alphaAnimation.repeatMode = Animation.REVERSE
        alphaAnimation.repeatCount = Animation.INFINITE
        view.startAnimation(alphaAnimation)
    }

    /**
     * 开始眨眼动画
     *
     * @param view           需要设置动画的View
     * @param alphaAnimation 透明度动画（自行配置）
     */
    fun startBlink(view: View, alphaAnimation: AlphaAnimation?) {
        view.startAnimation(alphaAnimation)
    }

    /**
     * 停止眨眼动画
     *
     * @param view 需要清除动画的View
     */
    fun stopBlink(view: View?) {
        view?.clearAnimation()
    }

    /**
     * 移动指定View的宽度
     *
     * @param view
     */
    fun moveViewWidth(view: View, callback: TranslateCallback) {
        view.post {

            //通过post拿到的tvTranslate.getWidth()不会为0。
            val translateAnimation =
                TranslateAnimation(0F, view.width.toFloat(), 0F, 0F)
            translateAnimation.duration = 1000
            translateAnimation.fillAfter = true
            view.startAnimation(translateAnimation)

            //动画监听
            translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    //检查Android版本
                    callback.animationEnd()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    /**
     * 移动指定View的宽度
     *
     * @param view               需要位移的View
     * @param callback           位移动画回调
     * @param translateAnimation 位移动画 （自行配置）
     */
    fun moveViewWidth(
        view: View,
        callback: TranslateCallback,
        translateAnimation: TranslateAnimation
    ) {
        view.post {

            //通过post拿到的tvTranslate.getWidth()不会为0。
            view.startAnimation(translateAnimation)

            //动画监听
            translateAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    //检查Android版本
                    callback.animationEnd()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
        }
    }

    interface TranslateCallback {
        //动画结束
        fun animationEnd()
    }
}

