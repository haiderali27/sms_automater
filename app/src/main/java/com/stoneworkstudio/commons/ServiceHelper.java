package com.stoneworkstudio.commons;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import static androidx.core.content.ContextCompat.getSystemService;

public class ServiceHelper extends Service {
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    public boolean serviceStatus(Class<?> serviceClass){
        return isMyServiceRunning(serviceClass);
    }
    public ServiceHelper(){

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
