/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package CoreUtils;

public class FTriplet<T, E, S> {

    public final T FirstValue;
    public final E SecondValue;
    public final S ThirdValue;

    private FTriplet() {
        FirstValue = null;
        SecondValue = null;
        ThirdValue = null;
    }

    public FTriplet(T firstValue, E secondValue, S thirdValue) {
        FirstValue = firstValue;
        SecondValue = secondValue;
        ThirdValue = thirdValue;
    }

    @Override
    public String toString() {
        return "FTriplet{" +
                "FirstValue=" + FirstValue +
                ", SecondValue=" + SecondValue +
                ", ThirdValue=" + ThirdValue +
                '}';
    }
}
