package org.nativescript.ata.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.ObjectStreamException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    public void onClick(View view) {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                crashApplication();
                timer.cancel();
            }
        }, 100);
    }

    private static void crashApplication() {
        int random = ((int)(Math.random() * 100)) % 8;
        switch (random){
            case 0:
                Log.i("ATA Application", "Crashing the application by throwing a new RuntimeException...");
                throw new RuntimeException("Pseudo RuntimeException");
            case 1:
                Log.i("ATA Application", "Crashing the application by throwing a new NullPointerException...");
                throw new NullPointerException("Pseudo NullPointerException");
            case 2:
                Log.i("ATA Application", "Crashing the application by throwing a new IllegalStateException...");
                throw new IllegalStateException("Pseudo IllegalStateException");
            case 3:
                Log.i("ATA Application", "Crashing the application by throwing a new IllegalArgumentException...");
                throw new IllegalArgumentException("Pseudo IllegalArgumentException ");
            case 4:
                Log.i("ATA Application", "Crashing the application by throwing a new ClassCastException...");
                throw new ClassCastException("Pseudo ClassCastException");
            case 5:
                Log.i("ATA Application", "Crashing the application by throwing a new ArrayIndexOutOfBoundsException...");
                throw new ArrayIndexOutOfBoundsException("Pseudo ArrayIndexOutOfBoundsException");
            case 6:
                Log.i("ATA Application", "Crashing the application by throwing a new SecurityException...");
                throw new SecurityException("Pseudo SecurityException");
            case 7:
                Log.i("ATA Application", "Crashing the application by throwing a new IllegalThreadStateException...");
                throw new IllegalThreadStateException("Pseudo IllegalThreadStateException");
        }

        // Other ways to simulate a crash
        //Integer.parseInt(null);
        //onClick(null);
    }
}
