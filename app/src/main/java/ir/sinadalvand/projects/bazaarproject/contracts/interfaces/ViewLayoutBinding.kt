/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 19:43 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 19:43 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.contracts.interfaces

interface ViewLayoutBinding {

    /**
     * @return Any must be {@link android.view.View View} or {@link resId Int Int}
     */
    fun layoutView(): Any

}