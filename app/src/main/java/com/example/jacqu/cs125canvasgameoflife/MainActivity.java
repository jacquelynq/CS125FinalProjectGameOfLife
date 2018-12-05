package com.example.jacqu.cs125canvasgameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.graphics.Rect;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.Switch;
import android.view.MotionEvent;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Canvas mCanvas;
    private Paint mPaint = new Paint();
    private Paint mPaintText = new Paint(Paint.UNDERLINE_TEXT_FLAG);
    private Bitmap mBitmap;
    private Switch mswitch;
    private ImageView mImageView;
    private Rect mRect = new Rect();
    private int mColorBackground;
    private int mColorRectangle;
    private int mColorAccent;
    private int vWidth, vHeight;
    private int cellDim;
    public boolean griddrawn = false;
    public boolean[][] cellstate;

    private ImageButton imgBtn;
    private boolean paused = false;
    public TextView initialMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorRectangle = ResourcesCompat.getColor(getResources(),
                R.color.colorRectangle, null);
        mColorAccent = ResourcesCompat.getColor(getResources(),
                R.color.colorAccent, null);
        mPaint.setColor(mColorRectangle);
        mPaintText.setColor(ResourcesCompat.getColor(getResources(),
                R.color.colorPrimaryDark, null));

        mImageView = (ImageView) findViewById(R.id.myimageview);
        mswitch = (Switch) findViewById(R.id.switch1);
        imgBtn = (ImageButton)findViewById(R.id.play_or_pause);
        imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        initialMessage = findViewById(R.id.textView);

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused) {
                    imgBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                    paused = true;
                } else {
                    imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                    paused = false;
                }
            }
        });


    }
    public void helper(View view, Canvas mCanvas) {
        int numColumns = 15;
        int numRows = 15;
        int cellWidth, cellHeight;
        cellWidth = vWidth / numColumns;
        cellHeight = vHeight / numRows;
        cellDim = Math.min(cellHeight, cellWidth);
        int border = 50 / numColumns;
        cellstate = new boolean[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (cellstate[i][j]) {
                    if (cellstate
                } else {
                }
            }
        }
    }

    public void createGrid(View view) {
        // gets height and width of the screen
        vWidth = view.getWidth();
        vHeight = view.getHeight();
        // creates bitmap, imageview object, and canvas to support drawable image
        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);
        if (initialMessage.isShown()) {

        }



        int numColumns = 15;
        int numRows = 15;
        int cellWidth, cellHeight;
        cellWidth = vWidth / numColumns;
        cellHeight = vHeight / numRows;
        cellDim = Math.min(cellHeight, cellWidth);
        int border = 50 / numColumns;
        cellstate = new boolean[numRows][numColumns];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                mRect.set(i*cellDim + border, j*cellDim + border, (i + 1)*cellDim - border,
                        (j + 1)*cellDim - border);
                mCanvas.drawRect(mRect, mPaint);
                cellstate[i][j] = false;
            }
        }
        griddrawn = true;
        view.invalidate();
    }

    public void updateGrid(View view, Canvas mCanvas) {
        int numColumns = 15;
        int numRows = 15;
        int border = 50 / numColumns;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                mRect.set(i*cellDim + border, j*cellDim + border, (i + 1)*cellDim - border,
                        (j + 1)*cellDim - border);
                if (cellstate[i][j]) {
                    mCanvas.drawRect(mRect, mPaintText);
                } else {
                    mCanvas.drawRect(mRect, mPaint);
                }
            }
        }
        view.invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        int[] posXY = new int[2];
        mImageView.getLocationOnScreen(posXY);
        y -= posXY[1];
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!griddrawn) {
                    createGrid(mImageView);
                    break;
                } else {
                    int i = (int) x / cellDim;
                    int j = (int) (y) / cellDim;
                    if (i >= 0 && i < 15 && j >= 0 && j < 15 && mswitch.isChecked()) {
                        if (cellstate[i][j]) {
                            cellstate[i][j] = false;
                        } else {
                            cellstate[i][j] = true;
                        }
                        updateGrid(mImageView, mCanvas);
                    }
                }
        }
        return true;
    }

}

