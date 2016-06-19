package le.set;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Laetitia on 04/03/2016.
 */

/*Card Set represents the set of cards that have been selected
* @ATTRIBUTES
* set: list of cards included in the set, designated by their id.
* max_capacity: maximum capacity of the set
* variables: set of variables that are necessary.
* */

public class CardSet {
    private LinkedList<Integer> set;
    //private Lock lock;
    private final int max_capacity = 3;
    //private Condition isFull;
    private utilities variables;

    public CardSet(utilities variables) {
        set = new LinkedList<>();
        this.variables = variables;
    }

    public synchronized boolean push(int card) {

        System.out.println("PUSH in a size" + " " + set.size());
        int size = set.size();
        if (size == max_capacity) return false;
        set.push(card);
        if (size == max_capacity - 1) notifyAll();
        return true;
    }

    public synchronized boolean pop(int card) {
        System.out.println("POP in a size" + " " + set.size());
        int size = set.size();
        if (size == 0) return false;
        return set.remove((Integer) card);
    }

    private int[] notfullplate() {
        //fetch new cards and position them.
        int cpt = 0;
        int[] newcards = new int[3];
        for (int i = 0; i < 3; i++) {
            newcards[i] = -1;
        }
        for (Integer i : set) {
            final int card = i;
            Panel p = variables.pos2pan[variables.id2pos[card]];
            if (variables.cards.empty()) {
                p.setCard(-1);
                p.setMode(0);
            } else {
                int c = variables.cards.pop();
                newcards[cpt] = c;
                cpt++;
                variables.id2pos[c] = variables.id2pos[card];
                p.setCard(c);
                p.setMode(1);
            }
            variables.id2pos[card] = -1;
        }
        return newcards;

    }

    private void fullplate() {
        // move the cards
        for (Integer i : set) {
            final int cardid = i;
            Panel p = variables.pos2pan[variables.id2pos[cardid]];
            if (variables.id2pos[cardid] >= 12) {
                p.setCard(-1);
                p.setMode(0);
                variables.id2pos[cardid] = -1;
            } else {
                int k = 12;
                while (k < 15) {
                    Panel q = variables.pos2pan[k];
                    int card = q.card.value;
                    if (card >= 0) {
                        variables.id2pos[card] = variables.id2pos[cardid];
                        variables.id2pos[cardid] = -1;
                        p.setCard(card);
                        p.setMode(1);
                        q.setMode(0);
                        q.setCard(-1);
                        break;
                    } else {
                        System.out.println("NO CARD AT POSITION " + k);
                        k++;
                    }
                }
            }
        }
    }

    private void update_sets(int[] newcards) {
        //clean the sets:
        int[] oldcards = new int[3];
        for (int i = 0; i < 3; i++) {
            oldcards[i] = set.get(i);
        }
        variables.ClearSets(oldcards);
        //add the new cards
        if (newcards != null && newcards[0] >= 0 && newcards[1] >= 0 && newcards[2] >= 0) {
            variables.UpdateSets(newcards);
        }
    }

    public synchronized void watcher() throws InterruptedException {
        while (set.size() < max_capacity) {
            wait();
        }
        System.out.println("IS FULL");
        Triplet triplet = new Triplet(set.get(0), set.get(1), set.get(2));
        boolean isSet = triplet.isSet();
        //CASE 1: Set and there was no fullplate
        if (isSet && !variables.fullplate.get()) {
            System.out.println("GAME WAS NOT FULLPLATE");
            variables.score.increment();
            int[] newcards = notfullplate();
            update_sets(newcards);
            //update and check for a new Set
            variables.checkSet();

        }
        //CASE 2: isSet and was fullplate
        else if (isSet && variables.fullplate.get()) {
            System.out.println("GAME WAS FULLPLATE");
            variables.score.increment();
            fullplate();
            update_sets(null);
            variables.fullplate.set(false);
            //update and check for a new Set
            variables.checkSet();

        } else {
            variables.score.decrement();
            //Deselect the panels accordingly.
            int cpt = 0;
            for (Integer i : set) {
                final int cardid = i;
                Panel p = variables.pos2pan[variables.id2pos[cardid]];
                p.setMode(1);
            }
        }

        //Clear the set.
        set.clear();
    }
}