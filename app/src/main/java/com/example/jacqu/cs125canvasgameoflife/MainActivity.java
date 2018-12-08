package com.example.jacqu.cs125canvasgameoflife;

import android.os.Handler;
import android.os.Looper;
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
    public TextView initialMessage;

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
        mImageView = findViewById(R.id.myimageview);
        mswitch = findViewById(R.id.switch1);
        imgBtn = findViewById(R.id.play_or_pause);
        // lanch background is a temp file
        imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        initialMessage = findViewById(R.id.textView);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paused && !(mswitch.isChecked())) {
                    imgBtn.setImageResource(R.drawable.ic_pause_black_24dp);
                    paused = true;
                } else if (paused && !(mswitch.isChecked())){
                    imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                    paused = false;
                }
            }
        });

        mswitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mswitch.isChecked()) {
                    imgBtn.setImageResource(R.drawable.ic_play_arrow_black_24dp);
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

    public void updateGrid(View view) {

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
                        cellstate[i][j] = !cellstate[i][j];
                        updateGrid(mImageView);
                    }
                }
        }
        return true;
    }

    Handler handler = new Handler(Looper.getMainLooper());
    Runnable movePlayer0Runnable = new Runnable() {
        public void run() {
            updategame();
            updateGrid(mImageView, mCanvas);
            handler.postDelayed(this, 5000); //in 5 sec player0 will move again
        }
    };
  
    // some extra, fun little functions
    public void resetgame() {
        for (int i = 0; i < 15; i ++) {
            for (int j = 0; j < 15; j++) {
                cellstate[i][j] = false;
            }
        }
    }
    public void inversegame() {
        for (int i = 0; i < 15; i ++) {
            for (int j = 0; j < 15; j++) {
                cellstate[i][j] = !cellstate[i][j];
            }
        }
    }
    /**
     * This is where the rules of the game of life are implemented
     */
    public void updategame() {
        // checked identifies all squares that need to change
        boolean[][] checked = new boolean[15][15];
        for (int i = 0; i < 15; i ++) {
            for (int j = 0; j < 15; j++) {
                int neighbors = countNeighbors(i, j);
                // cell dies if neighbors is less than 2 or greater then 4
                checked[i][j] = false;
                // live condition is if neighbors is between 3 and 4
                if (neighbors > 1 && neighbors < 4) {
                    checked[i][j] = true;
                }
            }
        }
        for (int i = 0; i < 15; i ++) {
            for (int j = 0; j < 15; j++) {
                cellstate[i][j] = checked[i][j];
            }
        }
    }
    /**
     * Function to count the number of live neighbors for a given square
     * @param i row number
     * @param j column number
     *          This could be implemented so much more elegantly with graphs I bet.
     */
    private int countNeighbors(int i, int j) {
        int neighbors = 0;
        // if square is not on an edge
        if (i > 0 && j > 0 && i < 14 && j < 14) {
            for (int row = i - 1; row <= i + 1; row++) {
                for (int col = j - 1; col <= j + 1; col++) {
                    if (!(col == j && row == i)) {
                        if (cellstate[row][col]) {
                            neighbors++;
                        }
                    }
                }
            }
        }
        // if square is on left edge (not corner)
        if (i == 0 && j != 0 && j != 14) {
            for (int row = i; row <= i + 1; row++) {
                for (int col = j - 1; col <= j + 1; col++) {
                    if (!(col == j && row == i)) {
                        if (cellstate[row][col]) {
                            neighbors++;
                        }
                    }
                }
            }
        }
        // if square is on right edge (not corner)
        if (i == 14 && j != 0 && j != 14) {
            for (int row = i - 1; row <= i; row++) {
                for (int col = j - 1; col <= j + 1; col++) {
                    if (!(col == j && row == i)) {
                        if (cellstate[row][col]) {
                            neighbors++;
                        }
                    }
                }
            }
        }
        // if square is on upper edge (not corner)
        if (j == 0 && i != 0 && i != 14) {
            for (int row = i - 1; row <= i + 1; row++) {
                for (int col = j; col <= j + 1; col++) {
                    if (!(col == j && row == i)) {
                        if (cellstate[row][col]) {
                            neighbors++;
                        }
                    }
                }
            }
        }
        // if square is on bottom edge (not corner)
        if (j == 14 && i != 0 && i != 14) {
            for (int row = i - 1; row <= i + 1; row++) {
                for (int col = j - 1; col <= j; col++) {
                    if (!(col == j && row == i)) {
                        if (cellstate[row][col]) {
                            neighbors++;
                        }
                    }
                }
            }
        }
        // top left corner
        if (i == 0 && j == 0) {
            if (cellstate[1][0]) {
                neighbors++;
            }
            if (cellstate[1][1]) {
                neighbors++;
            }
            if (cellstate[0][1]) {
                neighbors++;
            }
        }
        // bottom left corner
        if (i == 0 && j == 14) {
            if (cellstate[0][13]) {
                neighbors++;
            }
            if (cellstate[1][13]) {
                neighbors++;
            }
            if (cellstate[1][14]) {
                neighbors++;
            }
        }
        // top right corner
        if (i == 14 && j == 0) {
            if (cellstate[13][0]) {
                neighbors++;
            }
            if (cellstate[13][1]) {
                neighbors++;
            }
            if (cellstate[14][1]) {
                neighbors++;
            }
        }
        // bottom right corner
        if (i == 14 && j == 14) {
            if (cellstate[13][13]) {
                neighbors++;
            }
            if (cellstate[14][13]) {
                neighbors++;
            }
            if (cellstate[13][14]) {
                neighbors++;
            }
        }
        return neighbors;
    }
}

