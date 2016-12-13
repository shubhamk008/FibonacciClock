/*
This activity is the beginning of the
applications's life. Performs first run,
auto-update, manual run & leverages a
switch to dock mode.

-Fibonacci Clock
MIT License
www.github.com/shubhamk008/fibonacciclock

-Developed by Shubham Kumar
www.github.com/shubhamk008
www.talknology.in
*/

package in.talknology.sly.fibonacciclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;                                                                          // for current time
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    // Global Declrations
    int hour = -1, min = -1;                                                                        // initialise hour and min to -1; both invalid
    static int[] factor = {5, 3, 2, 1, 1};                                                          // initialise factors to 5,3,2,1 & 1. Fixed throughout program.
    static int[] hourFlag = {0, 0, 0, 0, 0};                                                        // initialise hour flag to zeroes.
    static int[] minFlag = {0, 0, 0, 0, 0};                                                         // initialise min flag to zeroes.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstRun();

        final EditText editText2 = (EditText) findViewById(R.id.editText2);                         // get the id for edit text
        Button displayText = (Button) findViewById(R.id.button7);                                   // get the id for button
        displayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputTimeStr = editText2.getText().toString();                               // obtain entered string to inputTimeStr
                char[] inputTime = inputTimeStr.toCharArray();                                      // copy inputTimeStr to inputTime character array

                if (trueTime(inputTimeStr) == 0)                                                    // if time is correct or not
                {
                    timeErrorMessage();                                                             // incorrect time, displays suitable message
                } else {                                                                            // time is correct

                    String hourStr = "", minStr = "";                                               // initialise hour and min capture string to empty string
                    int i = 0;                                                                      // i is pointer to seek input
                    while (inputTime[i] != ':' && Character.isDigit(inputTime[i]))                  // as long as ':' is not encountered and it is a digit, keep adding it to hourStr
                    {
                        hourStr = hourStr + inputTime[i];                                           // append to hour string
                        i++;                                                                        // as long as ':' not encountered
                    }
                    i++;                                                                            // inputTime[i]==':' right now, after i++ inputTime[i]== first minute char
                    while (i < inputTime.length && Character.isDigit(inputTime[i]))                 // till the end for minute, as long as it is digit
                    {
                        minStr = minStr + inputTime[i];                                             // append to min string
                        i++;                                                                        // as long as length of input string char array
                    }

                    hour = Integer.parseInt(hourStr);                                               // Typecast hourStr string value to integer and store in hour global variable
                    min = Integer.parseInt(minStr);                                                 // Typecast minStr string value to integer and store in min global variable


                    if (finalTime() == 1)                                                           // if time is manipulated (or not) successfully, returns 1; if error then returns 0;
                    {
                        //Toast.makeText(getApplicationContext(), "Time is " + hour + ":" + min, Toast.LENGTH_SHORT).show();    // display the text that you entered in edit text, with hour and min modified

                        calcColour();                                                               // set hourFlag and minFlag arrays so that colour can be changed appropriately by imageChange method

                        //Toast.makeText(getApplicationContext(), "hour flags:"+hourFlag[0]+hourFlag[1]+hourFlag[2]+hourFlag[3]+hourFlag[4], Toast.LENGTH_SHORT).show();    // Toasts all hourFlag elements
                        // Toast.makeText(getApplicationContext(), "min flags:"+minFlag[0]+minFlag[1]+minFlag[2]+minFlag[3]+minFlag[4], Toast.LENGTH_SHORT).show();     // Toasts all minFlag elements

                        imageChange();                                                              // change image to suitable colour
                        resetTime();                                                                // reset time variables hour & min to -1 to avoid unwanted scenarios, sets hourFlag and minFlag arrays to zeroes
                    }

                }
            }
        });


        // ---------------- Auto time resetter at every 5th minute, ex: app start at 10:03-->refresh at 10:05, 10:10, etc.

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                Calendar c = Calendar.getInstance();
                min = c.get(Calendar.MINUTE);
                int sec = c.get(Calendar.SECOND);

                if (min % 5 == 0 && sec == 0) {

                    hour = c.get(Calendar.HOUR);
                    if (hour == 0) hour = 12;
                    min = c.get(Calendar.MINUTE);
                    while (min % 5 != 0)                                                            // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
                    {
                        min--;
                    }
                    min = min / 5;


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
                    min = min / 5;

                    runOnUiThread(new Runnable() {

                        public void run() {

                            imageChange();

                            resetTime();
                        }
                    });

                }

            }

        };

        timer.scheduleAtFixedRate(timerTask, 0, 1000);

    }


    public void firstRun() {
        Calendar c = Calendar.getInstance();                                                        // new object of type Calendar
        hour = c.get(Calendar.HOUR);                                                                // get hour (in 24-HR format) in int
        min = c.get(Calendar.MINUTE);                                                               // get minute in int

        if (hour == 0)
            hour = 12;
        while (min % 5 != 0)                                                                        // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
        {
            min--;
        }
        min = min / 5;                                                                              // min=min/5 for further computation

        calcColour();                                                                               // set hourFlag and minFlag arrays so that colour can be changed appropriately by imageChange method
        imageChange();                                                                              // change image to suitable colour
        resetTime();                                                                                // reset time variables hour & min to -1 to avoid unwanted scenarios, sets hourFlag and minFlag arrays to zeroes
    }

    public void currentTime(View v) {                                                               // Action for button current time. System time is to be taken

        Calendar c = Calendar.getInstance();                                                        // new object of type Calendar
        hour = c.get(Calendar.HOUR);                                                                // get hour (in 24-HR format) in int
        min = c.get(Calendar.MINUTE);                                                               // get minute in int

        while (min % 5 != 0)                                                                        // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
        {
            min--;
        }
        min = min / 5;                                                                              // min=min/5 for further computation

        calcColour();                                                                               // set hourFlag and minFlag arrays so that colour can be changed appropriately by imageChange method
        imageChange();                                                                              // change image to suitable colour
        resetTime();                                                                                // reset time variables hour & min to -1 to avoid unwanted scenarios, sets hourFlag and minFlag arrays to zeroes
    }

    public int finalTime()                                                                          // if time is manipulated (or not) successfully, returns 1; if error then returns 0;
    {                                                                                               // purposefully not used switch as small switching
        if (hour == 13)
            hour = 1;                                                                               // if in 24-HR format, convert to 12-HR format
        else if (hour == 14) hour = 2;
        else if (hour == 15) hour = 3;
        else if (hour == 16) hour = 4;
        else if (hour == 17) hour = 5;
        else if (hour == 18) hour = 6;
        else if (hour == 19) hour = 7;
        else if (hour == 20) hour = 8;
        else if (hour == 21) hour = 9;
        else if (hour == 22) hour = 10;
        else if (hour == 23) hour = 11;
        else if (hour == 0 | hour == 24) hour = 12;

        if (hour < 1 | hour > 12 | min < 0 | min > 59)                                              // if value is out of bound, return error message
        {
            timeErrorMessage();                                                                     // displays error message: "Invalid Input! Try again"
            return 0;                                                                               // returns 0 to mark failure of finalTime
        } else                                                                                      // value in bound
        {
            while (min % 5 != 0)                                                                    // checking for nearest lowest multiple of 5 in min. Ex: 54=50,59=55,etc.
            {
                min--;
            }
            min = min / 5;                                                                          // Divide by 5 allows to calculate for minFlag array elements
            return 1;                                                                               // success of this method: finalTime
        }
    }


    public void timeErrorMessage()                                                                  // to print error message
    {
        Toast.makeText(this, "Invalid Input! Try again", Toast.LENGTH_SHORT).show();                // toast error message
    }


    public int trueTime(String testString)                                                          // is time valid or not; retun for valid=1, invalid=0
    {
        char[] testing = testString.toCharArray();                                                  // from string to array
        if (testString.isEmpty())                                                                   // is string is empty
            return 0;                                                                               // error
        else if (testing[0] == ':' | testing[testing.length - 1] == ':')                            // if first or last char is : then invalid
            return 0;                                                                               // error

        int countcolon = 0;                                                                         // total colon count
        for (int i = 0; i < testing.length; i++)                                                    // cant set i=1 as the number might also be only a single char, ex:'1'; if st
        {
            if (testing[i] == 'a' | testing[i] == 'p' | testing[i] == 'm')                          // if testing[i] is among these letters
                return 0;                                                                           // error
            else if (testing[i] == ':')                                                             // if char==':'
                countcolon++;                                                                       // add to countcolon
        }
        if (countcolon != 1)                                                                        // if number of colons!=1
            return 0;                                                                               // error
        return 1;                                                                                   // success for correct time
    }

    public void calcColour()                                                                        // set hourFlag and minFlag arrays
    {
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
    }

    public void imageChange()                                                                       // change images in preview
    {

        ImageView box5x5 = (ImageView) findViewById(R.id.imageView3);                               // initialise box5x5 object of type ImageView, corresponding image is imageView3
        ImageView box3x3 = (ImageView) findViewById(R.id.imageView4);                               // initialise box3x3 object of type ImageView, corresponding image is imageView4
        ImageView box2x2 = (ImageView) findViewById(R.id.imageView5);                               // initialise box2x2 object of type ImageView, corresponding image is imageView5
        ImageView box1x1a = (ImageView) findViewById(R.id.imageView7);                              // initialise box1x1a object of type ImageView, corresponding image is imageView7
        ImageView box1x1b = (ImageView) findViewById(R.id.imageView6);                              // initialise box1x1b object of type ImageView, corresponding image is imageView36

        box5x5.setImageResource(android.R.color.transparent);                                       // initialise all boxes to transparent colour
        box3x3.setImageResource(android.R.color.transparent);
        box2x2.setImageResource(android.R.color.transparent);
        box1x1a.setImageResource(android.R.color.transparent);
        box1x1b.setImageResource(android.R.color.transparent);

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

    public void resetTime()                                                                         // reset time variables hour & min to -1 to avoid unwanted scenarios
    {                                                                                               // [IMPORTANT] also sets hourFlag and minFlag arrays to zeroes to start afresh, with no previous values altering current values
        hour = -1;
        min = -1;                                                                                   // hour and min set to -1
        for (int i = 0; i < 5; i++) {
            hourFlag[i] = 0;                                                                        // all elements in hourFlag set to 0
            minFlag[i] = 0;                                                                         // all elements in minFlag set to 0
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                                                 // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);                                          // we add dock view button from res > menu > menu_main.xml
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {                                           // from inflater, check which option is selected
        int count;
        switch (item.getItemId()) {                                                                 // check all action bar item IDs, whichever clicked
            case R.id.dockView:                                                                     // if dock view button is selected
                Intent dockIntent = new Intent(this, dockView.class);                               // Intent is from this activity to dock view activity
                startActivity(dockIntent);                                                          // start dock view activity
        }
        return (super.onOptionsItemSelected(item));                                                 // returns boolean for selection of action bar button
    }
}
