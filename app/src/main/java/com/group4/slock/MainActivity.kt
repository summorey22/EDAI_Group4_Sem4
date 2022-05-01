package com.group4.slock

import android.Manifest.permission.*
import android.annotation.TargetApi
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Build.VERSION.SDK
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private var myList: RecyclerView? = null
    private var mDeviceList = ArrayList<Array<String>>()
    private var anotherList = ArrayList<String>()
    var nfcAdapter: NfcAdapter? = null
    var bluetoothAdapter: BluetoothAdapter? = null
    var myProgressBar: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myProgressBar = findViewById(R.id.progressBar)
        myList = findViewById(R.id.recyclerview)


        checkNFCStatus()
        checkBTStatus()
        findBTDevices()

        Log.d("PP", mDeviceList.toString())

        if (BluetoothAdapter.getDefaultAdapter().state == BluetoothAdapter.STATE_CONNECTED){
            move()
        }
    }
    private fun checkNFCStatus(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null){
            Toast.makeText(this, "NFC not Found", Toast.LENGTH_SHORT).show()
        }else{
            if(!nfcAdapter!!.isEnabled){
                Toast.makeText(this, "Please turn on the NFC", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_NFC_SETTINGS)
                startActivity(intent)
            }
        }
    }

    private fun checkBTStatus(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter!!.isEnabled){
            Toast.makeText(this, "Please turn on the Bluetooth", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_BLUETOOTH_SETTINGS)
            startActivity(intent)
        }
    }

    fun move(){
        val intent = Intent(this@MainActivity, MainActivity2::class.java)
        startActivity(intent)
    }

    private fun findBTDevices(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (ActivityCompat.checkSelfPermission(
                this,
                BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return ActivityCompat.requestPermissions(this@MainActivity, arrayOf(
                BLUETOOTH_CONNECT,
                BLUETOOTH_SCAN,
                BLUETOOTH_ADMIN,
            ),1)
        }
        bluetoothAdapter?.startDiscovery()

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(mReceiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                val action = intent.action
                if (BluetoothDevice.ACTION_FOUND == action) {
                    val device = intent
                        .getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                    if (ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(this@MainActivity, arrayOf(
                            BLUETOOTH_CONNECT,
                            BLUETOOTH_SCAN,
                            BLUETOOTH_ADMIN
                        ), 1)
                    }
                    myProgressBar?.isVisible = false

                    val buff = arrayOf(device!!.name, device.address)
                    if(!anotherList.contains(device.address)){
                        mDeviceList.add(
                            buff
                        )
                    }
                    anotherList.add(device.address)
                    myList?.adapter = MyAdaptor(mDeviceList)
                    myList?.layoutManager = LinearLayoutManager(this@MainActivity)

                }else{
                    //TODO: Add code for android version less than 12
                }
            }
        }
    }

}