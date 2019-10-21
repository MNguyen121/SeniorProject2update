package com.example.seniorprojectfr;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String Tag = "SecondActivity";
    BluetoothAdapter mBluetoothAdaptor;
    Button btnEnableDisable_Discoverable;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdaptor mDeviceListAdaptor;
    ListView lvNewDevices;

    // Create a BroadcastReceiver for ACTION_FOUND.

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdaptor.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdaptor.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(Tag, "onRecieve: State OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(Tag, "eonRecieve: State Turning OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(Tag, "onRecieve: State ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(Tag, "onRecieve: State Turning ON");
                        break;
                }
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                // BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(mBluetoothAdaptor.ACTION_SCAN_MODE_CHANGED)) {
                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, mBluetoothAdaptor.ERROR);

                switch (mode) {
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:
                        Log.d(Tag, "mBroadcastReceiver2: Discoverability Enabled.");
                        break;
                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:
                        Log.d(Tag, "mBroadcastReceiver2: Discoverability Enabled. Able to Receive connection");
                        break;
                    case BluetoothAdapter.SCAN_MODE_NONE:
                        Log.d(Tag, "mBroadcastReceiver2: Discoverability Enabled. Not able to Receive connection.");
                        break;
                    case BluetoothAdapter.STATE_CONNECTING:
                        Log.d(Tag, "mBroadcastReceiver2: Connecting...");
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        Log.d(Tag, "mBroadcastReceiver2: Connected");
                        break;
                }
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                // BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                String deviceName = device.getName();
//                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(Tag, "onReceive: ACTION FOUND");

            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases
                //case1:bonded already
                if(mDevice.getBondState()== BluetoothDevice.BOND_BONDED){
                    Log.d(Tag, "onReceive: BOND_BONDED");
                }
                //case2: creating new bond
                if(mDevice.getBondState()== BluetoothDevice.BOND_BONDING){
                    Log.d(Tag, "onReceive: BOND_BONDING");
                }
                //case3: bond broken
                if(mDevice.getBondState()== BluetoothDevice.BOND_NONE){
                    Log.d(Tag, "onReceive: BOND_NONE");
                }
            }
        }
    };


    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(Tag, "onReceive: ACTION FOUND");

            if(action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(Tag, "onReceive:" + device.getName() + ": "+ device.getAddress());
                mDeviceListAdaptor =  new DeviceListAdaptor(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdaptor);

            }
        }
    };


    @Override
    protected void onDestroy() {
        Log.d(Tag, "onDestroyed Called");
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver4);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btnOnOFF = (Button) findViewById(R.id.btnOnOFF);
        mBluetoothAdaptor = BluetoothAdapter.getDefaultAdapter();
        btnEnableDisable_Discoverable = (Button)findViewById(R.id.btnEnableDiscoverable_on_off);
        lvNewDevices = (ListView)findViewById(R.id.lvNewDevices);
        mBTDevices = new ArrayList<>();

//        IntentFilter filter = new IntentFilter((BluetoothDevice.ACTION_BOND_STATE_CHANGED));
//        registerReceiver(mBroadcastReceiver4, filter);

        mBluetoothAdaptor = BluetoothAdapter.getDefaultAdapter();

        lvNewDevices.setOnItemClickListener(SecondActivity.this);

        btnOnOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(Tag, "onClick: Enabling/Disabling bluetooth");
                enableDisabledBT();
                }

        });
    }



    public void enableDisabledBT(){
        if(mBluetoothAdaptor == null){
            Log.d(Tag, "enabledDisableBT: Does not have Bluetooth capabilities");
        }
        if(!mBluetoothAdaptor.isEnabled()){
            Log.d(Tag, "enabledDisableBT: Enabling BT");

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(mBluetoothAdaptor.isEnabled()){
            Log.d(Tag, "enabledDisableBT: Disabling BT");
            mBluetoothAdaptor.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
    }


    public void btnEnableDisable_Discoverable(View view) {
        Log.d(Tag, "btnEnableDisable_Discoverable: Making Device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdaptor.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver2,intentFilter);
    }

    public void btnDiscover(View view) {
        Log.d(Tag, "btnDiscover: Looking for Unpaired Devices");
        //if disconvering and click button, cancel and start
        if(mBluetoothAdaptor.isDiscovering()){
            mBluetoothAdaptor.cancelDiscovery();
            Log.d(Tag, "btnDiscover: Cancelled Discovery");

            //Check BT Permissions in Manifest;
            checkBTPermissions();
            mBluetoothAdaptor.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }
        if(!mBluetoothAdaptor.isDiscovering())
        {
            checkBTPermissions();
            mBluetoothAdaptor.isDiscovering();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        //if not discovering, start
//        if(mBluetoothAdaptor.isDiscovering()){
//            //Check BT Permissions in Manifest;
//            checkBTPermissions();
//            mBluetoothAdaptor.startDiscovery();
//            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//            registerReceiver(mBroadcastReceiver4, discoverDevicesIntent);
//        }
    }
    /*method requires for all devices funning API23+;
    android must programmatically check the permissions for bluetooth. Put proper permission from manifest
    is not enough
    * */
    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if(permissionCheck != 0){
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            }
            else{
                Log.d(Tag, "CheckBTPermissions: No need to check permissions. SDK version <LOLLIPpop");
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //first cancel discovery because its very memory intensive
        mBluetoothAdaptor.cancelDiscovery();

        Log.d(Tag, "You Clicked on a device");
        String deviceName = mBTDevices.get(i).getName();
        String deviceAddress = mBTDevices.get(i).getAddress();

        Log.d(Tag, "OnItemClick: deviceName = " + deviceName);
        Log.d(Tag, "OnItemClick: deviceAddress = " + deviceAddress);

        //create the bond
        //requires API 19 or above
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
            Log.d(Tag,"trying to pair with " + deviceName);
            mBTDevices.get(i).createBond();
        }
    }
}