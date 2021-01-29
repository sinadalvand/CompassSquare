/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 11:06 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 11:06 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.core.text.HtmlCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ir.sinadalvand.projects.bazaarproject.R
import ir.sinadalvand.projects.bazaarproject.contracts.ViewErrorHandler
import ir.sinadalvand.projects.bazaarproject.contracts.XfragmentVm
import ir.sinadalvand.projects.bazaarproject.contracts.XrequestStatus
import ir.sinadalvand.projects.bazaarproject.data.model.ResponseWrapper
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.util.Extensions.gone
import ir.sinadalvand.projects.bazaarproject.util.Extensions.invisible
import ir.sinadalvand.projects.bazaarproject.util.Extensions.visible
import ir.sinadalvand.projects.bazaarproject.util.Utils.getAddress
import ir.sinadalvand.projects.bazaarproject.util.Utils.getCategory
import ir.sinadalvand.projects.bazaarproject.viewmodel.FragmentDetailViewModel
import kotlinx.android.synthetic.main.fragment_details.*
import kotlinx.android.synthetic.main.layout_error_handler.*

class DetailFragment : XfragmentVm<FragmentDetailViewModel>(), ViewErrorHandler {

    // use safe arg to prevent any error to pass parameters between fragments
    private val navArgs: DetailFragmentArgs by navArgs()

    override fun layoutView(): Any = R.layout.fragment_details

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm.venueDetail.observe(viewLifecycleOwner) {
            setDetailsToViews(it.data)
            errorHandler(it)
        }
        requestData()
    }

    private fun requestData() {
        vm.requestVenue(navArgs.venueId)
    }

    // get what kind of error happend
    private fun errorHandler(wrapper: ResponseWrapper<*>) {
        when (wrapper.requestStatus) {
            XrequestStatus.ERROR -> {
                showErrorHandler(getString(R.string.unknown_error), getString(R.string.unknown_error_happened), null, getString(R.string.return_text)) {
                    findNavController().navigateUp()
                }
            }
            XrequestStatus.NO_INTERNET -> {
                showErrorHandler(getString(R.string.no_internet), getString(R.string.check_netwoek_desc), null, getString(R.string.retry)) {
                    requestData()
                }
            }
            else -> {
                hideErrorHandler()
            }
        }
    }

    // set values to views
    private fun setDetailsToViews(venue: Venue?) {
        if (venue == null) {
            hideAllViews()
            return
        }
        fragment_details_img_header.visible()
        fragment_details_title?.text = HtmlCompat.fromHtml(venue.name ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
        fragment_details_txt_rate.text = getString(R.string.rate, venue.rate?.toString() ?: "1.0")
        fragment_details_txt_like.text = (venue.like ?: 0).toString()
        fragment_details_txt_category?.text = getCategory(venue.categories)
        fragment_details_txt_address?.text = getAddress(venue.location)
    }

    private fun hideAllViews() {
        fragment_details_layout_like.invisible()
        fragment_details_layout_rate.invisible()
        fragment_details_txt_category.invisible()
        fragment_details_txt_address.invisible()
        fragment_details_divider.invisible()
        fragment_details_title.invisible()
        fragment_details_recycler_photo.invisible()
        fragment_details_img_header.invisible()
    }


    override fun showErrorHandler(title: String, msg: String, res: Int?, buttonText: String?, func: () -> Unit) {
        layout_handler_layout_container.visible(true)

        layout_handler_img_icon?.setImageResource(res ?: R.drawable.ic_warning)
        layout_handler_txt_title?.text = title
        layout_handler_txt_desc?.text = msg

        if (buttonText == null) {
            layout_handler_btn_action?.gone()
            layout_handler_btn_action?.setOnClickListener(null)
        } else {
            layout_handler_btn_action?.visible()
            layout_handler_btn_action?.text = buttonText
            layout_handler_btn_action?.setOnClickListener { func.invoke() }
        }

    }

    override fun hideErrorHandler() {
        layout_handler_layout_container.invisible(true)
    }


}