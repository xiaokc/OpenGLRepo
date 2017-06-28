package com.android.cong.openglrepo.textures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.android.cong.openglrepo.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.SystemClock;

/**
 * Created by xiaokecong on 27/06/2017.
 */

public class TextureImageRenderer implements GLSurfaceView.Renderer {
    private Context mContext;
    private long mLastTime;
    private int mProgram;

    // Geometric variables
    public static float vertices[];
    public static short indices[];
    public static float uvs[];
    public FloatBuffer vertexBuffer;
    public ShortBuffer drawListBuffer;
    public FloatBuffer uvBuffer;

    // Our matrices
    private final float[] mtrxProjection = new float[16];
    private final float[] mtrxView = new float[16];
    private final float[] mtrxProjectionAndView = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private final float[] mScratchMatrix = new float[16];

    public TextureImageRenderer(Context context) {
        this.mContext = context;
        mLastTime = System.currentTimeMillis() + 100;

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setUpTriangle();

        setUpImage();

        GLES20.glClearColor(0f, 0f, 0f, 1f);

        // Create the shaders, solid color
        //        int vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER, Utils.vs_SolidColor);
        //        int fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, Utils.fs_SolidColor);
        //
        //        Utils.sp_SolidColor = GLES20.glCreateProgram();
        //        GLES20.glAttachShader(Utils.sp_SolidColor, vertexShader);
        //        GLES20.glAttachShader(Utils.sp_SolidColor, fragmentShader);
        //        GLES20.glLinkProgram(Utils.sp_SolidColor);

        int vertexShader = Utils.loadShader(GLES20.GL_VERTEX_SHADER, Utils.vs_Image);
        int fragmentShader = Utils.loadShader(GLES20.GL_FRAGMENT_SHADER, Utils.fs_Image);

        Utils.sp_Image = GLES20.glCreateProgram();
        GLES20.glAttachShader(Utils.sp_Image, vertexShader);
        GLES20.glAttachShader(Utils.sp_Image, fragmentShader);
        GLES20.glLinkProgram(Utils.sp_Image);

        GLES20.glUseProgram(Utils.sp_Image);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        // Setup our screen width and height for normal sprite translation.
        // Matrix.orthoM(mtrxProjection, 0, 0f, width, 0f, height, 3f, 7f);
        float ratio = (float) width / height;
        Matrix.frustumM(mtrxProjection, 0, -ratio, ratio, -1f, 1f, 3f, 7f);


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        long now = System.currentTimeMillis();

        if (mLastTime > now) {
            return;
        }

        // Get the amount of time the last frame took.
        long elapsed = now - mLastTime;

        // Update our example

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mtrxView, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mtrxProjectionAndView, 0, mtrxProjection, 0, mtrxView, 0);

        long time = SystemClock.uptimeMillis() % 4000L;
        float angle = 0.09f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0f, 0f, -1f);

        Matrix.multiplyMM(mScratchMatrix, 0, mtrxProjectionAndView, 0, mRotationMatrix,0);

        // Render our example
        render(mScratchMatrix);

        // Save the current time to see how long it took :).
        mLastTime = now;

    }

    public void render(float[] m) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // get handle to vertex shader's vPosition member,
        // Enable generic vertex attribute array,
        // Prepare the triangle coordinate data
        int mPositionHandle = GLES20.glGetAttribLocation(Utils.sp_Image, "vPosition");
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        GLES20.glVertexAttribPointer(mPositionHandle, 3, GLES20.GL_FLOAT, false, 0, vertexBuffer);

        // Get handle to texture coordinates location
        // Enable generic vertex attribute array
        // Prepare the texturecoordinates
        int mTexCoordLoc = GLES20.glGetAttribLocation(Utils.sp_Image, "a_texCoord");
        GLES20.glEnableVertexAttribArray(mTexCoordLoc);
        GLES20.glVertexAttribPointer(mTexCoordLoc, 2, GLES20.GL_FLOAT, false, 0, uvBuffer);

        // Get handle to shape's transformation matrix
        // Apply the projection and view transformation
        int mtrxHandle = GLES20.glGetUniformLocation(Utils.sp_Image, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(mtrxHandle, 1, false, m, 0);

        // Get handle to textures locations
        // Set the sampler texture unit to 0, where we have saved the texture.
        int mSampleLoc = GLES20.glGetUniformLocation(Utils.sp_Image, "s_texture");
        GLES20.glUniform1i(mSampleLoc, 0);

        // Draw the triangle
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, indices.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mTexCoordLoc);
    }

    private void setUpTriangle() {
        vertices = new float[] {
                -1f, 1f, 0.0f,
                -1f, -1f, 0.0f,
                1f, -1f, 0.0f,
                1f, 1f, 0.0f,
        };

        indices = new short[] {0, 1, 2, 0, 2, 3}; // The order of vertex rendering.

        vertexBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertices);
        vertexBuffer.position(0);

        drawListBuffer = ByteBuffer.allocateDirect(indices.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(indices);
        drawListBuffer.position(0);
    }

    private void setUpImage() {
        // Create our UV coordinates.
        uvs = new float[] {
                0.0f, 0.0f,
                0.0f, 1.0f,
                1.0f, 1.0f,
                1.0f, 0.0f
        };

        uvBuffer = ByteBuffer.allocateDirect(uvs.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(uvs);
        uvBuffer.position(0);

        int[] textureids = new int[1];
        GLES20.glGenTextures(1, textureids, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.air_hockey_surface);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureids[0]);

        // Set filtering
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        bitmap.recycle();
    }

    public void onResume() {
        mLastTime = System.currentTimeMillis();
    }

    public void onPause() {

    }
}
