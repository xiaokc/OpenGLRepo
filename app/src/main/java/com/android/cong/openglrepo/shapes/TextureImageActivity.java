package com.android.cong.openglrepo.shapes;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class TextureImageActivity extends Activity {
    private GLSurfaceView mGLView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new TextureImageGLSurfaceView(this);
        setContentView(mGLView);
    }
}
