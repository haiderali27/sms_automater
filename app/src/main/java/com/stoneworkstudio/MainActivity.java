package com.stoneworkstudio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.stoneworkstudio.constants.Constants;
import com.stoneworkstudio.service.SMSService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final Intent[] POWERMANAGER_INTENTS = {
            new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
            new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
            new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
            new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
            new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
            new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
            new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
            new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity"))
    };

   // SMS sms = new SMS("+923000806458");
    private static TextView messageDisplayTextView;
    private Button checkServiceStatus;
    private Button startService;
    private Button stopService;


    private boolean isSvcRunning = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (Intent intent : POWERMANAGER_INTENTS)
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                // show dialog to ask user action
                break;
            }
        setContentView(R.layout.activity_main);
        checkServiceStatus = (Button) findViewById(R.id.btnCheckServiceStatus);
        startService = (Button) findViewById(R.id.btnStartService);
        stopService = (Button) findViewById(R.id.btnStopService);
        checkServiceStatus.setOnClickListener(this);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        messageDisplayTextView = (TextView) findViewById(R.id.txtTest);
        messageDisplayTextView.setText("Check the status of serivce by Check Service Status");
        //final SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage("+923219517478",null,"andriod sms",null,null);
        SmsManager smsmanager = SmsManager.getDefault();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.FOREGROUND_SERVICE},1 );
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BROADCAST_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS}, 1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCheckServiceStatus: {
                String serviceStatus = isMyServiceRunning(SMSService.class)?"running":"not running";
                Toast.makeText(this, "Service Status:"+ serviceStatus, Toast.LENGTH_SHORT).show();
            }break;
            case R.id.btnStartService: {
                if(isMyServiceRunning(SMSService.class)){
                    Toast.makeText(this, "Service is already running", Toast.LENGTH_SHORT).show();

                }
                else {
                    Intent intent = new Intent(this, SMSService.class);
                    intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(intent);
                }
            }break;
            case R.id.btnStopService: {
                stopService(new Intent(this, SMSService.class));
            }break;
            default:{}break;
        }
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
