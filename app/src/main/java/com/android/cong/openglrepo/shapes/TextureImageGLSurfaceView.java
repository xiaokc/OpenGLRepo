package com.android.cong.openglrepo.shapes;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class TextureImageGLSurfaceView extends GLSurfaceView {
    private final TextureImageRenderer mRenderer;
    public TextureImageGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new TextureImageRenderer();

        setRenderer(mRenderer);


    }
}
