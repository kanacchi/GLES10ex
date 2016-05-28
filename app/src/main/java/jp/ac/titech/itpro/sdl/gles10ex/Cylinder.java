package jp.ac.titech.itpro.sdl.gles10ex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Cylinder implements SimpleRenderer.Obj {

    private FloatBuffer vTopBuf;
    private FloatBuffer vBottomBuf;
    private FloatBuffer vSideBuf;;
    private float x, y, z;
    private int NUM_VERTICES;
    private float[] nVSide;

    public Cylinder(int n, float r, float l, float x, float y, float z) {
        NUM_VERTICES = n;
        float delta = (float)(Math.PI / 180) * (360f / NUM_VERTICES);

        float[] vTop = new float[NUM_VERTICES * 3];
        float[] vBottom = new float[NUM_VERTICES * 3];
        float[] vSide = new float[(NUM_VERTICES + 1) * 2 * 3];
        nVSide = new float[NUM_VERTICES * 3];

        //上面
        for(int i = 0,k = 0; i < NUM_VERTICES; i++) {
            vTop[k++] = (float) (r * Math.cos(delta * i));
            vTop[k++] = (float) (r * Math.sin(delta * i));
            vTop[k++] = 0f;
        }

        //下面
         for(int i = 0,k = 0; i < NUM_VERTICES; i++) {
            vBottom[k++] = (float) (r * Math.cos(delta * i));
            vBottom[k++] = (float) (r * Math.sin(delta * i));
            vBottom[k++] = l;
         }

        //側面の法線ベクトル
        for(int i = 0, k = 0; i < NUM_VERTICES; i++) {
            nVSide[k++] = (float) (r * Math.cos(delta * i + delta / 2));
            nVSide[k++] = (float) (r * Math.sin(delta * i + delta / 2));
            nVSide[k++] = l / 2;
        }

        //側面
        for(int i = 0, k = 0; i <= NUM_VERTICES; i++) {
            if(i != NUM_VERTICES) {
                vSide[k++] = (float) (r * Math.cos(delta * i));
                vSide[k++] = (float) (r * Math.sin(delta * i));
                vSide[k++] = 0f;
                vSide[k++] = (float) (r * Math.cos(delta * i));
                vSide[k++] = (float) (r * Math.sin(delta * i));
                vSide[k++] = l;
            } else {
                vSide[k++] = (float)r;
                vSide[k++] = 0f;
                vSide[k++] = 0f;
                vSide[k++] = (float)r;
                vSide[k++] = 0f;
                vSide[k++] = l;
            }
        }

        vTopBuf = ByteBuffer.allocateDirect(vTop.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vTopBuf.put(vTop);
        vTopBuf.position(0);

        vBottomBuf = ByteBuffer.allocateDirect(vBottom.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vBottomBuf.put(vBottom);
        vBottomBuf.position(0);

        vSideBuf = ByteBuffer.allocateDirect(vSide.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        vSideBuf.put(vSide);
        vSideBuf.position(0);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);


        //上面
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vTopBuf);
        gl.glNormal3f(0, 0, -1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, NUM_VERTICES);

        //下面
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBottomBuf);
        gl.glNormal3f(0, 0, 1);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, NUM_VERTICES);

        //側面
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vSideBuf);
        for(int i = 0, k = 0; i < NUM_VERTICES * 2; i += 2) {
            gl.glNormal3f(nVSide[k++], nVSide[k++] , nVSide[k++]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, i, 4);
        }
    }


    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getZ() {
        return z;
    }
}
