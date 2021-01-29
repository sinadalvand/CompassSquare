/*
 * *
 *  * Created by Sina Dalvand on 10/25/20 10:36 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 10/25/20 10:36 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.core.view.ViewCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import com.google.android.material.snackbar.Snackbar
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.math.roundToInt

/**
 * all extensions method will develop here
 */
public object Extensions {


    /* =<======================  Rx Java / RxAndroid  =====================> */

    fun <T> Flowable<T>.toLiveData(): LiveData<T> {
        return LiveDataReactiveStreams.fromPublisher(this)
    }

    fun <T> Observable<T>.toLiveData(): LiveData<T> {
        return LiveDataReactiveStreams.fromPublisher(this.toFlowable(BackpressureStrategy.BUFFER))
    }

    fun <T> Flowable<T>.backgroundRequest(): Flowable<T> {
        return this.subscribeOn(Schedulers.io())
    }

    fun <T> Single<T>.backgroundRequest(): Single<T> {
        return this.subscribeOn(Schedulers.io())
    }

    fun  Completable.backgroundRequest(): Completable {
        return this.subscribeOn(Schedulers.io())
    }

    fun <T> Observable<T>.backgroundRequest(): Observable<T> {
        return this
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


    /* =<======================  Context  =====================> */

    /**
     * make toast message
     * @receiver Context
     * @param msg String?
     */
    fun Context.toast(msg: String?) {
        msg?.let {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun View.snack(msg: String?) {
        msg?.let {
            val snack = Snackbar.make(this, msg, Snackbar.LENGTH_LONG)
            ViewCompat.setLayoutDirection(snack.view, ViewCompat.LAYOUT_DIRECTION_RTL);
            snack.show()
        }
    }

    /* <==============================  activity ===========================> */

    /**
     * open user default browser for special link
     * @param activity Context
     * @param link String
     */
    fun Activity.openBrowser(activity: Context, link: String) {
        val browserIntent = Intent(ACTION_VIEW, Uri.parse(link))
        activity.startActivity(browserIntent)
    }


    /* <================================  global =============================> */

    /**
     * convert dp to px size
     **/
    val Int.dpPx: Int get() = (this * Resources.getSystem().displayMetrics.density).roundToInt()

    /**
     * convert pc to dp size
     **/
    val Int.spPx: Int get() = (this * Resources.getSystem().displayMetrics.scaledDensity).roundToInt()


    /**
     * check any value is null or not
     * @receiver Any?
     * @return Boolean
     */
    fun Any?.isNull(): Boolean = this == null

    /**
     * check any value is not null or not
     * @receiver Any?
     * @return Boolean
     */
    fun Any?.isNotNull(): Boolean = this != null

    /**
     * let us to handle none exist items in arrays
     * @receiver Array<T>
     * @param index Int
     * @return T?
     */
    fun <T> Array<T>.getNullable(index: Int): T? = try {
        get(index)
    } catch (e: Exception) {
        null
    }

    /**
     * create inflate easy as possible
     * @param parser Int
     * @param root ViewGroup
     * @param attachToRoot Boolean
     * @return View
     */
    fun inflate(
            @LayoutRes parser: Int,
            @Nullable root: ViewGroup,
            attachToRoot: Boolean = false
    ): View {
        return LayoutInflater.from(root.context).inflate(parser, root, attachToRoot)
    }

    /* <================================  View  =============================> */


    fun View.gone() {
        visibility = View.GONE
    }


    fun View.visible(animate: Boolean = false) {
        if (visibility == View.VISIBLE) return
        if (animate) {
            alpha = 0f
            visibility = View.VISIBLE
            post {
                animate().alpha(1.0f).setDuration(300).start()
            }
        } else
            visibility = View.VISIBLE
    }

    fun View.invisible(animate: Boolean = false) {
        if (animate) {
            animate().alpha(1.0f).setDuration(300).withEndAction { visibility = View.INVISIBLE }
                    .start()
        } else
            visibility = View.INVISIBLE
    }

    /**
     * use to make border or radius pragmatically
     * @receiver View
     * @param color Int border color
     * @param width Int border width
     * @param background Int background color
     */
    fun View.setBorder(
            color: Int = Color.TRANSPARENT,
            width: Int = 1.dpPx,
            background: Int = Color.TRANSPARENT
    ) {
        setBorder(color, width, 0f, background)
    }


    /**
     * use to make border or radius pragmatically
     * @receiver View
     * @param color Int border color
     * @param width Int border width
     * @param radius Float border corner radius
     * @param background Int background color
     */
    fun View.setBorder(
            color: Int = Color.TRANSPARENT,
            width: Int = 1.dpPx,
            radius: Float = 0f,
            background: Int = Color.TRANSPARENT
    ) {
        setBorder(color, width, arrayOf(radius, radius, radius, radius), background)
    }


    /**
     * use to make border or radius pragmatically
     * @receiver View
     * @param color Int border color
     * @param width Int border width
     * @param radius Array<Float> border radius (left-top,right-top,right-bottom,left-bottom)
     * @param background Int background color
     */
    fun View.setBorder(
            color: Int = Color.TRANSPARENT,
            width: Int = 1.dpPx,
            radius: Array<Float> = arrayOf(),
            background: Int = Color.TRANSPARENT
    ) {
        this.background = GradientDrawable().apply {
            cornerRadii = floatArrayOf(
                    radius.getNullable(0) ?: 0f, radius.getNullable(0) ?: 0f,
                    radius.getNullable(1) ?: 0f, radius.getNullable(1) ?: 0f,
                    radius.getNullable(2) ?: 0f, radius.getNullable(2) ?: 0f,
                    radius.getNullable(3) ?: 0f, radius.getNullable(3) ?: 0f
            )
            setStroke(width, color)
            shape = GradientDrawable.RECTANGLE
            setColor(background)
        }
    }

}