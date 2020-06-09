package com.example.lightswitchproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.R.layout.simple_list_item_1
import android.content.Context
import android.support.v7.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.dialog_add_device.*
import java.util.*

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        var list = ArrayList<String>()
        var ref = FirebaseDatabase.getInstance().reference

        buttonAddDevice.setOnClickListener {
            val context = this
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Insert information")
            val view = layoutInflater.inflate(R.layout.dialog_add_device, null)
            val deviceNameET = view.findViewById(R.id.editTextDeviceName) as EditText
            val deviceIpET = view.findViewById(R.id.editTextDeviceIP) as EditText
            builder.setView(view)
            builder.setPositiveButton(android.R.string.ok) { dialog, which ->
                if(deviceNameET.text.isBlank()||deviceIpET.text.isBlank()){
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("Error")
                    builder.setMessage("Device name and ip must be filled")
                    builder.setPositiveButton(android.R.string.ok){
                            dialog, which ->
                        dialog.cancel()
                    }
                    builder.show()
                }else {
                    val deviceNameHolder = deviceNameET.text.toString()
                    val deviceIPHolder = deviceIpET.text.toString()
                    ref.child("DEVICES").child(deviceNameHolder).child("IP").setValue(deviceIPHolder)
                        .addOnCompleteListener {
                            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                        }
                    ref.child("DEVICES").child(deviceNameHolder).child("MODE").setValue("MANUAL")
                }
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
                dialog.cancel()
            }
            builder.show()

        }
        ref.child("DEVICES").addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                list.clear()

                var device = p0.value as HashMap<String, Any>
                for(d in device.keys)
                    list.add("Device Name :\t" + d + "\n" + "Device Info :\t" + device[d])
                listViewDevice.adapter = ArrayAdapter(applicationContext, simple_list_item_1, list)
            }
        })
        listViewDevice.setOnItemClickListener {
                parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            val namadevice = item.toString().substringAfter("\t")
                .substringBefore("\n")
            val ipdevice = item.toString().substringAfter("Device Info :\t")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose Mode")
            builder.setPositiveButton("Auto"){
                    dialog, which ->

                ref.child("DEVICES").child(namadevice).child("MODE").setValue("AUTO")

            }

            builder.setNegativeButton("Manual"){
                    dialog, which ->
                ref.child("DEVICES").child(namadevice).child("MODE").setValue("MANUAL")
            }
            builder.setNeutralButton(android.R.string.cancel){
                    dialog, which ->
                dialog.cancel()
            }
            builder.show()
        }
        listViewDevice.setOnItemLongClickListener { parent, view, position, id ->
            val item = parent.getItemAtPosition(position)
            val namadevice = item.toString().substringAfter("\t")
                .substringBefore("\n")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Delete Data ?")
            builder.setPositiveButton(android.R.string.ok){
                    dialog, which ->
                ref.child("DEVICES").child(namadevice).setValue(null)
            }
            builder.setNeutralButton(android.R.string.cancel){
                    dialog, which ->
                dialog.cancel()
            }
            builder.show()
            true
        }
    }
}



