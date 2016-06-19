package le.set;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


public class home extends AppCompatActivity {
    public final static String GAME_MODE = "le.set.MESSAGE";
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
        }

    public void Game(View view)
    {
    Intent intent = new Intent(this,Game.class);
        String string = view.getTag().toString();
        if (string.equals("SINGLE PLAYER"))
        {
            intent.putExtra(GAME_MODE, "single");
            startActivity(intent);
        }
        else if (string.equals("MULTIPLAYER"))
        {
            intent.putExtra(GAME_MODE, "multi");
            startActivity(intent);
        }
    }
    }
