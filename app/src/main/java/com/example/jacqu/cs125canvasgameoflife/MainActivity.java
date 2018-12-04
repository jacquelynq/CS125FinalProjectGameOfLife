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

public class MainActivity extends AppCompatActivity {
    // sets variables for drawing
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private ImageView mImageView;

    private Paint mPaintLive = new Paint();
    private Paint mPaintDead = new Paint();
    private int mColorBackground;
    private int mColorLive;
    private int mColorDead;

    private Rect mRect = new Rect();
    private int vWidth, vHeight;
    private int numColumns, numRows;
    private int cellDim;
    private int border;

    // sets variable for buttons
    private Switch mswitch;

    // sets variables for tracking game of life
    public boolean griddrawn = false;
    public boolean[][] cellstate;

    private ImageButton imgBtn;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // sets content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // sets color variables
        mColorBackground = ResourcesCompat.getColor(getResources(),
                R.color.colorBackground, null);
        mColorLive = ResourcesCompat.getColor(getResources(),
                R.color.colorLive, null);
        mColorDead = ResourcesCompat.getColor(getResources(),
                R.color.colorDead, null);

        mPaintDead.setColor(mColorDead);
        mPaintLive.setColor(mColorLive);

        // sets imageview and switch variables
        mImageView = (ImageView) findViewById(R.id.myimageview);
        mswitch = (Switch) findViewById(R.id.switch1);
        imgBtn = (ImageButton)findViewById(R.id.play_or_pause);
        imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);

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

    public void createGrid(View view) {
        // gets height and width of the screen
        vWidth = view.getWidth();
        vHeight = view.getHeight();
        // creates bitmap, imageview object, and canvas to support drawable image
        mBitmap = Bitmap.createBitmap(vWidth, vHeight, Bitmap.Config.ARGB_8888);
        mImageView.setImageBitmap(mBitmap);
        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(mColorBackground);

        // sets gird parameters for drawing
        numColumns = 15;
        numRows = 15;
        int cellWidth, cellHeight;
        cellWidth = vWidth / numColumns;
        cellHeight = vHeight / numRows;
        cellDim = Math.min(cellHeight, cellWidth);
        border = 50 / numColumns;

        //creates boolean array to tack game of life
        cellstate = new boolean[numRows][numColumns];

        // draws grid
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                mRect.set(i*cellDim + border, j*cellDim + border, (i + 1)*cellDim - border,
                        (j + 1)*cellDim - border);
                mCanvas.drawRect(mRect, mPaintDead);
                cellstate[i][j] = false;
            }
        }
        griddrawn = true;
        view.invalidate();
    }

    public void updateGrid(View view, Canvas mCanvas) {

        // updates grid with new colors based on true / false
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                mRect.set(i*cellDim + border, j*cellDim + border, (i + 1)*cellDim - border,
                        (j + 1)*cellDim - border);
                if (cellstate[i][j]) {
                    mCanvas.drawRect(mRect, mPaintLive);
                } else {
                    mCanvas.drawRect(mRect, mPaintDead);
                }
            }
        }
        view.invalidate();
    }

    public boolean onTouchEvent(MotionEvent event) {
        // gets absolute coordinates of where event happened
        float x = event.getX();
        float y = event.getY();

        // takes account of difference between image view coordinates and absolute coordinates
        int[] posXY = new int[2];
        mImageView.getLocationOnScreen(posXY);
        x -= posXY[0];
        y -= posXY[1];

        // runs code depending on which action used
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

