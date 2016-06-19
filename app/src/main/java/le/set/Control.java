package le.set;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Laetitia on 03/03/2016.
 */
public class Control extends SurfaceView {
    SurfaceHolder holder;
    SurfaceHolder.Callback callback;
    public Control(Context context,AttributeSet attrs) {
        super(context,attrs);
        holder = getHolder();
        callback =new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas c = holder.lockCanvas(null);
                Draw(c);
                holder.unlockCanvasAndPost(c);
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
            }
        };
        holder.addCallback(callback);
    }
    protected void Draw(Canvas canvas) {
       //TODO Draw the Control panel
    }
}
