package com.example.safecity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {
    private  ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();



        progressBar= findViewById(R.id.progressBar);

        Thread thread = new Thread (new Runnable() {
            @Override
            public void run() {
                doWork();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }
        );
        thread.start();

    }

    public void doWork()
    {
        int progress;
        for(progress =20; progress <=100; progress = progress +20)
        {
            try
            {
                progressBar.setProgress(progress);
                Thread.sleep(1000);


            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

    }


}