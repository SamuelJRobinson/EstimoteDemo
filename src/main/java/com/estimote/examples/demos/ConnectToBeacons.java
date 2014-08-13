package com.estimote.examples.demos;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import java.util.List;

/**
 * Created by samuel.j.robinson on 12/08/2014.
 */
public class ConnectToBeacons {



    private static final String TAG = ListBeaconsActivity.class.getSimpleName();

    public static final String EXTRAS_TARGET_ACTIVITY = "extrasTargetActivity";
    public static final String EXTRAS_BEACON = "extrasBeacon";

    private static final int REQUEST_ENABLE_BT = 1234;
    //    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);

    private BeaconManager beaconManager;
    private LeDeviceListAdapter adapter;
    public List<Beacon> beacons;

    public List <Beacon> ConnectToBeacons(Context context) {


        // Configure BeaconManager.
        beaconManager = new BeaconManager(context);

        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            Toast.makeText(context, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        } else {
            connectToService(context);
        }
        beaconManager.setRangingListener(new BeaconManager.RangingListener()
        {
           @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

            }
        });
        return beacons;
    }



    private void onStop() {
        try {
            beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
        } catch (RemoteException e) {
            Log.d(TAG, "Error while stopping ranging", e);
        }
    }

    private void connectToService(final Context context) {
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
                } catch (RemoteException e) {
                    Toast.makeText(context, "Cannot start ranging, something terrible happened",
                            Toast.LENGTH_LONG).show();
                    Log.e(TAG, "Cannot start ranging", e);
                }
            }
        });
    }
}
