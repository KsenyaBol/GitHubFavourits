package com.example.githubfavourits.ui.base

import android.os.Bundle
import androidx.annotation.ContentView
import androidx.annotation.LayoutRes
import com.github.mikephil.charting.charts.LineChart
import com.omega_r.base.components.OmegaActivity
import com.omega_r.base.mvp.presenters.OmegaPresenter

abstract class BaseActivity : OmegaActivity, BaseView {

    constructor() : super()

    @ContentView
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    abstract override val presenter: OmegaPresenter<out BaseView>

    // TODO clean
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.setBackgroundDrawable(ColorDrawable(R.attr.activityBackground))
//        URL-адрес для всех API: https://api.github.com/search
    }

//    abstract fun setOnChartValueSelectedListener(lineChart: LineChart)
}