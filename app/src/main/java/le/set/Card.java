package le.set;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

public class Card {
	private int number;
	private int color;
	private int filling;
	private int shape;
	 int value; // value = number + 3 * (color + 3 * (filling + 3 * shape))

	Card(int value) {
		this.value = value;
		// compute the other attributes from value
		number = value%3;
		color = (value/3)%3;
		filling = (value/9)%3;
		shape = (value/27)%3;
	}

	// equality test on SetCards
	public boolean equals(Object o) {
		Card c = (Card) o;
		return (value == c.value);
	}

	// return the characteristics of the card
	int[] characteristics() {
		return new int[] {number, color, filling, shape};
	}
	// draw the card on the Canvas c
	// different copies of the same shape are drawn, according to this.number
	void draw(Canvas c, int width, int height) {

		Paint background = new Paint();
		background.setColor(Color.WHITE);
		c.drawRect(new RectF(0,0,width,height), background);
		// set the default paint style
		Paint p = new Paint();
		p.setStrokeWidth(2);
		setColor(p);
		// computes the topmost point
		int startY = (36 - 16*number)*height/96;
		// draw as many shapes as this.number
		for (int i = 0; i <= number; i++) {
			RectF r = new RectF(width/8, startY + i * height/3, width*7/8, startY + i * height/3 + height/4);
			drawFilledShape(c, p, r);
		}
	}
	// set the color of the Paint p according to this.color
	private void setColor(Paint p) {
		switch (color) {
		case 0: p.setColor(Color.RED); break;
		case 1: p.setColor(Color.BLUE); break;
		case 2: p.setColor(Color.GREEN); break;
		default: new Error("invalid color");
		}
	}

	// draw the desired shape (on the Canvas c, with the Paint p) according to this.shape
	private void drawShape(Canvas c, Paint p, RectF r) {
		switch (shape) {
		case 0: c.drawOval(r, p); break;
		case 1: c.drawRect(r, p); break;
		case 2: c.drawPath(diamond(r), p); break;
		default: new Error("invalid shape");
		}
	}
	// draw the desired shape (on the Canvas c, with the Paint p) with the correct filling according to this.filling
	private void drawFilledShape(Canvas c, Paint p, RectF r) {
		switch (filling) {
		case 0: p.setStyle(Paint.Style.STROKE); drawShape(c, p, r); break;
		case 1:
			// in case of intermediate filling, we draw concentric copies of the same shape
			p.setStyle(Paint.Style.STROKE);
			for (int i = 0; i < r.width()/2; i+=20) {
				drawShape(c, p, new RectF(r.left + i, r.top + i * r.height()/r.width(), r.right - i, r.bottom - i * r.height()/r.width()));
			}
			break;
		case 2: p.setStyle(Paint.Style.FILL); drawShape(c, p, r); break;
		default: new Error("invalid filling");
		}
	}
	// creates a diamond in the rectangle r
	private Path diamond(RectF r) {
		Path p = new Path();
		p.moveTo(r.left, r.centerY());
		p.lineTo(r.centerX(), r.top);
		p.lineTo(r.right, r.centerY());
		p.lineTo(r.centerX(), r.bottom);
		p.lineTo(r.left, r.centerY());
		return p;
	}
	public static int thirdCard(int card1, int card2){
		Card c1=new Card(card1);
		Card c2=new Card(card2);
		int[] t1=c1.characteristics();
		int[] t2 = c2.characteristics();
		int result=0;
		int pow=1;
		for(int i=0;i<4;i++){
			result+=((6-t1[i]-t2[i])%3)*pow;
			pow*=3;
		}
		return result;
	}
}
