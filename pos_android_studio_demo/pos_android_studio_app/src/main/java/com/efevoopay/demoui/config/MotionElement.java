package com.cohetepay.demoui.config;

/***
 * Touch point information.
 *
 * @since 2018/06/15
 * @author king
 */
public class MotionElement {

    public float x;
    public float y;
    /**
     */
    public float pressure;
    /**
     * Is the drawing tool a finger or a pen (touch pen)
     */
    public int toolType;

    public MotionElement(float mx, float my, float mp, int ttype) {
        x = mx;
        y = my;
        pressure = mp;
        toolType = ttype;
    }


}
