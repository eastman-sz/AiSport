package com.history

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.sportdata.GpsInfoDbHelper
import com.util.ILog
import com.zz.sport.ai.R
import kotlinx.android.synthetic.main.activity_sport_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.sp
import org.jetbrains.anko.uiThread

class SportDetailActivity : AppCompatActivity() {

    var sportId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sport_detail)

        title = "详细"

        mapView.onCreate(savedInstanceState)

        initViews()
        initListeners()
    }

    private fun initViews(){
        sportId = intent.getLongExtra("sportId" , 0)
        if (sportId == 0L){
            finish()
            return
        }
        val resultMapHelper = ResultMapHelper(mapView)
        doAsync {
            val points = GpsInfoDbHelper.getLatLng(sportId)

            val traceList =  GpsInfoDbHelper.getTraceLocation(sportId)

            ILog.e("---轨迹返回-: ${points.size}   ${traceList.size}")

            val gpsInfo = GpsInfoDbHelper.getGpsInfo(sportId)

            if (traceList.size > 50){
                uiThread {
//                    resultMapHelper.setPoints(points)
                    resultMapHelper.setPointsN(gpsInfo)
                }

            }else{

                LBSTraceHelper.startTrace(traceList , object : OnTraceListener{
                    override fun onFinished(lineID: Int, linePoints: List<LatLng>, distance: Int, waitingtime: Int) {

                        ILog.e("---轨迹返回-: ${points.size}   ${traceList.size}    ${linePoints.size}  distance: $distance  waitingtime: $waitingtime")

                        uiThread {
                            resultMapHelper.setPoints(linePoints)
                        }
                    }

                })
            }
        }

        sportDetailShortInfoView.setSportInfo(sportId)
    }

    private fun initListeners(){
/*        detailTextView.setOnClickListener {
            val dialog = SportDetailDialog(this)
            dialog.show()
            dialog.setSportId(sportId)
        }*/


    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
