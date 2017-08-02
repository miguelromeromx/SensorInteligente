package com.example.miguelromerogallegos.sensorinteligente;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Main2Activity extends AppCompatActivity {

    Button btn_horain, btn_horafin, so_b;
    TextView et_horain, et_horafin, tv_alarm;
    Switch switch2;

    FirebaseDatabase database;
    DatabaseReference myRef;

    Context context;
    PendingIntent pending_intent, pending_intent2;

    AlarmManager alarm_manager, alarm_manager2;
    private int horain, horafin, minutosin, minutosfin ;

    public String horainfb, minutoinfb, horafinfb, minutofinfb;

    String hour_string1, minute_string1, hour_string2, minute_string2;

    public static final int NOTIFICACION_ID=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn_horain = (Button) findViewById(R.id.btn_horain);
        btn_horafin = (Button) findViewById(R.id.btn_horafin);
        so_b = (Button) findViewById(R.id.so_btn);

        et_horain = (TextView) findViewById(R.id.et_horain);
        et_horafin = (TextView) findViewById(R.id.et_horafin);
        tv_alarm = (TextView) findViewById(R.id.tv_alarm);

        switch2 = (Switch) findViewById(R.id.switch2);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        this.context = this;
        alarm_manager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarm_manager2 = (AlarmManager)getSystemService(ALARM_SERVICE);

        final Intent my_intent = new Intent(Main2Activity.this, Alarm_Receiver.class);
        final Intent my_intent2 = new Intent(Main2Activity.this, Alarm_Receiver2.class);


        pending_intent = PendingIntent.getBroadcast(Main2Activity.this, 0, my_intent,0);
        pending_intent2 = PendingIntent.getBroadcast(Main2Activity.this, 0, my_intent2,0);


        myRef.child("3horain").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                horainfb = String.valueOf(dataSnapshot.getValue());

                myRef.child("3minutoin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        minutoinfb = String.valueOf(dataSnapshot.getValue());
                        et_horain.setText(horainfb + ":" + minutoinfb + " hrs.");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        myRef.child("3horafin").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                horafinfb = String.valueOf(dataSnapshot.getValue());

                myRef.child("3minutofin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        minutofinfb = String.valueOf(dataSnapshot.getValue());
                        et_horafin.setText(horafinfb + ":" + minutofinfb + " hrs.");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myRef.child("3encender").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (Integer.valueOf(dataSnapshot.getValue().toString())==0) {

                    //Toast.makeText(getApplicationContext(), "Sensor Apagado", Toast.LENGTH_SHORT).show();
                    switch2.setChecked(false);
                }

                else {

                    //Toast.makeText(getApplicationContext(),"Sensor Encendido", Toast.LENGTH_SHORT).show();
                    switch2.setChecked(true);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){

                    //Toast.makeText(getApplicationContext(),"Sensor Encendido", Toast.LENGTH_SHORT).show();
                    myRef.child("3encender").setValue(1);
                    Toast.makeText(context, "Alarma Activada", Toast.LENGTH_SHORT).show();


                }
                else {


                    //5Toast.makeText(getApplicationContext(),"Sensor Apagado", Toast.LENGTH_SHORT).show();
                    myRef.child("3encender").setValue(0);
                    Toast.makeText(context, "Alarma Desactivada", Toast.LENGTH_SHORT).show();

                }

            }
        });


        myRef.child("3alarma").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (Integer.valueOf(dataSnapshot.getValue().toString())==1){

                    tv_alarm.setText("¡¡ALERTA!! MOVIMIENTO DETECTADO EN ZONA 1");
                    tv_alarm.setTextColor(Color.RED);
                    tv_alarm.setTextSize(30);

                    Intent intent = new Intent(Main2Activity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); /////
                    PendingIntent pendingIntent= PendingIntent.getActivity(Main2Activity.this,0,intent,PendingIntent.FLAG_ONE_SHOT);
                    //PendingIntent pendingIntent= PendingIntent.getActivity(Main2Activity.this,0,intent,0);
                    Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

                    NotificationCompat.Builder builder= new NotificationCompat.Builder(Main2Activity.this);
                    builder.setSmallIcon(R.mipmap.icono);
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setSound(soundUri);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher_round));
                    builder.setContentTitle("¡ALERTA!");
                    builder.setContentText("¡¡MOVIMIENTO DETECTADO EN ZONA 1!!");
                    builder.setSubText("Sensor 1 detectó movimiento");



                    NotificationManager notificationManager= (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(NOTIFICACION_ID,builder.build());


                }

                else {

                    tv_alarm.setText("Sin detección de movimiento en este momento.");
                    tv_alarm.setTextColor(Color.DKGRAY);
                    tv_alarm.setTextSize(30);
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        btn_horain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                horain = c.get(Calendar.HOUR_OF_DAY);
                minutosin = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(Main2Activity.this, new TimePickerDialog.OnTimeSetListener(){


                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                        c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c.set(Calendar.MINUTE,minute);
                        c.set(Calendar.SECOND,0);

                        minute_string1 = String.valueOf(minute);
                        hour_string1 = String.valueOf(hourOfDay);

                        if (minute < 10) {
                            minute_string1 = "0" + String.valueOf(minute);
                        }

                        if (hourOfDay < 10){
                            hour_string1 = "0" + String.valueOf(hourOfDay);
                        }

                        et_horain.setText(hour_string1 + ":" + minute_string1 + " hrs.");
                        myRef.child("3horain").setValue(hour_string1);
                        myRef.child("3minutoin").setValue(minute_string1);

                        pending_intent = PendingIntent.getBroadcast(Main2Activity.this, 0, my_intent,0);

                        alarm_manager.setRepeating(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),1000*60*60*24,pending_intent);


                    }
                }, horain, minutosin, false);

                timePickerDialog.show();


            }
        });


        btn_horafin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c2 = Calendar.getInstance();

                horafin = c2.get(Calendar.HOUR_OF_DAY);
                minutosfin = c2.get(Calendar.MINUTE);


                TimePickerDialog timePickerDialog = new TimePickerDialog(Main2Activity.this, new TimePickerDialog.OnTimeSetListener(){


                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        c2.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        c2.set(Calendar.MINUTE,minute);
                        c2.set(Calendar.SECOND,0);

                        minute_string2 = String.valueOf(minute);
                        hour_string2 = String.valueOf(hourOfDay);

                        if (minute < 10) {
                            minute_string2 = "0" + String.valueOf(minute);
                        }

                        if (hourOfDay < 10){
                            hour_string2 = "0" + String.valueOf(hourOfDay);
                        }

                        et_horafin.setText(hour_string2 + ":" + minute_string2 + " hrs.");
                        myRef.child("3horafin").setValue(hour_string2);
                        myRef.child("3minutofin").setValue(minute_string2);

                        pending_intent2 = PendingIntent.getBroadcast(Main2Activity.this, 0, my_intent2,0);
                        alarm_manager2.setRepeating(AlarmManager.RTC_WAKEUP,c2.getTimeInMillis(),1000*60*60*24,pending_intent2);


                    }
                }, horafin, minutosfin, false);

                timePickerDialog.show();

            }
        });

        so_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mAuth.signOut();
                Intent mIntent = new Intent(Main2Activity.this,MainActivity.class);

                startActivity(mIntent);


            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.mAuth.signOut();
        Intent mIntent = new Intent(Main2Activity.this,MainActivity.class);

        startActivity(mIntent);

    }
}
