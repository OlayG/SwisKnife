package com.example.admin.swisknife;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements StopWatchFragment.OnFragmentInteractionListener, SmsFragment.OnFragmentInteractionListener {

    @BindView(R.id.rvTaskList)
    RecyclerView rvTaskList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;
    ProjectTaskAdapter projectTaskAdapter;
    List<ProjectTask> taskList = new ArrayList<>();
    FragmentManager fm = getSupportFragmentManager();
    @BindView(R.id.flFrame1)
    FrameLayout flFrame1;
    @BindView(R.id.flFrame2)
    FrameLayout flFrame2;

    TextView tvStopWatch;
    Button btnReset, btnStart, btnStop;
    EditText etPhoneNumber, etTextMessage;

    long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    int Seconds, Minutes, MilliSeconds;
    Handler handler;
    Thread thread;

    IntentFilter intentFilter;

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getExtras().getString("message"), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        handler = new Handler();

        layoutManager = new LinearLayoutManager(this);
        itemAnimator = new DefaultItemAnimator();
        rvTaskList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        rvTaskList.setItemAnimator(itemAnimator);

        getTasks();
        projectTaskAdapter = new ProjectTaskAdapter(taskList);
        rvTaskList.setAdapter(projectTaskAdapter);
        projectTaskAdapter.notifyDataSetChanged();

        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");

        String notification = getIntent().getStringExtra("PDF");

        if(notification != null && notification.equals("pdf")){
            flFrame2.setVisibility(View.GONE);
            PdfViewerFragment pdf = new PdfViewerFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.flFrame1,
                    pdf, "MainActivity").commit();
        }

    }

    @Subscribe
    public void onEvent(MessageEvent messageEvent) {

        switch (Integer.parseInt(messageEvent.getCustomMessage())) {
            case 0:
                flFrame2.setVisibility(View.VISIBLE);
                StopWatchFragment stopWatchButtons = new StopWatchFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flFrame2,
                        stopWatchButtons, "MainActivity").commit();

                StopWatchViewFragment stopWatchView = new StopWatchViewFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flFrame1,
                        stopWatchView, "MainActivity").commit();

                resetTimer();
                break;
            case 1:
                flFrame2.setVisibility(View.GONE);
                SmsFragment sendText = new SmsFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flFrame1,
                        sendText, "MainActivity").commit();
                break;
            case 2:
                flFrame2.setVisibility(View.GONE);
                NotificationFragment n = new NotificationFragment();
                fm.beginTransaction().replace(R.id.flFrame1, n, "MainActivity").commit();
                break;
            case 3:
                flFrame2.setVisibility(View.GONE);
                AlertDialogTasksFragment dialogTasks = new AlertDialogTasksFragment();
                fm.beginTransaction().replace(R.id.flFrame1, dialogTasks, "MainActivity").commit();
                break;
            case 4:
                flFrame2.setVisibility(View.GONE);
                final PictureDialogFragment dialogFragment = new PictureDialogFragment();
                dialogFragment.show(fm, "Dialog Fragment");
                thread = new Thread(){
                    @Override
                    public void run() {
                        try {
                            synchronized (this){
                                wait(3000);
                            }
                        } catch (InterruptedException ex){

                        }
                        dialogFragment.dismiss();
                    }
                };
                thread.start();
                break;
            case 5:
                flFrame2.setVisibility(View.GONE);
                PdfViewerFragment pdf = new PdfViewerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.flFrame1,
                        pdf, "MainActivity").commit();
                break;
        }
    }

    private void getTasks() {

        taskList.add(new ProjectTask("Timer", "Simple stopwatch application", 7));
        taskList.add(new ProjectTask("Messenger", "Send text messages to friends", 6));
        taskList.add(new ProjectTask("Notification application", "View and interact with Notifications", 5));
        taskList.add(new ProjectTask("Alert Dialog", "View a alert dialog", 4));
        taskList.add(new ProjectTask("Dialog Fragment", "View a picture in a dialog", 3));
        taskList.add(new ProjectTask("PDF Viewer", "Lets you view pdf files", 2));
    }

    public Runnable runnable = new Runnable() {

        public void run() {

            MillisecondTime = SystemClock.uptimeMillis() - StartTime;

            UpdateTime = TimeBuff + MillisecondTime;

            Seconds = (int) (UpdateTime / 1000);

            Minutes = Seconds / 60;

            Seconds = Seconds % 60;

            MilliSeconds = (int) (UpdateTime % 1000);

            tvStopWatch.setText("" + Minutes + ":"
                    + String.format("%02d", Seconds) + ":"
                    + String.format("%03d", MilliSeconds));

            handler.postDelayed(this, 0);
        }

    };

    @Override
    public void onFragmentInteraction(String btnPressed, String TAG) {

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop= (Button) findViewById(R.id.btnStop);
        btnReset = (Button) findViewById(R.id.btnReset);
        tvStopWatch = (TextView) findViewById(R.id.tvStopWatch);

        switch (TAG) {
            case "StopWatchFragme" +
                    "nt":
                if(btnPressed == "start"){

                    StartTime = SystemClock.uptimeMillis();
                    handler.postDelayed(runnable, 0);
                    btnReset.setEnabled(false);
                    btnStart.setEnabled(false);
                    btnStop.setEnabled(true);

                } else if(btnPressed == "stop") {

                    TimeBuff += MillisecondTime;
                    handler.removeCallbacks(runnable);
                    btnReset.setEnabled(true);
                    btnStart.setEnabled(true);
                    btnStop.setEnabled(false);

                } else if(btnPressed == "reset") {

                    resetTimer();

                }
                break;
            case "SmsFragment":
                sendSMSMessage();
                etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
                etTextMessage = (EditText) findViewById(R.id.etTextMessage);
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(etPhoneNumber.getText().toString(), null, ""+etTextMessage.getText(), null, null);
                    Toast.makeText(MainActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Toast.makeText(MainActivity.this, "Message failed to send", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void resetTimer() {

        MillisecondTime = 0L;
        StartTime = 0L;
        TimeBuff = 0L;
        UpdateTime = 0L;
        Seconds = 0;
        Minutes = 0;
        MilliSeconds = 0;

        tvStopWatch.setText("00:00:00");
    }

    protected void sendSMSMessage(){

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.SEND_SMS)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.SEND_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(intentReceiver);
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this,
                            "No permission granted!", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }

    }
}
