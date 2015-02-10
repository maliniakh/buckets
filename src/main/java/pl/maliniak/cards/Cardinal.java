package pl.maliniak.cards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author maliniak
 */
public enum Cardinal implements Comparable<Cardinal> {

    TWO(1),
    THREE(2),
    FOUR(2 << 1),
    FIVE(2 << 2),
    SIX(2 << 3),
    SEVEN(2 << 4),
    EIGHT(2 << 5),
    NINE(2 << 6),
    TEN(2 << 7),
    JACK(2 << 8),
    QUEEN(2 << 9),
    KING(2 << 10),
    ACE(2 << 11),
    X(null);
    /**
     * for computations wheter there are straights, straight draws and so on.
     */
    private Integer bitValue;

    private static final Map<String, Cardinal> str2CardinalMap = new HashMap<String, Cardinal>();

    static {
        str2CardinalMap.put("2", TWO);
        str2CardinalMap.put("3", THREE);
        str2CardinalMap.put("4", FOUR);
        str2CardinalMap.put("5", FIVE);
        str2CardinalMap.put("6", SIX);
        str2CardinalMap.put("7", SEVEN);
        str2CardinalMap.put("8", EIGHT);
        str2CardinalMap.put("9", NINE);
        str2CardinalMap.put("T", TEN);
        str2CardinalMap.put("J", JACK);
        str2CardinalMap.put("Q", QUEEN);
        str2CardinalMap.put("K", KING);
        str2CardinalMap.put("A", ACE);
    }

    private Cardinal(Integer bitValue) {
        this.bitValue = bitValue;
    }

    public static Cardinal getCardinalByOrdinal(int ordinal) {
        // zmniejszamy o 2, tzn 2 -> 0 czyli TWO
        return Cardinal.values()[ordinal - 2];
    }

    /**
     * @param str string form (2-9 or T-A)
     * @return
     */
    public static Cardinal getCardinal(String str) {
        Cardinal cardinal = str2CardinalMap.get(str);

        if (cardinal == null) {
            throw new IllegalArgumentException();
        }

        return cardinal;
    }

    /**
     * @param value 2 to 14 value which corresponds to 2-A cards
     * @return
     */
    public static Cardinal getCardinal(int value) {
        Cardinal[] valuesNoX = valuesNoX();

        for (Cardinal c : valuesNoX) {
            if (c.ordinal() + 2 == value) {
                return c;
            }
        }

        throw new IllegalArgumentException("not from range [2, 14].");
    }

    /**
     * array of enums, but with no generic X value.
     * @return
     */
    public static Cardinal[] valuesNoX() {
        Cardinal[] values = Cardinal.values();
        Cardinal[] valuesNoX;

        Cardinal x = values[values.length - 1];
        if (x != X) {
            throw new IllegalStateException();
        }

        valuesNoX = Arrays.copyOf(values, values.length - 1);

        return valuesNoX;
    }

    /**
     * A,2...A array.
     */
    public static Cardinal[] valuesStraight() {
        Cardinal[] valuesStraight = new Cardinal[13 + 1];
        Cardinal[] valuesNoX = Cardinal.valuesNoX();

        List<Cardinal> valuesStraightList = Arrays.asList(valuesNoX);
        List<Cardinal> valuesStraightVector = new ArrayList<Cardinal>(valuesStraightList);
        valuesStraightVector.add(0, Cardinal.ACE);                                // add ace at start

        valuesStraightVector.toArray(valuesStraight);

        return valuesStraight;
    }

    @Override
    public String toString() {
        if (this.ordinal() >= TWO.ordinal() && this.ordinal() <= NINE.ordinal()) {
            return (new Integer(this.ordinal() + 2).toString());                // digit
        } else {
            return (this.name().substring(0, 1));                               // first letter
        }
    }
}
