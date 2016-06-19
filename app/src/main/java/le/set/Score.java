package le.set;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.TextView;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Laetitia on 05/03/2016.
 * Score objects keeps track of the score in a seperate thread.
 */
public class Score implements  Runnable {
    private int score;
    private TextView score_view;
    private final String score_text= "Score: ";
    private Activity activity;
    private boolean isSame;
    public Score(Context context)
    {
        isSame = true;
        this.activity = (Activity) context;
        score = 0;
        score_view = (TextView) activity.findViewById(R.id.score);
        score_view.setText(score_text + score);

    }
    public int getScore()
    {
        return score;
    }

    @Override
    public synchronized void run() {
        while (true) {
            while (isSame) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    score_view.setText(score_text + score);
                }
            });
            isSame = true;
        }
    }
    public synchronized void increment()
    {
            score++;
            isSame=false;
           notify();
    }
    public synchronized void decrement()
    {
        score--;
        isSame=false;
        notify();
    }
}
