package com.example.cursoandroidclase2.uid.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cursoandroidclase2.R

class InputActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_input)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getIntExtra("id", -1)
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        val titleEdit = findViewById<EditText>(R.id.title)
        val bodyEdit = findViewById<EditText>(R.id.body)

        // titleEdit.text = Editable.Factory.getInstance().newEditable(title ?: "")
        // bodyEdit.text = Editable.Factory.getInstance().newEditable(body ?: "")

        titleEdit.text = (title ?: "").toEditable()
        bodyEdit.text = (body ?: "").toEditable()



        findViewById<Button>(R.id.backButton).setOnClickListener {
            // finish()
            onBackPressedDispatcher.onBackPressed()
        }
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val titleValue = titleEdit.text.toString()
            val bodyValue = bodyEdit.text.toString()

            if (titleValue.isBlank() || bodyValue.isBlank()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
            } else {
                // Toast.makeText(this, "Hola $titleValue!", Toast.LENGTH_LONG).show()
                val resultIntent = Intent().apply {
                    putExtra("id", id)
                    putExtra("title", titleValue)
                    putExtra("body", bodyValue)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }
    }
    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)
}