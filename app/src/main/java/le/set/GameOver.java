package le.set;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {

    private static String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        Intent intent= getIntent();
        //System.out.print((intent.getStringExtra(Game.SCORE)==null) ? "isnull" : "isntnull");
        //System.out.print(intent.getStringExtra(Game.SCORE));
        int score = Integer.parseInt(intent.getStringExtra(Game.SCORE));
        long time = Long.parseLong(intent.getStringExtra(Game.TIME));
        mode =intent.getStringExtra(home.GAME_MODE);
        long min = time/60000;
        long sec = (time/1000)%60;
        TextView tv = (TextView) findViewById(R.id.score);
        tv.setText("Score: "+score);
        TextView tv2 = (TextView) findViewById(R.id.time);
        tv2.setText("Durée de la partie: "+min+":"+sec);
    }

    public void Home(View view)
    {
        GameOver.this.finish();
    }

    public void Game(View view)
    {
        Intent intent = new Intent(this,Game.class);
        intent.putExtra(home.GAME_MODE, mode);
        GameOver.this.finish();
        startActivity(intent);
    }
}
