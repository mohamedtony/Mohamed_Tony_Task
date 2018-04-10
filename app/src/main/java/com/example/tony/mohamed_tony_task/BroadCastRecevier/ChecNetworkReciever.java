package com.example.tony.mohamed_tony_task.BroadCastRecevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ChecNetworkReciever extends BroadcastReceiver {
    public static boolean stateChanged=false;

    final  private  OnNetworkChangeInterface onNetworkChangeInterface;

    public ChecNetworkReciever(OnNetworkChangeInterface onNetworkChangeInterface){
        this.onNetworkChangeInterface=onNetworkChangeInterface;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        stateChanged = activeNetwork != null && activeNetwork.isConnected();

        onNetworkChangeInterface.onChangedNetwork(stateChanged);
    }
    public interface OnNetworkChangeInterface{
        public void onChangedNetwork(boolean changed);
    }

}
