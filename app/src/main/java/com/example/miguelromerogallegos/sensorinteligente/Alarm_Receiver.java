package com.example.miguelromerogallegos.sensorinteligente;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by miguelromerogallegos on 18/07/17.
 */

public class Alarm_Receiver extends BroadcastReceiver {
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    public void onReceive(Context context, Intent intent) {

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Toast.makeText(context, "Alarma Activada", Toast.LENGTH_SHORT).show();
        myRef.child("3encender").setValue(1);

    }
}
