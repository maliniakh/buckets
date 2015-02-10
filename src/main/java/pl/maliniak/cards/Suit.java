package pl.maliniak.cards;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Kolory kart.
 * @author maliniak
 */
public enum Suit {

    HEARTS("h"),
    SPADES("s"),
    CLUBS("c"),
    DIAMONDS("d"),
    X("x");

    private String string;
    /**
     * string form to color map.
     */
    private final static Map<String, Suit> str2SuitMap = new HashMap<String, Suit>();

    static {
        // inicjalizacja mapy nazw
        for (Suit suit : Suit.values()) {
            str2SuitMap.put(suit.string, suit);
        }
    }

    private Suit(String string) {
        this.string = string;
    }

    /**
     * @param suitStr Reprezentacja koloru, przyslana przez body.
     * @return Odpowiedni enum.
     */
    public static Suit getSuitByOrdinal(String suitStr) {
        int suitInt = Integer.parseInt(suitStr);

        return Suit.values()[suitInt];
    }

    public static Suit getSuit(String suitStr) {
        Suit suit = str2SuitMap.get(suitStr);

        if (suit == null) {
            throw new IllegalArgumentException();
        }

        return suit;
    }

    /**
     * @return Wartosci kolorow, bez genericsowego "X".
     */
    public static Suit[] valuesNoX() {
        Suit[] values = Suit.values();
        Suit[] valuesNoX;

        Suit x = values[values.length - 1];
        if (x != X) {
            throw new IllegalStateException("Usuwamy cos innego niz X");
        }

        valuesNoX = Arrays.copyOf(values, values.length - 1);

        return valuesNoX;
    }

    @Override
    public String toString() {
        if (this == HEARTS) {
            return "♥";
        } else if (this == DIAMONDS) {
            return "◆";
        } else if (this == SPADES) {
            return "♠";
        } else if (this == CLUBS) {
            return "♣";
        } else if (this == X) {
            return "X";
        } else {
            throw new IllegalStateException("");
        }
    }

    public String toStringLetter() {
        if (this == HEARTS) {
            return "h";
        } else if (this == DIAMONDS) {
            return "d";
        } else if (this == SPADES) {
            return "s";
        } else {
            return "c";
        }
    }
}
