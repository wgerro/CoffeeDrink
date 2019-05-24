package com.example.gerro.gra;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tooltip.Tooltip;

public class MainActivity extends AppCompatActivity {
    TextView times;
    Toast toast;
    TextView points;
    Button add_point;
    Integer counter;
    Integer highScore = 0;
    Tooltip tooltip1;
    boolean first_game = true;
    boolean resume_game = false;
    DataBase database;

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("ssss", "onResume()");

    }

    @Override
    protected void onStart() {
        Log.w("ssss", "onStart()");
        super.onStart();
    }
    @Override
    protected void onRestart() {

        super.onRestart();
        Log.w("ssss", "onRestart()");
    }

    @Override
    protected void onPause() {

        if(toast != null) {
            toast.cancel();
        }
        super.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w("ssss", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        times = (TextView) findViewById(R.id.times);
        points = (TextView) findViewById(R.id.points);
        add_point = (Button) findViewById(R.id.add_point);
        TooltipCompat.setTooltipText(add_point, "Pokaz");
        tooltip1 = new Tooltip.Builder(add_point).setCancelable(true).setText("Kliknij, aby wypić kawę!").setBackgroundColor(Color.parseColor("#FFFFFF")).setGravity(Gravity.TOP).show();

        database = new DataBase(this);
        highScore = database.getResults();
        add_point.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(first_game) {
                    gameStart();
                    tooltip1.dismiss();
                }
                if(resume_game) {
                    counter++;
                    points.setText(counter.toString());
                }
            }
        });
    }

    public void gameStart() {
        resume_game = true;
        first_game = false;
        counter = 0;
        new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                String v = String.format("%02d", millisUntilFinished / 1000);
                times.setText("00:" + v);
                if((millisUntilFinished / 1000) == 0) {
                    gameStop();

                }
            }

            public void onFinish() {
                counter = 0;
                times.setText("00:10");
                points.setText(counter.toString());
            }
        }.start();
    }

    public void gameStop() {
        resume_game = false;

        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
        alert.setCanceledOnTouchOutside(false);
        alert.setCancelable(false);
        alert.setTitle("KONIEC");
        alert.setMessage("Udało Ci się tyle wypić kaw: " + counter);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "JESZCZE RAZ",
            new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    tooltip1.show();
                    first_game = true;
                }
            });
        alert.show();

        if(highScore < counter) {
            highScore = counter;
            database.insertResult(highScore);
        }


    }

    public void viewHighScore(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Najlepszy wynik: " + highScore.toString();
        int duration = Toast.LENGTH_SHORT;

        toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.RIGHT|Gravity.CENTER_HORIZONTAL, 20 , 20);
        //toast.getView().setBackgroundColor(Color.parseColor("#FFFFFF"));
        //TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
        //toastMessage.setTextColor(Color.BLACK);
        toast.show();
    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}
