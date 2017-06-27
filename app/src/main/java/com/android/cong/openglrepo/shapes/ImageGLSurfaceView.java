package com.android.cong.openglrepo.shapes;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class ImageGLSurfaceView extends GLSurfaceView {
    private final ImageRenderer mRenderer;
    private float mPreviousX;
    private float mPreviousY;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;

    public ImageGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new ImageRenderer();

        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if (y > getHeight() / 2) {
                    dx = -dx;
                }

                if (x < getWidth() / 2) {
                    dy = -dy;
                }
                mRenderer.setAngle(mRenderer.getAngle() + (dx + dy) * TOUCH_SCALE_FACTOR);
                requestRender();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        mPreviousX = x;
        mPreviousY = y;

        return true;
    }
}
