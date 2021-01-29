/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 19:20 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 19:20 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ir.sinadalvand.projects.bazaarproject.contracts.interfaces.ViewLayoutBinding

abstract class Xfragment : Fragment(), ViewLayoutBinding {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return getPreparedView(inflater, container)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        overrideBackButton()
    }

    /**
     * parse every input for fragment to view and don't care it's Res id or View
     * @param inflater LayoutInflater
     * @param container ViewGroup?
     * @return View
     */
    private fun getPreparedView(inflater: LayoutInflater, container: ViewGroup?): View {
        return when (val layout = layoutView()) {
            is Int -> inflater.inflate(layout, container, false)
            is View -> return layout
            else -> throw IllegalArgumentException("You should Set @resLayout Int or View")
        }
    }


    /**
     * override back press button in fragments
     * @return Boolean consume back press button if yes return true
     */
    open fun backPressButton(): Boolean {
        return false
    }


    // we should override back button to be able return to previous category
    private fun overrideBackButton() {
        view?.isFocusableInTouchMode = true;
        view?.requestFocus();
        view?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event?.action != KeyEvent.ACTION_DOWN) {
                    return backPressButton()
                }
                return false
            }
        })
    }

}