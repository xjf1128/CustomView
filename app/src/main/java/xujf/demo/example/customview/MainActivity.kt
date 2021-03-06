package xujf.demo.example.customview

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import xujf.demo.example.customview.circle_menu.CircleMenuActivity
import xujf.demo.example.customview.dials_view.DialsViewActivity
import xujf.demo.example.customview.xlink_list.LinkListActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun toCircleMenu(v: View) {
        val intent = Intent(this, CircleMenuActivity::class.java)
        startActivity(intent)
    }

    fun toLinkList(v: View) {
        val intent = Intent(this, LinkListActivity::class.java)
        startActivity(intent)
    }

    fun toDialsView(v: View) {
        val intent = Intent(this, DialsViewActivity::class.java)
        startActivity(intent)
    }
}
