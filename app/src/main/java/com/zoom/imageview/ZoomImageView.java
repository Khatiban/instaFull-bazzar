package com.zoom.imageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.OverScroller;
import android.widget.Scroller;

import com.fury.instafull.R;

public class ZoomImageView extends ImageView implements OnTouchListener, OnGlobalLayoutListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType = null;
    public static final float DEFAULT_MAX_SCALE = 3.0f;
    public static final float DEFAULT_MID_SCALE = 1.75f;
    public static final float DEFAULT_MIN_SCALE = 1.0f;
    private static final int EDGE_BOTH = 2;
    private static final int EDGE_LEFT = 0;
    private static final int EDGE_NONE = -1;
    private static final int EDGE_RIGHT = 1;
    private boolean allowParentInterceptOnEdge;
    private final Matrix baseMatrix;
    private int bottom;
    private FlingRunnable currentFlingRunnable;
    private final RectF displayRect;
    private final Matrix drawMatrix;
    private boolean isZoomEnabled;
    private int left;
    private OnLongClickListener longClickListener;
    private final float[] matrixValues;
    private float maxScale;
    private float midScale;
    private float minScale;
    private MultiGestureDetector multiGestureDetector;
    private OnPhotoTapListener photoTapListener;
    private int right;
    private ScaleType scaleType;
    private int scrollEdge;
    private final Matrix suppMatrix;
    private int top;
    private OnViewTapListener viewTapListener;

    private class AnimatedZoomRunnable implements Runnable {
        static final float ANIMATION_SCALE_PER_ITERATION_IN = 1.07f;
        static final float ANIMATION_SCALE_PER_ITERATION_OUT = 0.93f;
        private final float deltaScale;
        private final float focalX;
        private final float focalY;
        private final float targetZoom;

        public AnimatedZoomRunnable(float currentZoom, float targetZoom, float focalX, float focalY) {
            this.targetZoom = targetZoom;
            this.focalX = focalX;
            this.focalY = focalY;
            if (currentZoom < targetZoom) {
                this.deltaScale = ANIMATION_SCALE_PER_ITERATION_IN;
            } else {
                this.deltaScale = ANIMATION_SCALE_PER_ITERATION_OUT;
            }
        }

        public void run() {
            ZoomImageView.this.suppMatrix.postScale(this.deltaScale, this.deltaScale, this.focalX, this.focalY);
            ZoomImageView.this.checkAndDisplayMatrix();
            float currentScale = ZoomImageView.this.getScale();
            if ((this.deltaScale <= ZoomImageView.DEFAULT_MIN_SCALE || currentScale >= this.targetZoom) && (this.deltaScale >= ZoomImageView.DEFAULT_MIN_SCALE || this.targetZoom >= currentScale)) {
                float delta = this.targetZoom / currentScale;
                ZoomImageView.this.suppMatrix.postScale(delta, delta, this.focalX, this.focalY);
                ZoomImageView.this.checkAndDisplayMatrix();
                return;
            }
            ZoomImageView.this.postOnAnimation(ZoomImageView.this, this);
        }
    }

    private class FlingRunnable implements Runnable {
        private int currentX;
        private int currentY;
        private final ScrollerProxy scroller;

        public FlingRunnable(Context context) {
            this.scroller = new ScrollerProxy(context);
        }

        public void cancelFling() {
            this.scroller.forceFinished(true);
        }

        public void fling(int viewWidth, int viewHeight, int velocityX, int velocityY) {
            RectF rect = ZoomImageView.this.getDisplayRect();
            if (rect != null) {
                int minX;
                int maxX;
                int minY;
                int maxY;
                int startX = Math.round(-rect.left);
                if (((float) viewWidth) < rect.width()) {
                    minX = ZoomImageView.EDGE_LEFT;
                    maxX = Math.round(rect.width() - ((float) viewWidth));
                } else {
                    maxX = startX;
                    minX = startX;
                }
                int startY = Math.round(-rect.top);
                if (((float) viewHeight) < rect.height()) {
                    minY = ZoomImageView.EDGE_LEFT;
                    maxY = Math.round(rect.height() - ((float) viewHeight));
                } else {
                    maxY = startY;
                    minY = startY;
                }
                this.currentX = startX;
                this.currentY = startY;
                if (startX != maxX || startY != maxY) {
                    this.scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, ZoomImageView.EDGE_LEFT, ZoomImageView.EDGE_LEFT);
                }
            }
        }

        public void run() {
            if (this.scroller.computeScrollOffset()) {
                int newX = this.scroller.getCurrX();
                int newY = this.scroller.getCurrY();
                ZoomImageView.this.suppMatrix.postTranslate((float) (this.currentX - newX), (float) (this.currentY - newY));
                ZoomImageView.this.setImageMatrix(ZoomImageView.this.getDisplayMatrix());
                this.currentX = newX;
                this.currentY = newY;
                ZoomImageView.this.postOnAnimation(ZoomImageView.this, this);
            }
        }
    }

    private class MultiGestureDetector extends SimpleOnGestureListener implements OnScaleGestureListener {
        private final GestureDetector gestureDetector;
        private boolean isDragging;
        private float lastPointerCount;
        private float lastTouchX;
        private float lastTouchY;
        private final ScaleGestureDetector scaleGestureDetector;
        private final float scaledMinimumFlingVelocity;
        private final float scaledTouchSlop;
        private VelocityTracker velocityTracker;

        public MultiGestureDetector(Context context) {
            this.scaleGestureDetector = new ScaleGestureDetector(context, this);
            this.gestureDetector = new GestureDetector(context, this);
            this.gestureDetector.setOnDoubleTapListener(this);
            ViewConfiguration configuration = ViewConfiguration.get(context);
            this.scaledMinimumFlingVelocity = (float) configuration.getScaledMinimumFlingVelocity();
            this.scaledTouchSlop = (float) configuration.getScaledTouchSlop();
        }

        public boolean isScaling() {
            return this.scaleGestureDetector.isInProgress();
        }

        public boolean onTouchEvent(MotionEvent event) {
            if (this.gestureDetector.onTouchEvent(event)) {
                return true;
            }
            this.scaleGestureDetector.onTouchEvent(event);
            float x = 0.0f;
            float y = 0.0f;
            int pointerCount = event.getPointerCount();
            for (int i = ZoomImageView.EDGE_LEFT; i < pointerCount; i += ZoomImageView.EDGE_RIGHT) {
                x += event.getX(i);
                y += event.getY(i);
            }
            x /= (float) pointerCount;
            y /= (float) pointerCount;
            if (((float) pointerCount) != this.lastPointerCount) {
                this.isDragging = false;
                if (this.velocityTracker != null) {
                    this.velocityTracker.clear();
                }
                this.lastTouchX = x;
                this.lastTouchY = y;
            }
            this.lastPointerCount = (float) pointerCount;
            switch (event.getAction()) {
                case ZoomImageView.EDGE_LEFT /*0*/:
                    if (this.velocityTracker == null) {
                        this.velocityTracker = VelocityTracker.obtain();
                    } else {
                        this.velocityTracker.clear();
                    }
                    this.velocityTracker.addMovement(event);
                    this.lastTouchX = x;
                    this.lastTouchY = y;
                    this.isDragging = false;
                    break;
                case ZoomImageView.EDGE_RIGHT /*1*/:
                    if (this.isDragging) {
                        this.lastTouchX = x;
                        this.lastTouchY = y;
                        this.velocityTracker.addMovement(event);
                        float vX = this.velocityTracker.getXVelocity();
                        float vY = this.velocityTracker.getYVelocity();
                        if (Math.max(Math.abs(vX), Math.abs(vY)) >= this.scaledMinimumFlingVelocity && ZoomImageView.this.getDrawable() != null) {
                            ZoomImageView.this.currentFlingRunnable = new FlingRunnable(ZoomImageView.this.getContext());
                            ZoomImageView.this.currentFlingRunnable.fling(ZoomImageView.this.getWidth(), ZoomImageView.this.getHeight(), (int) (-vX), (int) (-vY));
                            ZoomImageView.this.post(ZoomImageView.this.currentFlingRunnable);
                            break;
                        }
                    }
                    break;
                case ZoomImageView.EDGE_BOTH /*2*/:
                    float dx = x - this.lastTouchX;
                    float dy = y - this.lastTouchY;
                    if (!this.isDragging) {
                        this.isDragging = Math.sqrt((double) ((dx * dx) + (dy * dy))) >= ((double) this.scaledTouchSlop);
                    }
                    if (this.isDragging) {
                        if (ZoomImageView.this.getDrawable() != null) {
                            ZoomImageView.this.suppMatrix.postTranslate(dx, dy);
                            ZoomImageView.this.checkAndDisplayMatrix();
                        }
                        this.lastTouchX = x;
                        this.lastTouchY = y;
                        if (this.velocityTracker != null) {
                            this.velocityTracker.addMovement(event);
                            break;
                        }
                    }
                    break;
            }
            this.lastPointerCount = 0.0f;
            if (this.velocityTracker != null) {
                this.velocityTracker.recycle();
                this.velocityTracker = null;
            }
            return true;
        }

        public boolean onScale(ScaleGestureDetector detector) {
            float scale = ZoomImageView.this.getScale();
            float scaleFactor = detector.getScaleFactor();
            if (ZoomImageView.this.getDrawable() != null && ((scale < ZoomImageView.this.maxScale || scaleFactor <= ZoomImageView.DEFAULT_MIN_SCALE) && (((double) scale) > 0.75d || scaleFactor >= ZoomImageView.DEFAULT_MIN_SCALE))) {
                ZoomImageView.this.suppMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
                ZoomImageView.this.checkAndDisplayMatrix();
            }
            return true;
        }

        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector detector) {
        }

        public boolean onDoubleTap(MotionEvent event) {
            try {
                float scale = ZoomImageView.this.getScale();
                float x = event.getX();
                float y = event.getY();
                if (scale < ZoomImageView.this.midScale) {
                    ZoomImageView.this.post(new AnimatedZoomRunnable(scale, ZoomImageView.this.midScale, x, y));
                } else if (scale < ZoomImageView.this.midScale || scale >= ZoomImageView.this.maxScale) {
                    ZoomImageView.this.post(new AnimatedZoomRunnable(scale, ZoomImageView.this.minScale, x, y));
                } else {
                    ZoomImageView.this.post(new AnimatedZoomRunnable(scale, ZoomImageView.this.maxScale, x, y));
                }
            } catch (Exception e) {
            }
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent event) {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent event) {
            if (ZoomImageView.this.photoTapListener != null) {
                RectF displayRect = ZoomImageView.this.getDisplayRect();
                if (displayRect != null) {
                    float x = event.getX();
                    float y = event.getY();
                    if (displayRect.contains(x, y)) {
                        ZoomImageView.this.photoTapListener.onPhotoTap(ZoomImageView.this, (x - displayRect.left) / displayRect.width(), (y - displayRect.top) / displayRect.height());
                        return true;
                    }
                }
            }
            if (ZoomImageView.this.viewTapListener != null) {
                ZoomImageView.this.viewTapListener.onViewTap(ZoomImageView.this, event.getX(), event.getY());
            }
            return false;
        }

        public void onLongPress(MotionEvent e) {
            if (ZoomImageView.this.longClickListener != null) {
                ZoomImageView.this.longClickListener.onLongClick(ZoomImageView.this);
            }
        }
    }

    public interface OnPhotoTapListener {
        void onPhotoTap(View view, float f, float f2);
    }

    public interface OnViewTapListener {
        void onViewTap(View view, float f, float f2);
    }

    @TargetApi(9)
    private class ScrollerProxy {
        private boolean isOld;
        private Object scroller;

        public ScrollerProxy(Context context) {
            if (VERSION.SDK_INT < 9) {
                this.isOld = true;
                this.scroller = new Scroller(context);
                return;
            }
            this.isOld = false;
            this.scroller = new OverScroller(context);
        }

        public boolean computeScrollOffset() {
            if (this.isOld) {
                return ((Scroller) this.scroller).computeScrollOffset();
            }
            return ((OverScroller) this.scroller).computeScrollOffset();
        }

        public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
            if (this.isOld) {
                ((Scroller) this.scroller).fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY);
            } else {
                ((OverScroller) this.scroller).fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, overX, overY);
            }
        }

        public void forceFinished(boolean finished) {
            if (this.isOld) {
                ((Scroller) this.scroller).forceFinished(finished);
            } else {
                ((OverScroller) this.scroller).forceFinished(finished);
            }
        }

        public int getCurrX() {
            return this.isOld ? ((Scroller) this.scroller).getCurrX() : ((OverScroller) this.scroller).getCurrX();
        }

        public int getCurrY() {
            return this.isOld ? ((Scroller) this.scroller).getCurrY() : ((OverScroller) this.scroller).getCurrY();
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$android$widget$ImageView$ScaleType() {
        int[] iArr = $SWITCH_TABLE$android$widget$ImageView$ScaleType;
        if (iArr == null) {
            iArr = new int[ScaleType.values().length];
            try {
                iArr[ScaleType.CENTER.ordinal()] = EDGE_RIGHT;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[ScaleType.CENTER_CROP.ordinal()] = EDGE_BOTH;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$android$widget$ImageView$ScaleType = iArr;
        }
        return iArr;
    }

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attr) {
        this(context, attr, EDGE_LEFT);
    }

    public ZoomImageView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        this.minScale = DEFAULT_MIN_SCALE;
        this.midScale = DEFAULT_MID_SCALE;
        this.maxScale = DEFAULT_MAX_SCALE;
        this.allowParentInterceptOnEdge = true;
        this.baseMatrix = new Matrix();
        this.drawMatrix = new Matrix();
        this.suppMatrix = new Matrix();
        this.displayRect = new RectF();
        this.matrixValues = new float[9];
        this.scrollEdge = EDGE_BOTH;
        this.scaleType = ScaleType.FIT_CENTER;
        super.setScaleType(ScaleType.MATRIX);
        setOnTouchListener(this);
        this.multiGestureDetector = new MultiGestureDetector(context);
        setIsZoomEnabled(true);
    }

    public final RectF getDisplayRect() {
        checkMatrixBounds();
        return getDisplayRect(getDisplayMatrix());
    }

    public float getMinScale() {
        return this.minScale;
    }

    public void setMinScale(float minScale) {
        checkZoomLevels(minScale, this.midScale, this.maxScale);
        this.minScale = minScale;
    }

    public float getMidScale() {
        return this.midScale;
    }

    public void setMidScale(float midScale) {
        checkZoomLevels(this.minScale, midScale, this.maxScale);
        this.midScale = midScale;
    }

    public float getMaxScale() {
        return this.maxScale;
    }

    public void setMaxScale(float maxScale) {
        checkZoomLevels(this.minScale, this.midScale, maxScale);
        this.maxScale = maxScale;
    }

    public final float getScale() {
        this.suppMatrix.getValues(this.matrixValues);
        return this.matrixValues[EDGE_LEFT];
    }

    public final ScaleType getScaleType() {
        return this.scaleType;
    }

    public final void setScaleType(ScaleType scaleType) {
        if (scaleType == ScaleType.MATRIX) {
            throw new IllegalArgumentException(scaleType.name() + " is not supported in ZoomImageView");
        } else if (scaleType != this.scaleType) {
            this.scaleType = scaleType;
            update();
        }
    }

    public final boolean isZoomEnabled() {
        return this.isZoomEnabled;
    }

    public final void setIsZoomEnabled(boolean isZoomEnabled) {
        this.isZoomEnabled = isZoomEnabled;
        update();
    }

    public void setAllowParentInterceptOnEdge(boolean allowParentInterceptOnEdge) {
        this.allowParentInterceptOnEdge = allowParentInterceptOnEdge;
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        update();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        update();
    }

    public void setImageResource(int resId) {
        super.setImageResource(resId);
        update();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        update();
    }

    public final void setOnLongClickListener(OnLongClickListener listener) {
        this.longClickListener = listener;
    }

    public final void setOnPhotoTapListener(OnPhotoTapListener listener) {
        this.photoTapListener = listener;
    }

    public final void setOnViewTapListener(OnViewTapListener listener) {
        this.viewTapListener = listener;
    }

    public final void onGlobalLayout() {
        if (this.isZoomEnabled) {
            int top = getTop();
            int right = getRight();
            int bottom = getBottom();
            int left = getLeft();
            if (top != this.top || bottom != this.bottom || left != this.left || right != this.right) {
                updateBaseMatrix(getDrawable());
                this.top = top;
                this.right = right;
                this.bottom = bottom;
                this.left = left;
            }
        }
    }

    public final boolean onTouch(View v, MotionEvent ev) {
        boolean handled = false;
        if (!this.isZoomEnabled) {
            return false;
        }
        switch (ev.getAction()) {
            case EDGE_LEFT /*0*/:
                if (v.getParent() != null) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                if (this.currentFlingRunnable != null) {
                    this.currentFlingRunnable.cancelFling();
                    this.currentFlingRunnable = null;
                    break;
                }
                break;
            case EDGE_RIGHT /*1*/:
            case R.styleable.View_paddingEnd /*3*/:
                if (getScale() < this.minScale) {
                    RectF rect = getDisplayRect();
                    if (rect != null) {
                        v.post(new AnimatedZoomRunnable(getScale(), this.minScale, rect.centerX(), rect.centerY()));
                        handled = true;
                        break;
                    }
                }
                break;
        }
        if (this.multiGestureDetector == null || !this.multiGestureDetector.onTouchEvent(ev)) {
            return handled;
        }
        return true;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    protected Matrix getDisplayMatrix() {
        this.drawMatrix.set(this.baseMatrix);
        this.drawMatrix.postConcat(this.suppMatrix);
        return this.drawMatrix;
    }

    private final void update() {
        if (this.isZoomEnabled) {
            super.setScaleType(ScaleType.MATRIX);
            updateBaseMatrix(getDrawable());
            return;
        }
        resetMatrix();
    }

    private void checkAndDisplayMatrix() {
        checkMatrixBounds();
        setImageMatrix(getDisplayMatrix());
    }

    private void checkMatrixBounds() {
        RectF rect = getDisplayRect(getDisplayMatrix());
        if (rect != null) {
            float height = rect.height();
            float width = rect.width();
            float deltaX = 0.0f;
            float deltaY = 0.0f;
            int viewHeight = getHeight();
            if (height <= ((float) viewHeight)) {
                switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[this.scaleType.ordinal()]) {
                    case R.styleable.Toolbar_contentInsetStart /*5*/:
                        deltaY = (((float) viewHeight) - height) - rect.top;
                        break;
                    case R.styleable.Toolbar_contentInsetEnd /*6*/:
                        deltaY = -rect.top;
                        break;
                    default:
                        deltaY = ((((float) viewHeight) - height) / 2.0f) - rect.top;
                        break;
                }
            } else if (rect.top > 0.0f) {
                deltaY = -rect.top;
            } else if (rect.bottom < ((float) viewHeight)) {
                deltaY = ((float) viewHeight) - rect.bottom;
            }
            int viewWidth = getWidth();
            if (width <= ((float) viewWidth)) {
                switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[this.scaleType.ordinal()]) {
                    case R.styleable.Toolbar_contentInsetStart /*5*/:
                        deltaX = (((float) viewWidth) - width) - rect.left;
                        break;
                    case R.styleable.Toolbar_contentInsetEnd /*6*/:
                        deltaX = -rect.left;
                        break;
                    default:
                        deltaX = ((((float) viewWidth) - width) / 2.0f) - rect.left;
                        break;
                }
                this.scrollEdge = EDGE_BOTH;
            } else if (rect.left > 0.0f) {
                this.scrollEdge = EDGE_LEFT;
                deltaX = -rect.left;
            } else if (rect.right < ((float) viewWidth)) {
                deltaX = ((float) viewWidth) - rect.right;
                this.scrollEdge = EDGE_RIGHT;
            } else {
                this.scrollEdge = EDGE_NONE;
            }
            this.suppMatrix.postTranslate(deltaX, deltaY);
        }
    }

    private RectF getDisplayRect(Matrix matrix) {
        Drawable d = getDrawable();
        if (d == null) {
            return null;
        }
        this.displayRect.set(0.0f, 0.0f, (float) d.getIntrinsicWidth(), (float) d.getIntrinsicHeight());
        matrix.mapRect(this.displayRect);
        return this.displayRect;
    }

    private void resetMatrix() {
        this.suppMatrix.reset();
        setImageMatrix(getDisplayMatrix());
        checkMatrixBounds();
    }

    private void updateBaseMatrix(Drawable d) {
        if (d != null) {
            float viewWidth = (float) getWidth();
            float viewHeight = (float) getHeight();
            int drawableWidth = d.getIntrinsicWidth();
            int drawableHeight = d.getIntrinsicHeight();
            this.baseMatrix.reset();
            float widthScale = viewWidth / ((float) drawableWidth);
            float heightScale = viewHeight / ((float) drawableHeight);
            if (this.scaleType != ScaleType.CENTER) {
                float scale;
                if (this.scaleType != ScaleType.CENTER_CROP) {
                    if (this.scaleType != ScaleType.CENTER_INSIDE) {
                        RectF mTempSrc = new RectF(0.0f, 0.0f, (float) drawableWidth, (float) drawableHeight);
                        RectF mTempDst = new RectF(0.0f, 0.0f, viewWidth, viewHeight);
                        switch ($SWITCH_TABLE$android$widget$ImageView$ScaleType()[this.scaleType.ordinal()]) {
                            case R.styleable.View_theme /*4*/:
                                this.baseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER);
                                break;
                            case R.styleable.Toolbar_contentInsetStart /*5*/:
                                this.baseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END);
                                break;
                            case R.styleable.Toolbar_contentInsetLeft /*7*/:
                                this.baseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL);
                                break;
                            default:
                                break;
                        }
                    }
                    scale = Math.min(DEFAULT_MIN_SCALE, Math.min(widthScale, heightScale));
                    this.baseMatrix.postScale(scale, scale);
                    this.baseMatrix.postTranslate((viewWidth - (((float) drawableWidth) * scale)) / 2.0f, (viewHeight - (((float) drawableHeight) * scale)) / 2.0f);
                } else {
                    scale = Math.max(widthScale, heightScale);
                    this.baseMatrix.postScale(scale, scale);
                    this.baseMatrix.postTranslate((viewWidth - (((float) drawableWidth) * scale)) / 2.0f, (viewHeight - (((float) drawableHeight) * scale)) / 2.0f);
                }
            } else {
                this.baseMatrix.postTranslate((viewWidth - ((float) drawableWidth)) / 2.0f, (viewHeight - ((float) drawableHeight)) / 2.0f);
            }
            resetMatrix();
        }
    }

    @TargetApi(16)
    private void postOnAnimation(View view, Runnable runnable) {
        if (VERSION.SDK_INT >= 16) {
            view.postOnAnimation(runnable);
        } else {
            view.postDelayed(runnable, 16);
        }
    }

    private void checkZoomLevels(float minZoom, float midZoom, float maxZoom) {
        if (minZoom >= midZoom) {
            throw new IllegalArgumentException("MinZoom should be less than MidZoom");
        } else if (midZoom >= maxZoom) {
            throw new IllegalArgumentException("MidZoom should be less than MaxZoom");
        }
    }
}
