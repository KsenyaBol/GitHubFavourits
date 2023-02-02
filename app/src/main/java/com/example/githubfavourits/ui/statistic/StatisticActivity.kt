package com.example.githubfavourits.ui.statistic

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
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
import com.omega_r.libs.extensions.date.getDateMonth
import com.omega_r.libs.extensions.date.getDateYear
import com.omega_r.libs.extensions.string.toDate
import com.omegar.libs.omegalaunchers.createActivityLauncher
import com.omegar.libs.omegalaunchers.tools.put
import com.omegar.mvp.ktx.providePresenter
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class StatisticActivity : BaseActivity(R.layout.activity_statistic), StatisticView,
    OnChartValueSelectedListener {


    companion object {

        private const val EXTRA_NAME_USER = "nameUser"
        private const val EXTRA_REPO = "repo"
        private var direction = Entity.WEEK

        fun createLauncher(nameUser: String, repo: Repo) = createActivityLauncher(
            EXTRA_NAME_USER put nameUser,
            EXTRA_REPO put repo,
        )
    }

    enum class Entity(range: IntRange) {
        WEEK(0..6), MONTH(0..3), YEAR(0..11)
    }

    override val presenter: StatisticPresenter by providePresenter {
        StatisticPresenter(this[EXTRA_NAME_USER]!!, this[EXTRA_REPO]!!)
    }

    private val timeText: TextView by bind(R.id.textview_time)
    private val dayButton: Button by bind(R.id.button_day)
    private val monthButton: Button by bind(R.id.button_month)
    private val yearButton: Button by bind(R.id.button_year)
    private val backImageButton: ImageButton by bind(R.id.imagebutton_back)
    private val nextImageButton: ImageButton by bind(R.id.imagebutton_next)

    override var starredList: List<StarredRepository> = listOf()
        set(value) {
            field = value
            //starList = value
        }

//    private var starList: List<StarredRepository> = listOf()
    private var barArrayList = arrayListOf<BarEntry>()
    private val allDateBarList = arrayListOf<Date>()
    private val weekBarList = arrayListOf<Date>()
    private val monthBarList = arrayListOf<Date>()
    private var yearBarList = arrayListOf<Date>()
    private var nowDate = Calendar.getInstance()
    private val currentDate = Date()
    private val dateFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
    private val dateText: String = dateFormat.format(currentDate)
    var quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")

    private val barchart: BarChart by bind(R.id.barchart) {
        setOnChartValueSelectedListener(this@StatisticActivity)


        val xFormatter: ValueFormatter = object : ValueFormatter() {
            @SuppressLint("SimpleDateFormat")
            override fun getFormattedValue(value: Float): String {

                if (direction == Entity.WEEK) {
                    quarters = arrayOf("Mn", "Tu", "Wn", "Th", "Fr", "St", "Sn")
                }
                if (direction == Entity.MONTH) {
                    quarters = arrayOf("1st", "2nd", "3d", "4th")
                }
                if (direction == Entity.YEAR) {
                    quarters = arrayOf("J", "F", "M", "A", "M", "J", "J", "A", "S", "O", "N", "D")
                }

                return quarters.getOrNull(value.toInt()) ?: value.toString()
            }
        }

        val yFormatter: ValueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {

                var count = 0

                if (direction == Entity.WEEK) {
                    count = weekBarList.size
                }
                if (direction == Entity.MONTH) {
                    count = monthBarList.size
                }
                if (direction == Entity.YEAR) {
                    count = yearBarList.size
                }

                return "$count"
            }
        }
        getBarChartData()
        val barDataSet = BarDataSet(barArrayList, "")
        val barData = BarData(barDataSet)

        barDataSet.valueTextColor = Color.BLACK
        barDataSet.valueTextSize = 14F
        barDataSet.setGradientColor(R.color.gray, R.color.dark_greyish_blue)
        barDataSet.axisDependency = YAxis.AxisDependency.LEFT

        barchart.data = barData
        barchart.setGridBackgroundColor(R.color.transparent)
        barchart.setTouchEnabled(true)
        barchart.animateY(1000)
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

        val calendar: Calendar = GregorianCalendar()
        calendar.clear()
        calendar[Calendar.YEAR] = Calendar.YEAR
        calendar[Calendar.WEEK_OF_YEAR] = Calendar.WEEK_OF_YEAR
        val weekStart = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 6)
        val weekEnd = calendar.time
        val period: ClosedRange<Date> = (weekStart..weekEnd)

        val format = SimpleDateFormat("EEEE dd MMMM yyyy")

        starredList.forEach { starred ->
            val starredAtDate: Date = starred.favouriteAt.toDate(format)!!
            allDateBarList.add(starredAtDate)
            Log.d("allDateList", allDateBarList.toString())
        }

//        Log.d("starList", starList.toString())

        allDateBarList.forEach { date ->
            if (date.getDateYear() == Calendar.YEAR) yearBarList.add(date)
            Log.d("yearList", yearBarList.toString())
        }
        yearBarList.forEach { date ->
            if (date.getDateMonth() == Calendar.MONTH) monthBarList.add(date)
        }
        monthBarList.forEach { date ->
            if (date in period) weekBarList.add(date)
            Log.d("weekList", weekBarList.toString())
        }


//        if (direction == Entity.WEEK) {
//            barArrayList = arrayListOf(
//                BarEntry(0f,2f),
//                BarEntry(1f,4f),
//                BarEntry(2f,5f),
//                BarEntry(3f,3f),
//                BarEntry(4f,2f),
//                BarEntry(5f,6f),
//                BarEntry(6f,4f),
//            )
//        }
        barArrayList = arrayListOf(
            BarEntry(0f, 2f),
            BarEntry(1f, 4f),
            BarEntry(2f, 5f),
            BarEntry(3f, 3f),
            BarEntry(4f, 2f),
            BarEntry(5f, 6f),
            BarEntry(6f, 4f),
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun converter() {

//        val newFormat = format.format(starredList)
//        allDateBarList.add(newFormat)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val calendar: Calendar = GregorianCalendar()
//        calendar.clear()
//        calendar[Calendar.YEAR] = Calendar.YEAR
//        calendar[Calendar.WEEK_OF_YEAR] = Calendar.WEEK_OF_YEAR
//        val weekStart = calendar.time
//        calendar.add(Calendar.DAY_OF_YEAR, 6)
//        val weekEnd = calendar.time
//        val period: ClosedRange<Date> = (weekStart..weekEnd)
//
//        val format = SimpleDateFormat("EEEE dd MMMM yyyy")
//
//
//
//        starredList.forEach { starred ->
//            val starredAtDate: Date = starred.favouriteAt.toDate(format)!!
//            allDateBarList.add(starredAtDate)
//            Log.d("allDateList", allDateBarList.toString())
//        }
//
////        Log.d("starList", starList.toString())
//
//        allDateBarList.forEach { date ->
//            if (date.getDateYear() == Calendar.YEAR) yearBarList.add(date)
//            Log.d("yearList", yearBarList.toString())
//        }
//        yearBarList.forEach { date ->
//            if (date.getDateMonth() == Calendar.MONTH) monthBarList.add(date)
//        }
//        monthBarList.forEach { date ->
//            if (date in period) weekBarList.add(date)
//            Log.d("weekList", weekBarList.toString())
//        }
//        dayBarList.sort()

        timeText.text = dateText

        presenter.getStarredDataList()

        dayButton.setOnClickListener {
            direction = Entity.WEEK
            barchart.invalidate()
        }

        monthButton.setOnClickListener {
            direction = Entity.MONTH
            barchart.invalidate()

        }

        yearButton.setOnClickListener {
            direction = Entity.YEAR
            barchart.invalidate()

        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(applicationContext, "$e coming soon", Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Toast.makeText(applicationContext, "nothing", Toast.LENGTH_SHORT).show()
    }


}