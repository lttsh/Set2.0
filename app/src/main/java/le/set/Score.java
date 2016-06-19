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
 */
public class Score implements  Runnable {
    private int score;
    private TextView score_view;
    private final String score_text= "Score: ";
    private Condition hasChanged;
    private Lock lock;
    private Activity activity;
    private boolean isSame;
    public Score(Context context)
    {
        lock = new ReentrantLock();
        hasChanged = lock.newCondition();
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
    public void run() {
        while(true)
        {
            lock.lock();
            try
            {
                while(isSame)
                {
                    hasChanged.awaitUninterruptibly();
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        score_view.setText(score_text+score);
                    }
                });

            }
            finally {
                isSame = true;
                lock.unlock();
            }
        }
    }
    public void increment()
    {
        lock.lock();
        try{
            score++;
            isSame=false;
            hasChanged.signal();
        }
        finally {
            lock.unlock();
        }
    }
    public void decrement()
    {
        lock.lock();
        try{
            score--;
            isSame=false;
            hasChanged.signal();
        }
        finally {
            lock.unlock();
        }
    }
}
