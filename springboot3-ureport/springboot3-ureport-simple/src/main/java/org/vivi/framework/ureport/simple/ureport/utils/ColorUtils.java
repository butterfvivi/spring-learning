package org.vivi.framework.ureport.simple.ureport.utils;

/**
 * @author WangJX
 * @since 2024/6/12
 */
public class ColorUtils {


    /**
     * rgb颜色转16进制颜色
     * @param rgb rgb颜色
     * @return 16进制颜色
     */
    public static String toHex(String[] rgb) {
        StringBuilder sb = new StringBuilder();
        String R = Integer.toHexString(Integer.parseInt(rgb[0].trim()));
        String G = Integer.toHexString(Integer.parseInt(rgb[1].trim()));
        String B = Integer.toHexString(Integer.parseInt(rgb[2].trim()));
        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;
        sb.append(R);
        sb.append(G);
        sb.append(B);
        return sb.toString();
    }
}
