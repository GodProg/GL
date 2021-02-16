/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package CoreUtils;

/**
 * Custom Vector2 for our game
 */
public final class FVector2 {

    //This position is relative to the World
    public float XPos;
    public float YPos;

    //This represents the position of the world
    public static float AbsoluteX;
    public static float AbsoluteY;

    public FVector2() {
        XPos = 0;
        YPos = 0;
    }

    public FVector2(float XPos, float YPos) {
        this.XPos = XPos;
        this.YPos = YPos;
    }


//    /**
//     * add 2 vectors
//     * @param vect1
//     * @param vect2
//     * @return
//     * @Deprecated
//     */
//    public static FVector2 AddRelative(FVector2 vect1, FVector2 vect2) {
//        return new FVector2(vect1.XPos + vect2.XPos, vect1.YPos + vect2.YPos);
//    }

    public void SetXPos(float XPos) {
        this.XPos = XPos;
    }

    public void SetYPos(float YPos) {
        this.YPos = YPos;
    }

    public void Add(float dx, float dy) {
        XPos += dx;
        YPos += dy;
    }

    public static void SetAbsolutePosition(float x, float y) {
        AbsoluteX = x;
        AbsoluteY = y;

    }

    /**
     * Projects our world position onto screen coordinates
     *
     * @return the screen coordinates
     */
    public FVector2 GetScreenPosition() {
        return new FVector2(XPos - AbsoluteX, YPos - AbsoluteY);
    }


    @Override
    public String toString() {
        return "FVector2{" +
                "XPos=" + XPos +
                ", YPos=" + YPos +
                '}';
    }
}
