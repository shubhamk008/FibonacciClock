/*

This activity is to preview the dock mode.
A button (appears like an eye) in the action
bar enables this method from MainActivity.
This preview goes only show a fibonacci clock
in full screen to use as a Dock.

-Shubham Kumar
www.github.com/shubhamk008/fibonacciclock
www.talknology.in

*/

package in.talknology.sly.fibonacciclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static in.talknology.sly.fibonacciclock.MainActivity.factor;
import static in.talknology.sly.fibonacciclock.MainActivity.hourFlag;
import static in.talknology.sly.fibonacciclock.MainActivity.minFlag;

public class dockView extends AppCompatActivity {

    int hour, min, sec;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();                                                               // hides action bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  // hides notification bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);                       // keeps screen persistent- no timeout since dock mode is used for long duration
        setContentView(R.layout.activity_dock);                                                     // selects activity_dock.xml for previewing

        Toast.makeText(this, "Press back to quit", Toast.LENGTH_SHORT).show();                        // gives direction to quit from full screen

        // ------------- This is first run for dock view

        Calendar c = Calendar.getInstance();                                                        // new object of type Calendar
        hour = c.get(Calendar.HOUR);                                                                // get hour (in 24-HR format) in int
        min = c.get(Calendar.MINUTE);                                                               // get minute in int

        if (hour == 0)                                                                              // if it is 00:00 hrs, then
            hour = 12;                                                                              // set hour to 12
        while (min % 5 != 0)                                                                        // checking for nearest lowest multiple of 5 in min. Ex: 54->50,59->55,etc.
        {
            min--;
        }
        min = min / 5;                                                                              // min=min/5 for further computation

        for (int i = 0; i < 5; i++)                                                                 // for checking presence of all factors: 5,3,2,1,1 is there in hour/min or not
        {
            if (hour - factor[i] >= 0)                                                              // if hour has a factor[i] present in it, set hourFlag[i] to show factor[i]'s presence
            {
                hourFlag[i] = 1;                                                                    // set flag
                hour = hour - factor[i];                                                            // remove the factor of i, remaining factor be checked in next iterations
            }
            if (min - factor[i] >= 0)                                                               // if min has a factor[i] present in it, set minFlag[i] to show factor[i]'s presence
            {
                minFlag[i] = 1;                                                                     // set flag
                min = min - factor[i];                                                              // remove the factor of i, remaining factor be checked in next iterations
            }
        }

        imageChange();                                                                              // change colors according to time
        resetTime();                                                                                // reset time

        // ------------- End of first run for dock view

        Timer timer = new Timer();                                                                  // Keep updating time
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                Calendar c = Calendar.getInstance();
                min = c.get(Calendar.MINUTE);
                sec = c.get(Calendar.SECOND);

                if (min % 5 == 0 && sec == 0) {                                                     // if minute multiple of 5 ans sec is zero, then update view

                    hour = c.get(Calendar.HOUR);
                    min = c.get(Calendar.MINUTE);

                    if (hour == 0)
                        hour = 12;
                    while (min % 5 != 0)                                                            // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
                    {
                        min--;
                    }
                    min = min / 5;                                                                  // min=min/5 for further computation

                    for (int i = 0; i < 5; i++)                                                     // for checking presence of all factors: 5,3,2,1,1 is there in hour/min or not
                    {
                        if (hour - factor[i] >= 0)                                                  // if hour has a factor[i] present in it, set hourFlag[i] to show factor[i]'s presence
                        {
                            hourFlag[i] = 1;                                                        // set flag
                            hour = hour - factor[i];                                                // remove the factor of i, remaining factor be checked in next iterations
                        }
                        if (min - factor[i] >= 0)                                                   // if min has a factor[i] present in it, set minFlag[i] to show factor[i]'s presence
                        {
                            minFlag[i] = 1;                                                         // set flag
                            min = min - factor[i];                                                  // remove the factor of i, remaining factor be checked in next iterations
                        }
                    }

                    hour = c.get(Calendar.HOUR);
                    if (hour == 0) hour = 12;
                    min = c.get(Calendar.MINUTE);
                    while (min % 5 != 0)                                                            // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
                    {
                        min--;
                    }
                    min = min / 5;                                                                  // min=min/5 for further computation


                    runOnUiThread(new Runnable() {                                                  // Update UI via new thread on same activity

                        public void run() {

                            imageChange();                                                          // change colors according to time
                            resetTime();                                                            // reset time
                        }
                    });
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);                                              // update every 1000 millisecond (1 sec) to check for min%5 is 0 and sec is 0 with 0 delay
    }

    public void imageChange() {                                                                     // image change method

        ImageView box5x5 = (ImageView) findViewById(R.id.imageView);                                // initialise box5x5 object of type ImageView, corresponding image is imageView3
        ImageView box3x3 = (ImageView) findViewById(R.id.imageView9);                               // initialise box3x3 object of type ImageView, corresponding image is imageView4
        ImageView box2x2 = (ImageView) findViewById(R.id.imageView8);                               // initialise box2x2 object of type ImageView, corresponding image is imageView5
        ImageView box1x1a = (ImageView) findViewById(R.id.imageView2);                              // initialise box1x1a object of type ImageView, corresponding image is imageView7
        ImageView box1x1b = (ImageView) findViewById(R.id.imageView3);                              // initialise box1x1b object of type ImageView, corresponding image is imageView36

        box5x5.setImageResource(android.R.color.black);                                             // initialise all boxes to transparent colour
        box3x3.setImageResource(android.R.color.black);
        box2x2.setImageResource(android.R.color.black);
        box1x1a.setImageResource(android.R.color.black);
        box1x1b.setImageResource(android.R.color.black);

        // 5x5 box                                                                                  // 5x5 box

        if (hourFlag[0] == 1 && minFlag[0] == 1)                                                    // if both hour and min have a factor of 5
            box5x5.setImageResource(android.R.color.holo_blue_light);                               // then display blue
        else if (hourFlag[0] == 1)                                                                  // if hour has a factor of 5
            box5x5.setImageResource(android.R.color.holo_red_light);                                // then display red
        else if (minFlag[0] == 1)                                                                   // if min has a factor of 5
            box5x5.setImageResource(android.R.color.holo_green_light);                              // then display green

        // 3x3 box                                                                                  // 3x3 box

        if (hourFlag[1] == 1 && minFlag[1] == 1)                                                    // if both hour and min have a factor of 3
            box3x3.setImageResource(android.R.color.holo_blue_light);                               // then display blue
        else if (hourFlag[1] == 1)                                                                  // if hour has a factor of 3
            box3x3.setImageResource(android.R.color.holo_red_light);                                // then display red
        else if (minFlag[1] == 1)                                                                   // if min has a factor of 3
            box3x3.setImageResource(android.R.color.holo_green_light);                              // then display green

        // 2x2 box                                                                                  // 2x2 box

        if (hourFlag[2] == 1 && minFlag[2] == 1)                                                    // if both hour and min have a factor of 2
            box2x2.setImageResource(android.R.color.holo_blue_light);                               // then display blue
        else if (hourFlag[2] == 1)                                                                  // if hour has a factor of 2
            box2x2.setImageResource(android.R.color.holo_red_light);                                // then display red
        else if (minFlag[2] == 1)                                                                   // if min has a factor of 2
            box2x2.setImageResource(android.R.color.holo_green_light);                              // then display green

        // 1x1a box                                                                                 // 1x1a box

        if (hourFlag[3] == 1 && minFlag[3] == 1)                                                    // if both hour and min have a factor of 1
            box1x1a.setImageResource(android.R.color.holo_blue_light);                              // then display blue
        else if (hourFlag[3] == 1)                                                                  // if hour has a factor of 1
            box1x1a.setImageResource(android.R.color.holo_red_light);                               // then display red
        else if (minFlag[3] == 1)                                                                   // if min has a factor of 1
            box1x1a.setImageResource(android.R.color.holo_green_light);                             // then display green

        // 1x1b box                                                                                 // 1x1b box

        if (hourFlag[4] == 1 && minFlag[4] == 1)                                                    // if both hour and min have a factor of 1
            box1x1b.setImageResource(android.R.color.holo_blue_light);                              // then display blue
        else if (hourFlag[4] == 1)                                                                  // if hour has a factor of 1
            box1x1b.setImageResource(android.R.color.holo_red_light);                               // then display red
        else if (minFlag[4] == 1)                                                                   // if min has a factor of 1
            box1x1b.setImageResource(android.R.color.holo_green_light);                             // then display green

    }

    public void resetTime() {                                                                       // reset time method

        hour = -1;
        min = -1;                                                                                   // hour and min set to -1
        for (int i = 0; i < 5; i++) {
            hourFlag[i] = 0;                                                                        // all elements in hourFlag set to 0
            minFlag[i] = 0;                                                                         // all elements in minFlag set to 0
        }
    }

}