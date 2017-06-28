package com.android.cong.openglrepo.textures;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class TextureImageGLSurfaceView extends GLSurfaceView {
    private final TextureImageRenderer renderer;

    public TextureImageGLSurfaceView(Context context) {
        super(context);

        setEGLContextClientVersion(2);

        renderer = new TextureImageRenderer(context);
        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public void onPause() {
        super.onPause();
        renderer.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        renderer.onResume();
    }
}
