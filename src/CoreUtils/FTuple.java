/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package CoreUtils;

/**
 * Simple Class To store a tuple
 *
 * @param <S> the first tuple type
 * @param <T> the second tuple type
 */
public class FTuple<S, T> {

    public final S FirstPair;
    public final T SecondPair;

    public FTuple() {
        this.FirstPair = null;
        this.SecondPair = null;
    }

    public FTuple(S FirstPair, T SecondPair) {
        this.FirstPair = FirstPair;
        this.SecondPair = SecondPair;
    }

    @Override
    public String toString() {
        return "FTuple{" +
                "FirstPair=" + FirstPair +
                ", SecondPair=" + SecondPair +
                '}';
    }
}
