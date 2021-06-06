package com.rrvq.listacompras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrvq.listacompras.sesion.Login;

public class SplashScream extends AppCompatActivity {

    ImageView imageView;

    Animation animacion1, animacion2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_scream);


        imageView = findViewById(R.id.imageView);

        // animaciones de logo y texto
        animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_arriba);
        animacion2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        imageView.setAnimation(animacion1);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        }, 1000);

    }
}
