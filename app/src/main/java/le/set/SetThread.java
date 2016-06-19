package le.set;

/**
 * Created by Laetitia on 04/03/2016.
 */
public class SetThread implements  Runnable {
    private CardSet cardset;
    SetThread(CardSet cardset)
    {
        this.cardset = cardset;
    }
    @Override
    public void run() {
        while (true)
            try {
                cardset.watcher();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }
}
