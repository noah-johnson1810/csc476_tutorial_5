package edu.sdsmt.team5.risk;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GameArea extends View {
    private static final int NUM_ROWS = 10;
    private static final int NUM_COLUMNS = 10;
    private int dotRadius = 0;
    private static final int DOT_COLOR = Color.YELLOW;
    private Paint dotPaint;
    Paint linePaint;
    Paint rectanglePaint;
    private GameActivity gameActivity;
    private final Touch touch1 = new Touch();
    private final Touch touch2 = new Touch();
    public float dotCaptureRadius = 0;
    private float rectangleCaptureProbability = 0.5f;

    public GameArea(Context context) {
        super(context);
        init();
    }

    public GameArea(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameArea(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dotPaint = new Paint();
        dotPaint.setColor(DOT_COLOR);
        dotPaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        rectanglePaint = new Paint();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                if (gameActivity.getGameSim().getGameGrid().grid[row][col].getStatus() == TileStatus.PLAYER_ONE) {
                    dotPaint.setColor(Color.argb(150, 0, 0, 255));
                } else if (gameActivity.getGameSim().getGameGrid().grid[row][col].getStatus() == TileStatus.PLAYER_TWO) {
                    dotPaint.setColor(Color.argb(130, 255, 0, 0));
                } else { // empty
                    dotPaint.setColor(Color.YELLOW);

                    // apply glow effect for unclaimed collectibles

                    // inner glow
                    dotPaint.setAlpha(55); // lower opacity than actual dot
                    canvas.drawCircle(gameActivity.getGameSim().getGameGrid().grid[row][col].getX(), gameActivity.getGameSim().getGameGrid().grid[row][col].getY(), dotRadius * 1.6f, dotPaint);

                    // outer glow
                    dotPaint.setAlpha(25); // lowest opacity for faintest glow
                    canvas.drawCircle(gameActivity.getGameSim().getGameGrid().grid[row][col].getX(), gameActivity.getGameSim().getGameGrid().grid[row][col].getY(), dotRadius * 2.1f, dotPaint);
                    dotPaint.setAlpha(255);
                }
                canvas.drawCircle(gameActivity.getGameSim().getGameGrid().grid[row][col].getX(), gameActivity.getGameSim().getGameGrid().grid[row][col].getY(), dotRadius, dotPaint);
                dotPaint.setColor(Color.YELLOW);
            }
        }

        if (gameActivity.getGameSim().getCaptureType() == CaptureType.DOT) {
            if (touch1.id != -1) {
                dotPaint.setColor(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? Color.argb(60, 0, 0, 255) : Color.argb(40, 255, 0, 0));
                canvas.drawCircle(touch1.x, touch1.y, dotCaptureRadius, dotPaint);
                dotPaint.setColor(DOT_COLOR);
            }
        } else if (gameActivity.getGameSim().getCaptureType() == CaptureType.LINE) {
            if (touch1.id != -1 && touch2.id != -1) {
                linePaint.setColor(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? Color.argb(100, 0, 0, 255) : Color.argb(75, 255, 0, 0));
                linePaint.setStrokeWidth(getWidth() / 100f);

                canvas.drawLine(touch1.x, touch1.y, touch2.x, touch2.y, linePaint);
            }
        } else {
            if (touch1.id != -1 && touch2.id != -1) {
                float left = Math.min(touch1.x, touch2.x);
                float top = Math.min(touch1.y, touch2.y);
                float right = Math.max(touch1.x, touch2.x);
                float bottom = Math.max(touch1.y, touch2.y);

                rectanglePaint.setColor(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? Color.argb(55, 0, 0, 255) : Color.argb(35, 255, 0, 0));
                rectanglePaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(left, top, right, bottom, rectanglePaint);
            }
        }

        if (touch1.id != -1) {
            dotPaint.setColor(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? Color.argb(150, 0, 0, 255) : Color.argb(150, 255, 0, 0));
            canvas.drawCircle(touch1.x, touch1.y, dotRadius, dotPaint);
            dotPaint.setColor(DOT_COLOR);
        }
        if (touch2.id != -1) {
            dotPaint.setColor(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? Color.argb(150, 0, 0, 255) : Color.argb(150, 255, 0, 0));
            canvas.drawCircle(touch2.x, touch2.y, dotRadius, dotPaint);
            dotPaint.setColor(DOT_COLOR);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);

        int width = getWidth();
        int height = getHeight();

        int horizontalSpacing = width / NUM_COLUMNS;
        int verticalSpacing = height / NUM_ROWS;

        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLUMNS; col++) {
                float x = col * horizontalSpacing + horizontalSpacing / 2f;
                float y = row * verticalSpacing + verticalSpacing / 2f;
                gameActivity.getGameSim().getGameGrid().grid[row][col].setX(x);
                gameActivity.getGameSim().getGameGrid().grid[row][col].setY(y);
            }
        }

        dotRadius = Math.round(getWidth() / 75f);
        dotCaptureRadius = dotRadius * 9;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameActivity.getGameSim().getTouchesDisabled()) {
            return false;
        }

        int id = event.getPointerId(event.getActionIndex());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                touch1.id = id;
                touch2.id = -1;
                getPositions(event);
                touch1.copyToLast();
                invalidate();
                return true;

            case MotionEvent.ACTION_POINTER_DOWN:
                if (gameActivity.getGameSim().getCaptureType() == CaptureType.DOT) {
                    return false;
                }
                if (touch1.id >= 0 && touch2.id < 0) {
                    touch2.id = id;
                    getPositions(event);
                    touch2.copyToLast();
                    return true;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                getPositions(event);
                if (gameActivity.getGameSim().getCaptureType() == CaptureType.LINE) {
                    adjustLineWidth();
                }
                move();
                return true;

            case MotionEvent.ACTION_UP:
                if (event.getPointerCount() == 1 && event.getActionMasked() == MotionEvent.ACTION_UP) {
                    performClick();
                }
                handleCapture();
                touch1.id = -1;
                touch2.id = -1;
                return true;
        }

        return super.onTouchEvent(event);
    }

    private void getPositions(MotionEvent event) {
        for (int i = 0; i < event.getPointerCount(); i++) {

            // Get the pointer id
            int id = event.getPointerId(i);

            float x = event.getX(i);
            float y = event.getY(i);

            if (id == touch1.id) {
                touch1.copyToLast();
                touch1.x = x;
                touch1.y = y;
            } else if (id == touch2.id) {
                touch2.copyToLast();
                touch2.x = x;
                touch2.y = y;
            }
        }
    }

    /**
     * Handle movement of the touches
     */
    private void move() {
        // If no touch1, we have nothing to do
        // This should not happen, but it never hurts
        // to check.
        if (touch1.id < 0) {
            return;
        } else {
            // At least one touch
            touch1.computeDeltas();
            gameActivity.getGameSim().setCaptureItemX(gameActivity.getGameSim().getCaptureItemX() + touch1.dX);
            gameActivity.getGameSim().setCaptureItemY(gameActivity.getGameSim().getCaptureItemY() + touch1.dY);

            touch1.copyToLast();
        }
        invalidate();
    }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    /**
     * Calculate the distance between two points
     *
     * @param x1 first point x
     * @param y1 first point y
     * @param x2 second point x
     * @param y2 second point y
     * @return distance between the points
     */
    private float distance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Check if a dot is underneath the line defined by two touch points.
     *
     * @param dotX   X coordinate of the dot
     * @param dotY   Y coordinate of the dot
     * @param lineX1 X coordinate of the first touch point of the line
     * @param lineY1 Y coordinate of the first touch point of the line
     * @param lineX2 X coordinate of the second touch point of the line
     * @param lineY2 Y coordinate of the second touch point of the line
     * @return true if the dot is underneath the line, false otherwise
     */
    private boolean isDotUnderLine(float dotX, float dotY, float lineX1, float lineY1, float lineX2, float lineY2) {
        float r = dotRadius * 3;

        // Calculate the closest point on the line segment to the dot
        float closestX, closestY;
        float dx = lineX2 - lineX1;
        float dy = lineY2 - lineY1;
        float t = ((dotX - lineX1) * dx + (dotY - lineY1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t)); // Ensure t is between 0 and 1
        closestX = lineX1 + t * dx;
        closestY = lineY1 + t * dy;

        // Check if the distance between the dot and the closest point is within DOT_RADIUS * 2

        return distance(dotX, dotY, closestX, closestY) <= r;
    }

    private boolean isDotWithinDotCaptureRadius(float dotX, float dotY, float touchX, float touchY) {
        return distance(dotX, dotY, touchX, touchY) <= dotCaptureRadius;
    }

    /**
     * Check if a dot is inside the rectangle defined by two touch points.
     *
     * @param dotX       X coordinate of the dot
     * @param dotY       Y coordinate of the dot
     * @param rectLeft   X coordinate of the left side of the rectangle
     * @param rectTop    Y coordinate of the top side of the rectangle
     * @param rectRight  X coordinate of the right side of the rectangle
     * @param rectBottom Y coordinate of the bottom side of the rectangle
     * @return true if the dot is inside the rectangle, false otherwise
     */
    private boolean isDotInsideRectangle(float dotX, float dotY, float rectLeft, float rectTop, float rectRight, float rectBottom) {
        return ((dotX >= rectLeft && dotX <= rectRight) && (dotY >= rectTop && dotY <= rectBottom))
                || ((dotX >= rectLeft && dotX <= rectRight) && (dotY >= rectBottom && dotY <= rectTop))
                || ((dotX >= rectRight && dotX <= rectLeft) && (dotY >= rectTop && dotY <= rectBottom))
                || ((dotX >= rectRight && dotX <= rectLeft) && (dotY >= rectBottom && dotY <= rectTop));
    }

    private static class Touch {
        /**
         * Touch id
         */
        public int id = -1;

        /**
         * Current x location
         */
        public float x = 0;

        /**
         * Current y location
         */
        public float y = 0;

        /**
         * Previous x location
         */
        public float lastX = 0;

        /**
         * Previous y location
         */
        public float lastY = 0;

        /**
         * Change in x value from previous
         */
        public float dX = 0;

        /**
         * Change in y value from previous
         */
        public float dY = 0;

        /**
         * Compute the values of dX and dY
         */
        public void computeDeltas() {
            dX = x - lastX;
            dY = y - lastY;
        }

        /**
         * Copy the current values to the previous values
         */
        public void copyToLast() {
            lastX = x;
            lastY = y;
        }
    }

    private void handleCapture() {
        float rectWidth = Math.abs(touch1.x - touch2.x);
        float rectHeight = Math.abs(touch1.y - touch2.y);
        float rectArea = rectWidth * rectHeight;
        float rectAreaGameAreaRatio = rectArea / (getWidth() * getHeight());
        if (rectAreaGameAreaRatio > 0.5) {
            rectangleCaptureProbability = 1 - rectAreaGameAreaRatio;
            if (rectangleCaptureProbability < 0.1f) { // don't let the probability get below 10%
                rectangleCaptureProbability = 0.1f;
            }
        }

        for (GridTile[] row : gameActivity.getGameSim().getGameGrid().grid) {
            for (GridTile gridTile : row) {
                if (gridTile.getStatus() != TileStatus.EMPTY) {
                    continue;
                }
                if (gameActivity.getGameSim().getCaptureType() == CaptureType.RECTANGLE) {
                    if (isDotInsideRectangle(gridTile.getX(), gridTile.getY(), touch1.x, touch1.y, touch2.x, touch2.y)) {
                        if (Math.random() < rectangleCaptureProbability) {
                            captureTile(gridTile);
                        }
                    }
                } else if (gameActivity.getGameSim().getCaptureType() == CaptureType.LINE) {
                    if (isDotUnderLine(gridTile.getX(), gridTile.getY(), touch1.x, touch1.y, touch2.x, touch2.y)) {
                        float lineCaptureProbability = 0.75f;
                        if (Math.random() < lineCaptureProbability) {
                            captureTile(gridTile);
                        }
                    }
                } else {
                    if (isDotWithinDotCaptureRadius(gridTile.getX(), gridTile.getY(), touch1.x, touch1.y)) {
                        captureTile(gridTile);
                    }
                }
            }
        }
        rectangleCaptureProbability = 0.5f;
        gameActivity.updateUI();

        invalidate();
        gameActivity.getGameSim().setTouchesDisabled(true);

        Handler handler = new Handler();
        handler.postDelayed(this::launchCaptureSelectActivity, 2000);
    }

    void captureTile(GridTile gridTile) {
        if (gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1) {
            gameActivity.getGameSim().setPlayerOneScore(gameActivity.getGameSim().getPlayerOneScore() + 1);
            gridTile.setStatus(TileStatus.PLAYER_ONE);
        } else {
            gameActivity.getGameSim().setPlayerTwoScore(gameActivity.getGameSim().getPlayerTwoScore() + 1);
            gridTile.setStatus(TileStatus.PLAYER_TWO);
        }
    }

    void launchCaptureSelectActivity() {
        if(gameActivity.getGameSim().getWinningPlayer() == null) {
            gameActivity.getGameSim().setTouchesDisabled(false);
            gameActivity.getGameSim().setCurrentTurnPlayerNumber(gameActivity.getGameSim().getCurrentTurnPlayerNumber() == 1 ? 2 : 1);
            Intent intent = new Intent(gameActivity, CaptureSelectActivity.class);
            intent.putExtra("Sim", gameActivity.getGameSim().getState());
            gameActivity.startActivity(intent);
        }
        //Winner Winner Chicken Dinner. Run the winner screen
        else{
            Intent intent = new Intent(gameActivity, PostGameActivity.class);
            intent.putExtra("postgame_winner_name", gameActivity.getGameSim().getWinningPlayer());
            gameActivity.startActivity(intent);
        }
    }

    private void adjustLineWidth() {
        if (touch1.id != -1 && touch2.id != -1) {
            float gameAreaWidth = getWidth();
            float desiredDistance = gameAreaWidth * 0.7f;

            // calculate current distance between touch1 and touch2
            float currentDistance = distance(touch1.x, touch1.y, touch2.x, touch2.y);

            // calculate the ratio we need for the line to be relative to what it is now
            float scaleRatio = desiredDistance / currentDistance;

            // calculate the midpoint
            float midPointX = (touch1.x + touch2.x) / 2;
            float midPointY = (touch1.y + touch2.y) / 2;

            // calculate the new positions of touch1 and touch2
            float newTouch1X = midPointX + (touch1.x - midPointX) * scaleRatio;
            float newTouch1Y = midPointY + (touch1.y - midPointY) * scaleRatio;

            float newTouch2X = midPointX + (touch2.x - midPointX) * scaleRatio;
            float newTouch2Y = midPointY + (touch2.y - midPointY) * scaleRatio;

            // update the positions
            touch1.x = newTouch1X;
            touch1.y = newTouch1Y;
            touch2.x = newTouch2X;
            touch2.y = newTouch2Y;

            // update stroke width
            linePaint.setStrokeWidth(desiredDistance);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        // Handle the click event here or perform any necessary action
        return true;
    }
}
