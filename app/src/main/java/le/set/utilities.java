package le.set;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Created by Laetitia on 15/03/2016.
 */
public class utilities {
     public static Score score;
     public static Stack<Integer> cards;
     public static HashSet<Triplet> presentSets;
     public static Panel[] pos2pan;
     public static int[] id2pos;
     public static AtomicBoolean fullplate;
     public static boolean end;
     private static Activity activity;

    public utilities(Context game)
    {
        score = new Score(game);
        fullplate = new AtomicBoolean();
        end = false;
        cards = new Stack<Integer>();
        presentSets = new HashSet<Triplet>();
        pos2pan = new Panel[15];
        id2pos = new int[81];
        for (int i=0;i<81;i++)
        {
            id2pos[i]=-1;
        }
        activity = (Activity) game;
        initialize();
    }
    private static void test()
    {
        for (int i=0;i<81;i++)
        {
            if (id2pos[i]>-1 && pos2pan[id2pos[i]].card.value!=i)
            {
                System.out.println("ID2POS is not correctly updated");
                System.out.println("Carte "+ i+ " position "+ id2pos[i]);
                System.out.println("Position "+ id2pos[i] + " carte "+ pos2pan[id2pos[i]].card.value);
            }
        }

    }
    private static void print()
    {
        for (int i=0;i<80;i++)
        {
            if (id2pos[i]>=0)
            {
                System.out.println("CARD "+i+" is at position "+id2pos[i]);
            }
        }
        for (int i=0;i<15;i++)
        {
            System.out.println("POSITION "+i+" holds card "+pos2pan[i].card.value);
        }
    }
    private void initialize()
    {
        fullplate.set(false);
        //INITIALIZE CARDS
        cards = new Stack<Integer>();
        for (int i=0;i<81;i++)
        {
            cards.push(i);
        }
        Collections.shuffle(cards);
        //INITIALIZE POS2PAN && ID2POS
        TableLayout table = (TableLayout) activity.findViewById(R.id.table);
        for (int i=0;i<4;i++)
        {
            TableRow row =(TableRow) table.getChildAt(i);
            for (int j=0;j<4;j++)
            {
                if (i==3 && j==3) continue;
                Panel panel = (Panel) row.getChildAt(j);
                pos2pan[4*i+j] = panel;
                int c=-1;
                if (i<3)
                {
                    c = cards.pop();
                    id2pos[c]=4*i+j;
                }
                panel.setCard(c);
                Thread t = new Thread(new CardThread(panel,c));
                t.start();
            }
        }
        //INITIALIZE PRESENT SETS
        InitializeSets();
        checkSet();

    }
    public static void InitializeSets()
    {
        for(int i=0;i<12;i++){
            for(int j=i+1;j<12;j++){
                int i3 = Card.thirdCard(pos2pan[i].card.value, pos2pan[j].card.value);
                if(id2pos[i3]!=(-1)){
                    Triplet t=new Triplet(pos2pan[i].card.value, pos2pan[j].card.value,i3);
                    presentSets.add(t);
                }
            }
        }
    }
    public static void ClearSets(int[] oldCardsID){
        System.out.println("BEFORE CLEARSETS");
        display_sets();
        Triplet t;
        //We start by removing outdated sets
        Iterator<Triplet> iter=presentSets.iterator();
        LinkedList<Triplet> toRemove = new LinkedList<Triplet>();
        while(iter.hasNext()){
            t=iter.next();
            if(t.hasInCommon(oldCardsID)){
                toRemove.add(t);
                System.out.println("WANTS TO REMOVE: "+ id2pos[t.c1]+","+id2pos[t.c2]+", "+id2pos[t.c3]);

            }
        }
        for(Triplet trip : toRemove){
            presentSets.remove(trip);
        }
        System.out.println("AFTER CLEARSETS");
        display_sets();
    }

    public static void UpdateSets(int[] newCardsID)
    {
        Triplet t;
        for(int i=0;i<3;i++){
            for(int j=0;j<15;j++){
                int c= pos2pan[j].card.value;
                if((c!=(-1))&&(c!=newCardsID[i])) {
                    int i3 = Card.thirdCard(newCardsID[i], c);
                    if (id2pos[i3] != (-1)) {
                        t = new Triplet(newCardsID[i], c, i3);
                        presentSets.add(t);
                    }
                }
            }
        }
    }
    private static void display_sets()
    {
        for (Triplet t: presentSets)
        {
            System.out.println(id2pos[t.c1]+","+id2pos[t.c2]+", "+id2pos[t.c3]);
        }
    }
    synchronized static public void checkSet()
    {
        if (fullplate.get())
        {
            if (presentSets.size()<=0)
            {
                synchronized (activity)
                {
                    ((Game) activity).GameOver();

                }
            }
            else
            {
                display_sets();
            }
            return;
        }
        //CASE 1: NO SETS BUT NON EMPTY STACK
        if (presentSets.size()<=0 && cards.size()>0)
        {
            System.out.println("no set");
            int[] newcards = new int[3];
            for (int i =0;i<3;i++)
            {
                newcards[i]=-1;
            }
            for (int i=12;i<15;i++)
            {
                final int position = i;
                if (cards.empty())
                {
                    break;
                }
                else {

                    final int c = cards.pop();
                    newcards[i-12]= c;
                    Panel p = pos2pan[position];
                    p.setCard(c);
                    p.setMode(1);
                    id2pos[c] = position;
                }
            }
            if (newcards[0]>=0) {
                UpdateSets(newcards);
            }
            fullplate.set(true);
            checkSet();
            System.out.println("Added additionnal cards");
        }
        //CASE 2: NO SETS BUT EMPTY STACK: GAME OVER
        else if (presentSets.size()<=0 && cards.size()==0)
        {
            System.out.println("END GAME");
            synchronized (activity)
            {
                ((Game) activity).GameOver();

            }

        }
        //CASE3: SETS
        else
        {
            if (cards.size()==0)
            {
                System.out.println("NO MORE CARDS");
            }
            System.out.println("THERE WILL BE SETS");
            display_sets();
            if (!(pos2pan[12].display_mode==1 && pos2pan[13].display_mode==1 &&pos2pan[14].display_mode==1))
            {
                fullplate.set(false);
            }

        }
    }
}
