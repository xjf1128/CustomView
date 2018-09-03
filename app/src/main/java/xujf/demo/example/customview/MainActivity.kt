package xujf.demo.example.customview

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import xujf.demo.example.customview.circle_menu.CircleMenuActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toCircleMenu(v: View) {
        val intent = Intent(this, CircleMenuActivity::class.java)
        startActivity(intent)
    }
}
