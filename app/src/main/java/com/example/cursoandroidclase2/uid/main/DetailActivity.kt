package com.example.cursoandroidclase2.uid.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cursoandroidclase2.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        findViewById<TextView>(R.id.titleTextView).text = title
        findViewById<TextView>(R.id.bodyTextView).text = body

        findViewById<Button>(R.id.backButton).setOnClickListener {
            // finish()
            onBackPressedDispatcher.onBackPressed()
        }
    }
    override fun finish() {
        super.finish()
        //overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        // overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        overridePendingTransition(R.anim.zoom_fade_in, R.anim.zoom_fade_out)
    }

}