package view.formatfa.color;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class ColorViewTest extends View {
    private Context context;
    boolean isViewFinish = false;


    Paint paint = new Paint();
    private int viewW,viewH;
    public ColorViewTest(Context context) {
        super(context);
        this.context = context;
        init();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                invalidate();
            }
        });
    }

    private void init() {

    this.post(new Runnable() {
        @Override
        public void run() {
            isViewFinish=true;
            viewH=ColorViewTest.this.getHeight();
            viewW=ColorViewTest.this.getWidth();
            invalidate();
        }
    });


    }


    @Override
    protected void onDraw(Canvas canvas) {


        int total =255*255*255;
        if(!isViewFinish)return;
        int space = total/viewW;

        for(int i = 0;i<viewW;i+=1)
        {
            paint.setColor(Color.RED);
            canvas.drawLine(i,0,i,viewH,paint);
        }
        super.onDraw(canvas);
    }
}
