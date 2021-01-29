/*
 * *
 *  * Created by Sina Dalvand on 12/27/20 1:28 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/27/20 1:28 AM
 *
 */

package ir.sinadalvand.projects.bazaarproject.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ir.sinadalvand.projects.bazaarproject.R
import ir.sinadalvand.projects.bazaarproject.contracts.onRecyclerItemClicked
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.ui.holder.VenueHolder

class VenueAdapter(val onItemClick: onRecyclerItemClicked<Venue>) : PagingDataAdapter<Venue, VenueHolder>(object : DiffUtil.ItemCallback<Venue>() {
    override fun areItemsTheSame(oldItem: Venue, newItem: Venue): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Venue, newItem: Venue): Boolean = true
}) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueHolder =
            VenueHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_venue, parent, false))

    override fun onBindViewHolder(holder: VenueHolder, position: Int) {
        getItem(position)?.let { holder.onBind(it, position) }
        holder.itemView.setOnClickListener { onItemClick.recyclerItemClicked(getItem(position),position) }
    }


}