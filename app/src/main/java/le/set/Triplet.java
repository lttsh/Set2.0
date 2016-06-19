package le.set;
/**
 * Created by Estolol on 04/03/2016.
 */
public class Triplet {
    int c1;//id of the first card
    int c2;
    int c3;

    public Triplet(int card1id,int card2id,int card3id){
        c1=card1id;
        c2=card2id;
        c3=card3id;
        this.fix();
    }

    public Triplet(int card1id,int card2id){
        int card3id=Card.thirdCard(card1id, card2id);
        c1=card1id;
        c2=card2id;
        c3=card3id;
        this.fix();
    }

    public void fix(){
        int a =Math.min(c1, Math.min(c2, c3));
        int b =Math.max(c1,Math.max(c2,c3));
        int c =c1+c2+c3-a-b;
        c1=a;
        c2=c;
        c3=b;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triplet))
            return false;
        Triplet t = (Triplet) obj;
        t.fix();
        return ((this.c1 == t.c1) && (this.c2 == t.c2) && (this.c3 == t.c3));
    }

    @Override
    public int hashCode(){
        return c1*10000+c2*100+c3;
    }

    public String toString(){
        return c1+" "+c2+" "+c3;
    }

    public boolean isSet()
    {

        int color=0,filling=0, shape=0;
        color= (c1/3)%3+(c2/3)%3+(c3/3)%3;
        filling = (c1/9)%3+(c2/9)%3+(c3/9)%3;
        shape = (c1/27)%3+(c2/27)%3+(c3/27)%3;
        return (color%3==0 && filling%3==0 && shape%3==0 );
    }
    public boolean hasInCommon(int[] old){
        int[] test=new int[3];
        test[0]=c1;
        test[1]=c2;
        test[2]=c3;
        for (int i=0;i<3;i++)
        {
            for (int j=0;j<3;j++)
            {
                if (test[i]==old[j]) return true;
            }
        }
        return false;
    }
}
