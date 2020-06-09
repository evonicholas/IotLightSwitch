package com.example.lightswitchproject

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCredit.setText("Created by CL & EO 2019")
        var i :Int = 0


        logoView.setOnClickListener {
            Toast.makeText(this, "Logo Image",Toast.LENGTH_SHORT)
            i++
            if (i == 5){
                val intent = Intent(this, MonitorMenu::class.java)
                startActivity(intent)
            }

        }
        loginButton.setOnClickListener {
            val username = editTextusrname.text.toString().replace(" ","")
            val password = editTextpassword.text.toString()
            if(username.toLowerCase() == "admin" && password == "celab" ){
                val intent = Intent(this, MonitorMenu::class.java)
                startActivity(intent)
            }
            else{
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Login Failed")
                builder.setMessage("Username or Password incorrect")
                builder.setPositiveButton("OK"){dialog, which ->
                    dialog.cancel()
                }
                val mdialog = builder.create()
                mdialog.show()
            }
        }

    }
}
