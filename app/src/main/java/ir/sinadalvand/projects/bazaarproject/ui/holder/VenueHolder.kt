/*
 * *
 *  * Created by Sina Dalvand on 12/27/20 1:30 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/27/20 1:30 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.ui.holder

import android.view.View
import androidx.core.text.HtmlCompat
import ir.sinadalvand.projects.bazaarproject.contracts.XviewHolder
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.util.Utils.getAddress
import ir.sinadalvand.projects.bazaarproject.util.Utils.getCategory
import kotlinx.android.synthetic.main.recycler_item_venue.view.*

class VenueHolder(v: View) : XviewHolder<Venue>(v) {

    private val title = v.recycler_item_venue_title
    private val categories = v.recycler_item_venue_category
    private val address = v.recycler_item_venue_address
    private val distance = v.recycler_item_venue_distance
    private val shareBtn = v.recycler_item_venue_share

    override fun onBind(data: Venue, position: Int) {
        title.text = HtmlCompat.fromHtml(data.name ?: "-", HtmlCompat.FROM_HTML_MODE_COMPACT)
        categories.text = getCategory(data.categories);
        address.text = getAddress(data.location);
        distance.text = data.location?.distance?.toString() ?: "-";
    }






}