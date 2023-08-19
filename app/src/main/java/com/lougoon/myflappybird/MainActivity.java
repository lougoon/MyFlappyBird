package com.lougoon.myflappybird;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Boolean game_already_started,compteur,distance;
    private TextView score_text;
    private int gravity = 2;
    private int score,screenWidth,screenHeight,velocity,flappyY,milieu_screen,milieu_screen_h;
    private ImageView flappy,cara_top_1,cara_bottom_1,cara_top_2,cara_bottom_2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout backgroundView = findViewById(R.id.view_clic);
        flappy = findViewById(R.id.flappy);
        cara_top_1 = findViewById(R.id.cara_top_1);
        cara_bottom_1 = findViewById(R.id.cara_bottom_1);
        cara_top_2 = findViewById(R.id.cara_top_2);
        cara_bottom_2 = findViewById(R.id.cara_bottom_2);
        score_text = findViewById(R.id.score_text);

        // Get screen dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        milieu_screen= screenWidth/2;
        screenHeight = displayMetrics.heightPixels;
        milieu_screen_h = screenHeight/2;

        game_already_started = false;

        backgroundView.setOnClickListener(view -> {

            if (!game_already_started) {
                game_already_started = true;
                start_game();
                back_stuff_loop();
            }
            velocity= -20;

        });
    }

    public void back_stuff_loop() {

        Runnable back_stuff = new Runnable() {
            @Override
            public void run(){
                flappyY +=velocity;
                velocity+= gravity;
                flappy.setY(flappyY);
                cara_collision(cara_top_1,cara_bottom_1);
                cara_collision(cara_top_2,cara_bottom_2);
                avancer_cara();
                handler.postDelayed(this,25);
            }
        };

        handler.post(back_stuff);

    }

    private void set_dimension(ImageView imageView,int height){

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = 250; // en pixels
        layoutParams.height = height; // en pixels
        imageView.setLayoutParams(layoutParams);
    }

    private void start_game(){

        distance = false;
        compteur = false;

        flappyY = milieu_screen_h;
        flappy.setY(milieu_screen_h);

        initialisation_cara(cara_top_1,cara_bottom_1);
        initialisation_cara(cara_top_2,cara_bottom_2);
    }

    private void cara_collision(ImageView cara_top,ImageView cara_bottom) {

        if (flappy.getX() < cara_top.getX() + cara_top.getWidth() &&
                flappy.getX() + flappy.getWidth() > cara_top.getX() &&
                (flappy.getY() < cara_top.getHeight() || flappy.getY() + flappy.getHeight() > cara_bottom.getY())) {
            endgame();
        }
        // hors de zone de point reinitialise la valeur de compteur sur false
        if (flappy.getX()<cara_top_1.getX() || flappy.getX()>cara_top_1.getX() + cara_top_1.getWidth()){
            if (flappy.getX()<cara_top_2.getX() || flappy.getX()>cara_top_2.getX() + cara_top_2.getWidth()){
                compteur = false;
            }
        }

        // zone de point on ajoute un point et ensuite on definie compteur false pour plus attribué de point
        if (flappy.getX()>cara_top.getX() && flappy.getX()<cara_top.getX() + cara_top.getWidth()){
            if (!compteur) {
                score+=1;
                score_text.setText("score : "+ score);

            }
            compteur = true;
        }
        if (cara_top.getX()+ 2*cara_top.getWidth()-100<0){
            initialisation_cara(cara_top,cara_bottom);
        }
    }
    private void avancer_cara(){
        if (milieu_screen-cara_bottom_1.getWidth()+100>cara_bottom_1.getX()) {
            distance = true;
        }
        if (distance) {
            cara_top_2.setX(cara_top_2.getX() - 6);
            cara_bottom_2.setX(cara_top_2.getX());
        }
        cara_top_1.setX(cara_top_1.getX() - 6);
        cara_bottom_1.setX(cara_top_1.getX());
    }
    private void endgame(){
        start_game();
        velocity=0;
        score = 0;
        score_text.setText("score : "+score);
    }
    private void initialisation_cara(ImageView cara_top,ImageView cara_bottom){
        Random random = new Random();
        int space = 250;

        // Générer un chiffre aleatoire en fct da la hauteur
        int min = 300;
        int max = screenHeight - 300;
        int random_height = random.nextInt(max - min) + min;

        //cara initialisation
        set_dimension(cara_top,random_height-space);
        set_dimension(cara_bottom,screenHeight-random_height-space);
        cara_bottom.setX(screenWidth+100);
        cara_top.setX(screenWidth+100);
    }
}