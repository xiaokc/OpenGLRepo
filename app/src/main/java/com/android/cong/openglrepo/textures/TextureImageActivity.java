package com.android.cong.openglrepo.textures;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class TextureImageActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        glSurfaceView = new TextureImageGLSurfaceView(this);
        setContentView(glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
