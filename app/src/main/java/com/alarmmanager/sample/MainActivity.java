package com.alarmmanager.sample;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.AlarmManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.alarmmanager.diff.MyReceiver;
import com.alarmmanager.sample.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private String NOTIFICATION_EVENT_ACTION = "expo.modules.notifications.NOTIFICATION_EVENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlert();
            }
        });

        registerReceiver(new MyReceiver(), new IntentFilter());
    }

    private void setAlert() {
        long time = System.currentTimeMillis() + 10000;
        Date date =  new Date(time);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(NOTIFICATION_EVENT_ACTION,
                Uri.parse("expo-notifications://notifications/").buildUpon()
                        .appendPath("scheduled")
                        .appendPath(String.valueOf(date))
                        .appendPath("trigger")
                        .build());
        intent.setComponent(new ComponentName(this.getPackageName(), MyReceiver.class.getName()));
        intent.putExtra("DATE", String.valueOf(date));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S || alarmManager.canScheduleExactAlarms()) {
            AlarmManagerCompat.setExactAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
            );
            Log.e("SchoolarAlert", "Schedule exact");
        } else {
            AlarmManagerCompat.setAndAllowWhileIdle(
                    alarmManager,
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
            );
            Log.e("SchoolarAlert", "Schedule set");
        }
        Log.e("SchoolarAlert", "Scheduled at " + date);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}