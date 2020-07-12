package au.com.wuli.wuli;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.bluetooth.BluetoothDevice.ACTION_FOUND;
import static android.bluetooth.BluetoothDevice.BOND_BONDED;
import static android.bluetooth.BluetoothDevice.BOND_BONDING;
import static java.lang.System.in;

public class BluetoothActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "BluetoothActivity";

    String popupbtTitle ="Searching nearest Wuli";
    String popupbtMessage ="Please Wait...";

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    BluetoothAdapter mBluetoothAdapter;
    ProgressDialog dialog;
    android.support.v7.app.AlertDialog inputdialog;
    public int devid;
    boolean buttonstate;
    boolean isRegistered = false;
    boolean searching = true;
    boolean isbondcreated = false;
    boolean isConnected = false;
    boolean discoverystarted = false;
    boolean mConnected;
    boolean chehcbtrefresh;
    BluetoothDevice selecteddevice;
    Integer uniadd = null;
    public List<BluetoothGattCharacteristic>abcd = new ArrayList<>();
    boolean gattconnect=false;

    public List<String> mDevices = new ArrayList<String>();
    public List<String> Logslist = new ArrayList<String>();
    public ArrayList<BluetoothDevice> rawmDevices = new ArrayList<>();
    ListView lvnewDevices;
    BluetoothGattCharacteristic mbluetoothcharacterstic;
    BluetoothGattService mBluetoothGattService;
    Context mContext;

    BluetoothGatt mBluetoothGatt;

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("de8345e1-b8a4-4843-b177-165b9d889f05");

    public static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    public static final UUID RX_CHAR_UUID = UUID.fromString("6E400002-B5A3-F393-E0A9-E50E24DCCA9E");
    public static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");


    // Create a BroadcastReceiver for ACTION_FOUND
    private BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;

                }
            }
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (!mDevices.contains(device.getName() + "\n" + device.getAddress() + "\n")&&device.getName()!= "null") {
                    mDevices.add(device.getName() + "\n" + device.getAddress() + "\n");
                    rawmDevices.add(device);
                    Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());


                }


            }
            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
        }
    };

    private final BluetoothGattCallback mBlurtoothCallback = new BluetoothGattCallback() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //bluetooth is connected so discover services
                Log.d(TAG, "CLIENT IS CONNECTED");
                mBluetoothGatt.discoverServices();

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //Bluetooth is disconnected
                Log.d(TAG, "ClIENT DISCONNECTED");
            }
        }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {


            if (status != BluetoothGatt.GATT_SUCCESS) {
                // services are discoverd
                Log.d(TAG, "UNABLE TO FIND BLE SERVICES");
            }
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // services are discoverd

                Log.d(TAG, "FOUND THESE MANY OF BLE SERVICES " + mBluetoothGatt.getServices().toString());
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                byte[] incomingblevaue = characteristic.getValue();

                Log.d(TAG, "Incoming Value is " + incomingblevaue.toString());


            }
            if (status != BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Not Able to read values");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {

        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, "Succesfully sent the values");

            } else {

                //Process error...
                Log.d(TAG, "Error while sending values");
            }

        }
    };


    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        drawerLayout = findViewById(R.id.bluetoothdrawerid);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.na_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        Switch btnONOFF = (Switch) findViewById(R.id.Bluetoothswitch);
        buttonstate = btnONOFF.isChecked();

        final Button searchbt = (Button) findViewById(R.id.refreshbt);
        searchbt.setEnabled(true);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        dialog  = new ProgressDialog(BluetoothActivity.this);





        searchbt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mDevices.clear();
                rawmDevices.clear();
                enable_popup(popupbtTitle,popupbtMessage);

                if (mBluetoothAdapter.isEnabled()) {
                    searchbt.setEnabled(false);
                    discoverbluetooth();
                }

                final Handler btsearchhandle = new Handler();
                btsearchhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 10000 milli seconds
                        disable_popup();
                        searchbt.setEnabled(true);
                        listrefresh();

                    }
                }, 10000);

            }
        });


        btnONOFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: enabling/disabling bluetooth.");
                enableDisableBT();
            }
        });




        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 200 milli seconds

                checkbtswitch();

                //btconnecting();

                handler.postDelayed(this, 100);
            }
        }, 200);


    }






    public void listrefresh() {

        ListView lv = (ListView) findViewById(R.id.simpleListView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                mDevices);

        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(BluetoothActivity.this);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    public void discoverbluetooth() {
        Log.d(TAG, "discovery:Looking for unpaired BT devices");
        if (mBluetoothAdapter.isDiscovering()) {

            mBluetoothAdapter.startDiscovery();
            //mBluetoothAdapter.startLeScan(mLeScanCallback);
            IntentFilter discoverDeviceintent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver1, discoverDeviceintent);
        }
        if (!mBluetoothAdapter.isDiscovering()) {

            //check BT permissions in manifest
            checkBTPermissions();

            mBluetoothAdapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver1, discoverDevicesIntent);
        }

    }

    //    public void btconnecting(){
//
//
//        if (uniadd != null && rawmDevices.get(uniadd).getBondState()== BOND_BONDED && isConnected==false){
//        mBluetoothConnection.startClient(rawmDevices.get(uniadd), MY_UUID_INSECURE);}
//        isConnected =true;
//
//
//    }
    public void EnableDisable_Discoverable(View view) {
        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
        startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        registerReceiver(mBroadcastReceiver1, intentFilter);

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: called.");
        super.onDestroy();
        // unregisterReceiver(mBroadcastReceiver1);
        if (isRegistered == true) {
            unregisterReceiver(mBroadcastReceiver1);
        }
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Message msg = Message.obtain();
            String action = intent.getAction();
            if (ACTION_FOUND.equals(action)) {
                //Found, add to a device list
                Toast.makeText(BluetoothActivity.this, ACTION_FOUND,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void startSearching() {
        Log.i("Log", "in the start searching method");
        IntentFilter intentFilter = new IntentFilter(ACTION_FOUND);
        BluetoothActivity.this.registerReceiver(myReceiver, intentFilter);
        mBluetoothAdapter.startDiscovery();
    }

    public boolean createBond(BluetoothDevice btDevice)
            throws Exception {
        Class class1 = Class.forName("android.bluetooth.BluetoothDevice");
        Method createBondMethod = class1.getMethod("createBond");
        Boolean returnValue = (Boolean) createBondMethod.invoke(btDevice);
        return returnValue.booleanValue();
    }
    public void enable_popup_timer(String title, String message,int time){

        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        final Handler popuphandler = new Handler();
        popuphandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                dialog.hide();
            }
        }, time);

    }
    public void enable_popup(String title, String message){
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public  void disable_popup(){
        dialog.hide();
    }

    public void checkbtswitch() {
        Switch checkswitch = (Switch) findViewById(R.id.Bluetoothswitch);
        Button btsearch =(Button)findViewById(R.id.refreshbt);
        ListView ltview = (ListView)findViewById(R.id.simpleListView);
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                checkswitch.setChecked(false);
                buttonstate = false;
                btsearch.setEnabled(false);
                ltview.setEnabled(false);
                chehcbtrefresh = false;



            } else if (mBluetoothAdapter.isEnabled()) {
                checkswitch.setChecked(true);
                buttonstate = true;
                btsearch.setEnabled(true);
                ltview.setEnabled(true);
                if(chehcbtrefresh == false){
                    btsearch.performClick();
                }
                chehcbtrefresh = true;

//                if (searching == true) {
//                    Toast.makeText(this, "Searching Wuli", Toast.LENGTH_SHORT).show();
//                    searching = false;
//                }


            }else{
                Toast.makeText(this, "Fatal error..", Toast.LENGTH_SHORT).show();
                Log.i("Log", "FATAL ERROR IN CHECKBTSWITCH()");
            }
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean writeRXCharacteristic(byte[] value) {


        BluetoothGattService RxService = mBluetoothGatt.getService(RX_SERVICE_UUID);

        if (RxService == null) {
            //Service not supported
            Log.d(TAG, "rXservice failed");
            return false;
        }
        BluetoothGattCharacteristic RxChar = RxService.getCharacteristic(RX_CHAR_UUID);
        if (RxChar == null) {
            // service not supported
            Log.d(TAG, "rXcharacterfailed");
            return false;
        }
        RxChar.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
        RxChar.setValue(value);
        return mBluetoothGatt.writeCharacteristic(RxChar);

    }


    public void enableDisableBT() {


        Switch testswitch = (Switch) findViewById(R.id.Bluetoothswitch);
        if (mBluetoothAdapter == null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if (!mBluetoothAdapter.isEnabled() && testswitch.isChecked()) {
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
            isRegistered = true;



        }
        if (mBluetoothAdapter.isEnabled() && !testswitch.isChecked()) {
            Log.d(TAG, "enableDisableBT: disabling BT.");
            mBluetoothAdapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
            isRegistered = true;

        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (toggle.onOptionsItemSelected(item)) {

            return true;
        }


        return super.onOptionsItemSelected(item);

    }

    public void clickEvent(MenuItem v) {


        if (v.getItemId() == R.id.nav_home) {
            try {
                Intent intent1 = new Intent(this, MainActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }
        }
        if (v.getItemId() == R.id.nav_logs) {
            try {
                Intent intent1 = new Intent(this, LogsActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }

        }
        if (v.getItemId() == R.id.nav_lcation) {
            try {
                Intent intent1 = new Intent(this, LocationActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }

        }
        if (v.getItemId() == R.id.nav_connect) {
            try {
                Intent intent1 = new Intent(this, BluetoothActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }

        }

        if (v.getItemId() == R.id.nav_share) {
            try {
                Intent intent1 = new Intent(this, ShareActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }

        }
        if (v.getItemId() == R.id.nav_help) {
            try {
                Intent intent1 = new Intent(this, HelpActivity.class);
                this.startActivity(intent1);
            } finally {
                this.finish();
            }

        }
    }

    /**
     * This method is required for all devices running API23+
     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions
     * in the manifest is not enough.
     * <p>
     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkBTPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        } else {
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }

    }
    private void logindexer(){

        SharedPreferences logs = getSharedPreferences("LOGS",0);


        SharedPreferences.Editor editor = logs.edit();

        String logget = logs.getString("index-","");
        if(TextUtils.isEmpty(logget)){
            Integer newind=0;
            String insertnewindex = newind.toString();

            editor.putString("index-", insertnewindex);
            editor.apply();
        }else if(!TextUtils.isEmpty(logget)){
            String oldind = logget;
            Integer conoldind= Integer.parseInt(oldind);
            conoldind= conoldind + 1;
            String outnewind = conoldind.toString();
            editor.putString("index-", outnewind);
            editor.apply();

        }



        Log.d(TAG, "Current situation of INDEX --"+ logget);



    }


    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)


    @Override
    public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {

        final Button btsearch =(Button)findViewById(R.id.refreshbt);

        selecteddevice = null;
        selecteddevice = rawmDevices.get(i);



        PopupMenu btmenu = new PopupMenu(this, view);



        btmenu.getMenuInflater().inflate(R.menu.navigation_bt,btmenu.getMenu());


        btmenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            int selectmenu;
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                selectmenu = item.getItemId();




                if(selectmenu == R.id.bt_lock ){

                    AlertDialog.Builder alert = new AlertDialog.Builder(BluetoothActivity.this);
                    alert.setTitle("Wuli password");
                    alert.setMessage("Enter your Wuli password to Lock: ");
                    alert.setIcon(R.drawable.ic_help_black_24dp);
                    final EditText inputText = new EditText(BluetoothActivity.this);
                    inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    //inputText.setTextSize(4);
                    alert.setView(inputText);




                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                final String passvalue = inputText.getText().toString();



                                if(inputText.length()==4){

                                mBluetoothGatt = selecteddevice.connectGatt(BluetoothActivity.this, false, mBlurtoothCallback);
                                enable_popup_timer("Connecting Wuli","Initializing..",7000);


                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.UK);
                                String format = simpleDateFormat.format(new Date());
                                String msg= "  --Sent Lock Wuli Signals";
                                String logout= format+msg;


                                    logindexer();
                                    SharedPreferences logs = getSharedPreferences("LOGS",0);
                                    String logg = logs.getString("index-","");

                                    SharedPreferences.Editor editor = logs.edit();
                                    editor.putString(logg, logout);
                                    editor.apply();


                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms

                                        mBluetoothGattService = mBluetoothGatt.getService(selecteddevice.getUuids()[1].getUuid());
                                        mbluetoothcharacterstic = mBluetoothGattService.getCharacteristic(selecteddevice.getUuids()[1].getUuid());
                                        Log.d(TAG, "found CHARA  "+ abcd.size());
                                        String Lock="1";

                                        String out = passvalue+Lock;
                                        boolean abcd = writeRXCharacteristic(out.getBytes());
                                        //Toast.makeText(BluetoothActivity.this, "passwordout"+out, Toast.LENGTH_SHORT).show();


                                        final Handler handler7 = new Handler();
                                        handler7.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Do something after 5s = 5000ms
                                                enable_popup_timer("Locking Wuli","Initializing..",2000);

                                                mBluetoothGatt.disconnect();


                                            }
                                        }, 2000);



                                    }
                                }, 5000);}else {

                                    Toast.makeText(BluetoothActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                                }



                            }
                        });

                        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });


                    alert.show();














                }if(selectmenu == R.id.bt_unlock){

                    AlertDialog.Builder alert = new AlertDialog.Builder(BluetoothActivity.this);
                    alert.setTitle("Wuli password");
                    alert.setMessage("Enter your Wuli Password to Unlock: ");
                    alert.setIcon(R.drawable.ic_help_black_24dp);
                    final EditText inputText = new EditText(BluetoothActivity.this);
                    inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    //inputText.setTextSize(4);
                    alert.setView(inputText);




                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String passvalue = inputText.getText().toString();



                            if(inputText.length()==4){

                                mBluetoothGatt = selecteddevice.connectGatt(BluetoothActivity.this, false, mBlurtoothCallback);
                                enable_popup_timer("Connecting Wuli","Initializing..",7000);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.UK);
                                String format = simpleDateFormat.format(new Date());
                                String msg= "  --Sent Unlock Wuli Signals";
                                String logout= format+msg;


                                logindexer();
                                SharedPreferences logs = getSharedPreferences("LOGS",0);
                                String logg = logs.getString("index-","");

                                SharedPreferences.Editor editor = logs.edit();
                                editor.putString(logg, logout);
                                editor.apply();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms

                                        mBluetoothGattService = mBluetoothGatt.getService(selecteddevice.getUuids()[1].getUuid());
                                        mbluetoothcharacterstic = mBluetoothGattService.getCharacteristic(selecteddevice.getUuids()[1].getUuid());
                                        Log.d(TAG, "found CHARA  "+ abcd.size());
                                        String UnLock="0";

                                        String out = passvalue+UnLock;
                                        //Toast.makeText(BluetoothActivity.this, "passwordout"+out, Toast.LENGTH_SHORT).show();

                                        boolean abcd = writeRXCharacteristic(out.getBytes());


                                        final Handler handler7 = new Handler();
                                        handler7.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Do something after 5s = 5000ms
                                                enable_popup_timer("Unlocking Wuli","Initializing..",2000);

                                                mBluetoothGatt.disconnect();


                                            }
                                        }, 2000);



                                    }
                                }, 5000);}else {

                                Toast.makeText(BluetoothActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });


                    alert.show();







                }if(selectmenu==R.id.bt_reset){


                    AlertDialog.Builder alert = new AlertDialog.Builder(BluetoothActivity.this);
                    alert.setTitle("Wuli password");
                    alert.setMessage("Enter your old Wuli Password to reset: ");
                    alert.setIcon(R.drawable.ic_help_black_24dp);
                    final EditText inputText = new EditText(BluetoothActivity.this);
                    inputText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    inputText.setEnabled(true);



                    //inputText.setTextSize(4);
                    inputText.setEnabled(true);
                    alert.setView(inputText);





                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String passvalue = inputText.getText().toString();




                            if(inputText.length()==4){
                                AlertDialog.Builder alert1 = new AlertDialog.Builder(BluetoothActivity.this);
                                alert1.setTitle("Wuli password");
                                alert1.setMessage("Enter your new Wuli Password: ");
                                alert1.setIcon(R.drawable.ic_help_black_24dp);

                                final EditText inputText1 = new EditText(BluetoothActivity.this);
                                inputText1.setInputType(InputType.TYPE_CLASS_NUMBER);
                                inputText1.setRawInputType(Configuration.KEYBOARD_12KEY);

                                inputText1.setEnabled(true);
                                alert1.setView(inputText1);

                                alert1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        final String newvalue = inputText1.getText().toString();

                                        if (inputText1.length() == 4) {

                                            mBluetoothGatt = selecteddevice.connectGatt(BluetoothActivity.this, false, mBlurtoothCallback);
                                            enable_popup_timer("Connecting Wuli", "Initializing..", 7000);

                                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.UK);
                                            String format = simpleDateFormat.format(new Date());
                                            String msg= "  --Sent Change Password Signals";
                                            String logout= format+msg;


                                            logindexer();
                                            SharedPreferences logs = getSharedPreferences("LOGS",0);
                                            String logg = logs.getString("index-","");

                                            SharedPreferences.Editor editor = logs.edit();
                                            editor.putString(logg, logout);
                                            editor.apply();

                                            final Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Do something after 5s = 5000ms

                                                    mBluetoothGattService = mBluetoothGatt.getService(selecteddevice.getUuids()[1].getUuid());
                                                    mbluetoothcharacterstic = mBluetoothGattService.getCharacteristic(selecteddevice.getUuids()[1].getUuid());
                                                    Log.d(TAG, "found CHARA  " + abcd.size());
                                                    String Reset = "2";

                                                    String out = passvalue + Reset + newvalue;
                                                    //Toast.makeText(BluetoothActivity.this, "Password changed", Toast.LENGTH_SHORT).show();

                                                    boolean abcd = writeRXCharacteristic(out.getBytes());


                                                    final Handler handler7 = new Handler();
                                                    handler7.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            // Do something after 5s = 5000ms
                                                            enable_popup_timer("Changing Password", "Trying to change password..", 2000);

                                                            mBluetoothGatt.disconnect();


                                                        }
                                                    }, 2000);


                                                }
                                            }, 5000);
                                        } else {

                                            Toast.makeText(BluetoothActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });

                                alert1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {


                                    }
                                });

                                alert1.show();






                               }else {

                                Toast.makeText(BluetoothActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });


                    alert.show();






                }if(selectmenu==R.id.bt_forget){


                    AlertDialog.Builder alert = new AlertDialog.Builder(BluetoothActivity.this);
                    alert.setTitle("Wuli forgot password");
                    alert.setMessage("Enter your Unique Wuli ID to Reset password: ");
                    alert.setIcon(R.drawable.ic_help_black_24dp);
                    final EditText inputText = new EditText(BluetoothActivity.this);
                    inputText.setInputType(InputType.TYPE_CLASS_PHONE);
                    inputText.setRawInputType(Configuration.KEYBOARD_12KEY);
                    //inputText.setTextSize(4);
                    alert.setView(inputText);




                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            final String passvalue = inputText.getText().toString();



                            if(inputText.length()==4){

                                mBluetoothGatt = selecteddevice.connectGatt(BluetoothActivity.this, false, mBlurtoothCallback);
                                //enable_popup_timer("Connecting Wuli","Initializing..",2000);
                                enable_popup_timer("Resetting your Password","If your ID is successfully accepted, password will be changed to default 1234",7000);

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss", Locale.UK);
                                String format = simpleDateFormat.format(new Date());
                                String msg= "  --Sent Reset Password Signals";
                                String logout= format+msg;


                                logindexer();
                                SharedPreferences logs = getSharedPreferences("LOGS",0);
                                String logg = logs.getString("index-","");
                                SharedPreferences.Editor editor = logs.edit();
                                editor.putString(logg, logout);
                                editor.apply();

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Do something after 5s = 5000ms

                                        mBluetoothGattService = mBluetoothGatt.getService(selecteddevice.getUuids()[1].getUuid());
                                        mbluetoothcharacterstic = mBluetoothGattService.getCharacteristic(selecteddevice.getUuids()[1].getUuid());
                                        Log.d(TAG, "found CHARA  "+ abcd.size());
                                        String Lock="3";

                                        String out = passvalue+Lock;
                                        boolean abcd = writeRXCharacteristic(out.getBytes());
                                        //Toast.makeText(BluetoothActivity.this, "Password resetted", Toast.LENGTH_SHORT).show();


                                        final Handler handler7 = new Handler();
                                        handler7.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Do something after 5s = 5000ms
                                                enable_popup_timer("Resetting your Password","If your ID is successfully accepted, password will be changed to default 1234",2000);

                                                mBluetoothGatt.disconnect();


                                            }
                                        }, 2000);



                                    }
                                }, 5000);}else {

                                Toast.makeText(BluetoothActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();

                            }



                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                        }
                    });


                    alert.show();

                }



                return true;
            }
        });
        btmenu.show();


    }
}
