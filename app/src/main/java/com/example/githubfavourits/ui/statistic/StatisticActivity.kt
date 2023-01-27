package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.example.domain.entity.Repo
import com.example.domain.entity.StarredRepository
import com.example.githubfavourits.R
import com.example.githubfavourits.ui.base.BaseActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StatisticActivity : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {


    companion object {

        private const val EXTRA_NAME_USER = "nameUser"
        private const val EXTRA_REPO = "repo"
        private var direction = Entity.DAY

        private val weekMap =
            mapOf(
                Calendar.MONDAY to 0,
                Calendar.TUESDAY to 1,
                Calendar.WEDNESDAY to 2,
                Calendar.THURSDAY to 3,
                Calendar.FRIDAY to 4,
                Calendar.SATURDAY to 5,
                Calendar.SUNDAY to 6
            )

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    enum class Entity {
        DAY, MONTH, YEAR
    }

    override val presenter: StatisticPresenter by providePresenter{
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }

//    private val starredList = arrayListOf<StarredRepository>()

    private val timeText: TextView by bind(R.id.textview_time)
    private val timeDayText: TextView by bind(R.id.textview_time_day)
    private val dayButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    override var starredList: List<StarredRepository> = listOf()
//        set(value) {
//            field = value
//        }
    private var barArrayList =  arrayListOf<BarEntry>()
    private var nowData = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    private var dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM-yyyy")


    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)

        getBarChartData()

        var quartersDay =  arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")

        if (direction == Entity.DAY) {
            quartersDay = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
        }
        if (direction == Entity.MONTH) {
            quartersDay = arrayOf("1st", "2nd", "3d", "4th")
        }
        if (direction == Entity.YEAR) {
            quartersDay = arrayOf("Jn", "F", "Mr", "Ap", "My", "Jn", "Jl", "Au", "S", "O", "N", "D")
        }

        val xFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val now = Calendar.getInstance()
                now.add(Calendar.DAY_OF_MONTH, value.toInt())
                val week = now.get(Calendar.DAY_OF_WEEK)
                val index = weekMap[week]!!

                return quartersDay[index]
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val count = value.toInt()
                return "$count"
            }
        }

        val barDataSet = BarDataSet(barArrayList, "")
        val barData = BarData(barDataSet)

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.setGradientColor(R.color.gray, R.color.dark_greyish_blue)
        barDataSet.axisDependency = YAxis.AxisDependency.LEFT

        barchart.data = barData
        barchart.setGridBackgroundColor(R.color.transparent)
        barchart.setTouchEnabled(true)

        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(false)
        xAxis.textSize = 12f
        xAxis.textColor = (R.color.black)
        xAxis.gridColor = (R.color.transparent)

        YAxis.YAxisLabelPosition.INSIDE_CHART

        axisRight.isEnabled = false
        axisLeft.isEnabled = false
        axisLeft.setDrawAxisLine(false)
        axisLeft.setDrawGridLines(false)
        axisLeft.gridColor = (R.color.transparent)

        description.isEnabled = false

        barDataSet.setDrawIcons(false)
        barDataSet.isHighlightEnabled = false
        barDataSet.form = Legend.LegendForm.NONE

        xAxis.valueFormatter = xFormatter
        axisLeft.valueFormatter = yFormatter

        invalidate()
    }

    fun getBarChartData() {
        barArrayList = arrayListOf(
            BarEntry(0f,2f),
            BarEntry(1f,4f),
            BarEntry(2f,5f),
            BarEntry(3f,3f),
            BarEntry(4f,2f),
            BarEntry(5f,6f),
            BarEntry(6f,4f),
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val startDate = LocalDate.now().minusDays(7)
        val endDate = Calendar.DAY_OF_MONTH
        val nowMonth = Calendar.MONTH
        val nowYear = Calendar.YEAR


        timeDayText.text = "$startDate - $endDate"
//        timeText.text = "$nowMonth $nowYear"
//            LocalDate.parse("$nowData", dateFormat).toString()

        presenter.getStarredDataList()

        dayButton.setOnClickListener {
            direction = Entity.DAY
            barchart

        }

        monthButton.setOnClickListener {
            direction = Entity.MONTH
        }

        yearButton.setOnClickListener {
            direction = Entity.YEAR
        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }




}