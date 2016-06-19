package le.set;

import android.view.View;
import android.widget.TableLayout;

/**
 * Created by Laetitia on 03/03/2016.
 */
public class CardThread implements Runnable {
    Panel panel;
    int id;
    public CardThread(Panel panel,int i)
    {
        this.panel = panel;
        this.id =i;
    }

    @Override
    public void run() {
        panel.setCard(id);
        //Setting OnClickListener
        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panel.select();
            }
        });
    }
}
