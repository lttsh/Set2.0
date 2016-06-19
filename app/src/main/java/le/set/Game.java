package le.set;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Chronometer;
import android.widget.TableLayout;


public class Game extends AppCompatActivity {
    private static utilities variables;
    private static String mode;

    public final static String SCORE = "SCORE";
    public final static String TIME = "TIME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //Get game mode
                Intent intent = getIntent();
                mode = intent.getStringExtra(home.GAME_MODE);

                //Initialize all variables
                variables = new utilities(Game.this);
                //Initialize CardSet object which keeps track of the selected cards
                CardSet selected = new CardSet(variables);
                //Add selected
                TableLayout table = (TableLayout) findViewById(R.id.table);
                for (Panel p : variables.pos2pan)
                {
                    p.add_selected(selected);
                }

                //Launch all threads:
                //WATCHER THREAD
                new Thread(new SetThread(selected)).start();
                //SCORE THREAD
                new Thread(variables.score).start();
        //initialize chronometer
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    private synchronized void watch_end()
    {
        while (!variables.end)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("FINISH");
        Game.this.finish();

    }
    @Override
    protected void onPause() {
        super.onPause();
        //pause chronometer
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //restart chronometer
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.start();
    }

    public void GameOver()
    {
        Intent intent = new Intent(this,GameOver.class);


        intent.putExtra(SCORE, Integer.toString(variables.score.getScore()));
        intent.putExtra(TIME, Long.toString(SystemClock.elapsedRealtime() - ((Chronometer) findViewById(R.id.chronometer)).getBase()));
        intent.putExtra(home.GAME_MODE, mode);
        Game.this.finish();
        startActivity(intent);

    }
}
