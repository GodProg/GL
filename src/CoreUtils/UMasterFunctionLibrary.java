/**********************************
 * Copyright (c)  @Da Costa David *
 **********************************/

package CoreUtils;

/**
 * Soecial Function library
 */
public class UMasterFunctionLibrary {

    /**
     * Counts how many elements there are in an array of any kind before meeting a null value
     *
     * @param data the data to check
     * @param <T>  any non primitive data type
     * @return the number of valid consecutive objects
     */
    public static <T> int CountValidArrayElements(T[] data) {
        int count = 0;

        for (T CurrentData : data) {
            if (CurrentData != null) count++;
            else break;
        }
        return count;
    }

}
