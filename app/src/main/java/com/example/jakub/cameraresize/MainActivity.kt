package com.example.jakub.cameraresize

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.scanButton).apply {
            setOnClickListener{
                startActivity(Intent(this.context, Scanner::class.java))
            }
        }

    

    }
}
