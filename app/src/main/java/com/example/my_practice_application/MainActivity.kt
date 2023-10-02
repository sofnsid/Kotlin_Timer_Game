package com.example.my_practice_application

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import java.util.*
import kotlin.math.abs

class MainActivity : AppCompatActivity() {
    var p_num = 3
    var k = 1
    val point_list = mutableListOf<Float>()
    var isBlind = false


    fun start(){
        setContentView(R.layout.activity_start)

        MobileAds.initialize(this) {}
        val mAdView : AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        val tv_pnum : TextView = findViewById(R.id.tv_pnum)
        val btn_minus : Button = findViewById(R.id.btn_minus)
        val btn_plus : Button = findViewById(R.id.btn_plus)
        val btn_start : Button = findViewById(R.id.btn_start)
        val btn_blind : Button = findViewById(R.id.btn_blind)
        tv_pnum.text = p_num.toString()

        btn_blind.setOnClickListener{
            isBlind = !isBlind
            if(isBlind == true){
                btn_blind.text = "BLIND MODE ON"
            }
            else{
                btn_blind.text = "BLIND MODE OFF"
            }
        }

        btn_minus.setOnClickListener{
            p_num--
            if(p_num == 0){
                p_num = 1
            }
            tv_pnum.text = p_num.toString()
        }
        btn_plus.setOnClickListener{
            p_num++
            tv_pnum.text = p_num.toString()
        }
        btn_start.setOnClickListener{
            main()
        }

    }

    fun main(){
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}
       val mAdView : AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        var timerTask : Timer? = null

        val tv : TextView = findViewById(R.id.tv_pnum)
        val tv_t : TextView = findViewById(R.id.tv_timer)
        val tv_p : TextView = findViewById(R.id.tv_point)
        val tv_people : TextView = findViewById(R.id.tv_people)
        val btn : Button = findViewById(R.id.btn_start)
        val btn_i : Button = findViewById(R.id.btn_i)
        btn.text = "시작"
        val random_box = Random()
        val num = random_box.nextInt(1001)
        tv.text = (num.toFloat()/100).toString()
        var sec : Int = 0
        var stage : Int = 1
        tv_people.text = "참가자 $k"
        val bg_main : ConstraintLayout = findViewById(R.id.bg_main)
        val color_list = mutableListOf<String>("#48EF0836", "#45E6630C", "#45FFC000", "#4B00FF04", "#4B001AFF", "#601703C7", "#4B8C03C7E")
        val color_sel = color_list.get((k-1)%7)
        bg_main.setBackgroundColor(Color.parseColor(color_sel))

        btn_i.setOnClickListener {
            isBlind = false
            k = 1
            point_list.clear()
            start()
        }
        btn.setOnClickListener{
            stage++
            if(stage == 2){
                timerTask = kotlin.concurrent.timer(period = 10){
                    sec++
                    runOnUiThread{
                        if (isBlind == false){
                            tv_t.text = (sec.toFloat()/100).toString()
                        }
                        else if(isBlind == true && stage == 2){
                            tv_t.text = "???"
                        }

                    }

                }
                btn.text = "정지"
            }
            else if(stage == 3){
                tv_t.text = (sec.toFloat()/100).toString()
                timerTask?.cancel()
                val point = abs(sec-num).toFloat()/100
                point_list.add(point)
                tv_p.text = point.toString()
                btn.text = "다음"
                stage = 0
            }
            else if(stage == 1){
                if(k < p_num){
                    k++
                    main()
                }
                else {
                    end()
                }

            }
        }
    }
    fun end(){
        setContentView(R.layout.activity_end)

        MobileAds.initialize(this) {}
        val mAdView : AdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)


        val tv_last : TextView = findViewById(R.id.tv_last)
        val tv_lastpoint : TextView = findViewById(R.id.tv_lastpoint)
        val btn_init : Button = findViewById(R.id.btn_init)

        tv_lastpoint.text = (point_list.maxOrNull()).toString()
        var index_last = point_list.indexOf(point_list.maxOrNull())
        tv_last.text = "참가자 "+(index_last+1).toString()

        btn_init.setOnClickListener {
            isBlind = false
            point_list.clear()
            k = 1
            start()

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        start()

    }
}