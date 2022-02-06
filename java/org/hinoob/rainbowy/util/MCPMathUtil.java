package org.hinoob.rainbowy.util;

public class MCPMathUtil {

    private static final float[] SIN_TABLE = new float[65536];

    static {
        for (int i = 0; i < SIN_TABLE.length; ++i) {
            SIN_TABLE[i] = (float) StrictMath.sin((double) i * 3.141592653589793 * 2.0 / 65536.0);
        }
    }

    public static float sin(float p_76126_0_)
    {
        return SIN_TABLE[(int)(p_76126_0_ * 10430.378F) & 65535];
    }

    public static float cos(float value)
    {
        return SIN_TABLE[(int)(value * 10430.378F + 16384.0F) & 65535];
    }
}
