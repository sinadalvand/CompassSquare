/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 12:46 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 12:46 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.util

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class UtilsTest {

    @Test
    fun getConcatFormatedDateForFourSquareAPI() {
        val data = SimpleDateFormat("yyyyMMdd")
        val today = Utils.getDateIntFormat()
        Assert.assertEquals(today,data.format(Date()))
    }
}