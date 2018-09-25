package xujf.demo.example.customview.dials_view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_dials_view.*
import xujf.demo.example.customview.R

class DialsViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dials_view)
    }

    fun showDpv(v: View) {
        dpv.setScore(120f,60f)
        cpv.setScore(120f,60f, "继续努力")
    }
}
