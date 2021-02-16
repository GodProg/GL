/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package CoreUtils;

public class FQuadruplet<T, E, S, V> {

    public final T FirstValue;
    public final E SecondValue;
    public final S ThirdValue;
    public final V FourthValue;

    private FQuadruplet() {
        FirstValue = null;
        SecondValue = null;
        ThirdValue = null;
        FourthValue = null;
    }

    public FQuadruplet(T firstValue, E secondValue, S thirdValue, V fourthValue) {
        FirstValue = firstValue;
        SecondValue = secondValue;
        ThirdValue = thirdValue;
        FourthValue = fourthValue;
    }

    @Override
    public String toString() {
        return "FQuadruplet{" +
                "FirstValue=" + FirstValue +
                ", SecondValue=" + SecondValue +
                ", ThirdValue=" + ThirdValue +
                ", FourthValue=" + FourthValue +
                '}';
    }
}