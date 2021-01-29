/*
 * *
 *  * Created by Sina Dalvand on 12/22/20 10:52 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/22/20 10:52 PM
 *
 */

package ir.sinadalvand.projects.bazaarproject.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog
import com.github.javiersantos.materialstyleddialogs.enums.Style
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.material.appbar.AppBarLayout
import ir.sinadalvand.projects.bazaarproject.R
import ir.sinadalvand.projects.bazaarproject.contracts.ViewErrorHandler
import ir.sinadalvand.projects.bazaarproject.contracts.XfragmentVm
import ir.sinadalvand.projects.bazaarproject.contracts.onRecyclerItemClicked
import ir.sinadalvand.projects.bazaarproject.data.model.Venue
import ir.sinadalvand.projects.bazaarproject.exception.NoInternetException
import ir.sinadalvand.projects.bazaarproject.ui.adapter.VenueAdapter
import ir.sinadalvand.projects.bazaarproject.util.Extensions.gone
import ir.sinadalvand.projects.bazaarproject.util.Extensions.invisible
import ir.sinadalvand.projects.bazaarproject.util.Extensions.visible
import ir.sinadalvand.projects.bazaarproject.viewmodel.FragmentMainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.fragment_main_header_collapsable.*
import kotlinx.android.synthetic.main.fragment_main_header_fix.*
import kotlinx.android.synthetic.main.fragment_main_header_fix.view.*
import kotlinx.android.synthetic.main.layout_error_handler.*
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class MainFragment : XfragmentVm<FragmentMainViewModel>(), ViewErrorHandler, onRecyclerItemClicked<Venue> {

    // permission code request
    private val LOCATION_PERMISSION_CODE = 5671

    // gps turn on request
    private val GPS_SETTINGS_CODE = 9871

    // permission manifest
    private val permission = Manifest.permission.ACCESS_FINE_LOCATION

    // recycler adapter
    private val adapter = VenueAdapter(this)

    // gps open setting
    private var gpsException: ResolvableApiException? = null

    // set view for this fragment
    override fun layoutView(): Any = R.layout.fragment_main

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (requestLocationPermission()) {
            requestLocationService()
        }


        // set animation for
        fragment_main_icon.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.half_rotate)
                .apply { startOffset = 1000;repeatMode = Animation.REVERSE })


        registerGpsRequestListener()
        setCollapsePercentListener()
        setupRecyclerAdapter()
        setupData()
        stateHandler()
    }


    // register gps request for show turn on gps dialog
    private fun registerGpsRequestListener() {
        vm.getGpsRequest().observe(viewLifecycleOwner) {
            gpsException = it;
            gpsRequest()
        }
    }

    // request gps
    private fun gpsRequest() {
        startIntentSenderForResult(this.gpsException?.resolution?.intentSender, GPS_SETTINGS_CODE, null, 0, 0, 0, null)
    }


    // setup adapter and data paging
    private fun setupData() {
        vm.pagingVenue.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

    // bind adapter to recycler
    private fun setupRecyclerAdapter() {
        fragment_main_recycler?.apply {
            setHasFixedSize(true)
            adapter = this@MainFragment.adapter
        }

        fragment_main_refresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    // set listener to appbar layout to findout when hide divider
    private fun setCollapsePercentListener() {
        fragment_main_appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appbar, verticalOffset ->
            val percent = abs(verticalOffset).toFloat() / (appbar?.totalScrollRange?.toFloat() ?: 1f)
            fragment_main_appbar_divider.alpha = 1 - percent
        })
    }

    // send request to viewModel to config location service updater
    private fun requestLocationService() {
        vm.setupLocationManager(this)
    }

    /**
     * check location permission and if not grant then request it
     * @return Boolean
     */
    private fun requestLocationPermission(): Boolean {
        val permission = ActivityCompat.checkSelfPermission(requireContext(), permission)
        if (permission != PackageManager.PERMISSION_GRANTED) {
            showPermissionDialog()
            return false
        }
        return true
    }

    // show dialog before request location to say user know what we need
    private fun showPermissionDialog() {
        MaterialStyledDialog.Builder(requireActivity())
                .setTitle(getString(R.string.permission_request))
                .setIcon(R.drawable.ic_location)
                .setStyle(Style.HEADER_WITH_ICON)
                .withDialogAnimation(true)
                .setNegativeText(getString(R.string.no))
                .setPositiveText(getString(R.string.yes))
                .onPositive { requestRuntimePermission() }
                .onNegative { showNeedPermissionError() }
                .setDescription(getString(R.string.permission_request_description))
                .show()
    }

    // show error message in background that say we need permission to get location
    private fun showNeedPermissionError() {
        showErrorHandler(getString(R.string.permission_request), getString(R.string.need_yout_permission), null, getString(R.string.grant_persmission)) {
            requestRuntimePermission()
        }
    }

    // request runtime permission with requestPermissions()
    private fun requestRuntimePermission() {
        requestPermissions(arrayOf(permission), LOCATION_PERMISSION_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (GPS_SETTINGS_CODE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                // now request data again
                requestLocationService()

                // show updating
                hideErrorHandler()
                shortStatus(getString(R.string.updating), R.drawable.ic_loader, true)
            } else {
                showErrorHandler(getString(R.string.gps_access), getString(R.string.gps_desc), null, getString(R.string.turn_on)) {
                    gpsRequest()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // manage permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    requestLocationService()
                    hideErrorHandler()
                } else {
                    showNeedPermissionError()
                }
                return
            }
            else -> {

            }
        }
    }

    // show load status in view
    private fun stateHandler() {
        adapter.addLoadStateListener { loadState ->

            // total state for both (and operation)
            val total = loadState
            // database state
            val source = loadState.source
            // remote state
            val remote = loadState.mediator


            // updating
            if (total.refresh is LoadState.Loading) {
                shortStatus(getString(R.string.updating), R.drawable.ic_loader, true)
            }


            // offline
            if (remote?.refresh is LoadState.Error && (remote.refresh as? LoadState.Error)?.error is NoInternetException && source.refresh is LoadState.NotLoading) {
                fragment_main_refresh?.isRefreshing = false
                shortStatus(getString(R.string.offline), R.drawable.ic_no_internet)

                if (adapter.itemCount == 0) {
                    showErrorHandler(getString(R.string.empty), getString(R.string.empty_desc))
                }
            }


            //online
            if (remote?.refresh is LoadState.NotLoading && source.refresh is LoadState.NotLoading) {
                fragment_main_refresh?.isRefreshing = false
                shortStatus(getString(R.string.updated), R.drawable.ic_internet)

                if (adapter.itemCount == 0) {
                    showErrorHandler(getString(R.string.not_found), getString(R.string.not_found_desc))
                }
            }


            //Error
            if ((remote?.refresh is LoadState.Error || remote?.append is LoadState.Error || remote?.prepend is LoadState.Error) && source.refresh is LoadState.NotLoading) {
                val error = remote.refresh as? LoadState.Error ?: remote.append as? LoadState.Error ?: remote.prepend as? LoadState.Error
                if (error?.error is RuntimeException) {
                    fragment_main_refresh?.isRefreshing = false
                    shortStatus(getString(R.string.error), R.drawable.ic_error, false, ContextCompat.getColor(requireContext(), R.color.red))

                    if (adapter.itemCount == 0) {
                        showErrorHandler(getString(R.string.unexpected_error), getString(R.string.unexpected_error_desc))
                    }
                }


            }


            if (adapter.itemCount != 0) {
                hideErrorHandler()
            }

//          Timber.e(" \n <================ Dump Data: ==============> \n %s \n <=======================================>", total.toString())
        }
    }

    // rotate animation for load
    private val rotateAnim by lazy { AnimationUtils.loadAnimation(requireContext(), R.anim.rotate) }

    // show small status in top right side
    private fun shortStatus(status: String, @DrawableRes res: Int, turn: Boolean = false, color: Int = Color.WHITE) {
        fragment_main_status_txt?.text = status
        fragment_main_status_txt?.setTextColor(color)
        fragment_main_status_img?.setImageResource(res)
        if (turn) fragment_main_status_img?.startAnimation(rotateAnim) else rotateAnim.cancel()

    }

    override fun showErrorHandler(title: String, msg: String, res: Int?, buttonText: String?, func: () -> Unit) {
        layout_handler_layout_container?.visible(true)

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
        layout_handler_layout_container?.invisible(true)
    }

    override fun recyclerItemClicked(data: Venue?, position: Int) {
        data?.let {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToDetailFragment(data.id))
        }
    }
}