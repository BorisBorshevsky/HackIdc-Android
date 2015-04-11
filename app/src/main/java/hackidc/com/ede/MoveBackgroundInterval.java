package hackidc.com.ede;

/**
 * Created by user on 4/7/2015.
 */
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task
 * to execute once 5 seconds have passed.
 */

public class MoveBackgroundInterval {
    Timer timer;
    static FrameLayout fl;

    public MoveBackgroundInterval(int ms, FrameLayout fl) {
        this.fl = fl;
        timer = new Timer();
        timer.schedule(new RemindTask(), ms);
    }

    class RemindTask extends TimerTask {
        public void run() {

            FrameLayout.LayoutParams llp = (FrameLayout.LayoutParams) fl.getLayoutParams();
            llp.rightMargin =- 5;
            fl.setLayoutParams(llp);

            timer.cancel(); //Terminate the timer thread
        }
    }

    public static void main(String args[]) {
        new MoveBackgroundInterval(5, fl);
        System.out.format("Task scheduled.%n");
    }
}