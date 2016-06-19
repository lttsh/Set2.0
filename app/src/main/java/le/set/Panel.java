package le.set;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Laetitia on 02/03/2016.
 */
public class Panel extends SurfaceView{
    //3 POSSIBLE MODES//
    final public int HIDDEN =0;
    final public int DEFAULT=1;
    final public int SELECTED=2;
    //@ATTRIBUTES
    Card card;
    int display_mode;
    SurfaceHolder holder;
    CardSet selected;

    public Panel(Context context,AttributeSet attrs) {
        super(context, attrs);
        //Set mode
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.Options, 0, 0);
        String disp = a.getString(R.styleable.Options_display_mode);
        try{
            display_mode= Integer.parseInt(disp);
        }finally {
            a.recycle();
        }
        //Set holder and surface Callback
        holder = getHolder();
        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas(null);
                Draw(canvas);
                holder.unlockCanvasAndPost(canvas);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        };
        holder.addCallback(callback);
    }
    public void add_selected(CardSet selected)
    {
        this.selected = selected;
    }
    //Draw Method
    protected void Draw(Canvas canvas) {
        if (card!=null)
        {
            switch(display_mode)
            {
                case DEFAULT:

                    card.draw(canvas, this.getWidth(), this.getHeight());
                    Paint p = new Paint();
                    p.setColor(Color.DKGRAY);
                    p.setStrokeWidth(10);
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(new RectF(0,0,this.getWidth(),this.getHeight()),p);
                    break;
                case HIDDEN:
                    canvas.drawColor(Color.BLACK);
                    break;
                case SELECTED:
                    card.draw(canvas, this.getWidth(),this.getHeight());
                    p = new Paint();
                    p.setColor(Color.GREEN);
                    p.setStrokeWidth(10);
                    p.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(new RectF(0, 0, this.getWidth(), this.getHeight()), p);
                    break;
            }
           }
    }

    //Change the card associated with the panel
    void setCard(int i) {
        card = new Card(i);
       update_display();
    }
    //Change the display mode of the panel
    synchronized boolean setMode(int i)
    {
        if (i>=0 && i<3)
        {
            display_mode=i;
            return update_display();
        }
        else {System.err.println("INEXISTANT MODE");return false; }
    }

    //Called everytime the card or the display mode is changed.
    synchronized boolean update_display()
    {
        Canvas c = holder.lockCanvas(null);
            try {
                Draw(c);
                holder.unlockCanvasAndPost(c);
                return true;
            }
            catch (NullPointerException e)
            {
                return false;
            }
    }
    //Called when the view is clicked on
    synchronized public void select()
    {
        switch (display_mode) {
            case DEFAULT:
                if (selected.push(card.value))
                    System.out.println(setMode(SELECTED));
                else System.out.println("not push");
                break;
            case SELECTED:
                if (selected.pop(card.value))
                System.out.println(setMode(DEFAULT));
                else System.out.println("not pop");
                break;
        }
    }
}
