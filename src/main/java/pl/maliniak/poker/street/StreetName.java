package pl.maliniak.poker.street;

import java.util.Arrays;

/**
 *
 * @author maliniak
 */
public enum StreetName {

    // NIE ZMIENIAC KOLEJNOSCI
    PREFLOP,
    FLOP,
    TURN,
    RIVER,
    NA;                                 // zle wyscrapowany street

    /**
     * @param streetStr Reprezentacja stringowa street'a
     * @return 
     */
    public static StreetName getStreet(String streetStr) {
        // TODO: zrobic fajniej

        if(streetStr.equals("preflop")) {
            return PREFLOP;
        } else if(streetStr.equals("flop")) {
            return FLOP;
        } else if(streetStr.equals("turn")) {
            return TURN;
        } else if(streetStr.equals("river")) {
            return RIVER;
        } else if(streetStr.equals("na")) {
            return NA;
        } else {                                      // nie mozna ustalic streetu
            throw new IllegalArgumentException();
        }
    }

    /**
     * @return Zwraca tablice id streetow, bez wartosci NA.
     */
    public static StreetName[] valuesNoNA() {
        StreetName[] values = StreetName.values();
        StreetName[] valuesNoNA;

        StreetName na = values[values.length - 1];
        if(na != NA) {
            throw new IllegalStateException("Usuwamy cos innego niz NA");
        }

        valuesNoNA = Arrays.copyOf(values, values.length - 1);

        return valuesNoNA;
    }

    /**
     * @return Zwraca tablice id streetow, bez wartosci NA i bez preflopa.
     */
    public static StreetName[] valuesNoNANoPreflop() {
        return new StreetName[]{FLOP, TURN, RIVER};
    }

    /**
     * @param street
     * @return Tablica streetow po kolei, "przycieta" do podanego streeta;
     * wlacznie, tj. lista zawiera podany street.
     */
    public static StreetName[] valuesTrimmed(StreetName street) {
        // :)
        switch(street) {
            case RIVER:
                return new StreetName[] {PREFLOP, FLOP, TURN, RIVER};
            case TURN:
                return new StreetName[] {PREFLOP, FLOP, TURN};
            case FLOP:
                return new StreetName[] {PREFLOP, FLOP};
            case PREFLOP:
                return new StreetName[] {PREFLOP};
            default:
                throw new IllegalArgumentException("WTF");
        }
    }

    /**
     * @return user-friendly reprezentacja streeta
     */
    public String getName() {
        // TODO: zrobic fajniej

        if(this.equals(PREFLOP)) {
            return "preflop";
        } else if(this.equals(FLOP)) {
            return "flop";
        } else if(this.equals(TURN)) {
            return "turn";
        } else if(this.equals(RIVER)) {
            return "river";
        } else if(this.equals(NA)) {
            return "na";
        } else {                                      // nie mozna ustalic streetu
            throw new IllegalArgumentException();
        }
    }
    
    /**
     * @return nastepny street po tym.
     */
    public StreetName getNextStreet() {
        if(this == PREFLOP) {
            return FLOP;
        } else if(this == FLOP ) {
            return TURN;
        } else if(this == TURN) {
            return RIVER;
        } else if(this == RIVER) {
            throw new IllegalStateException("nastepny street po riv?");
        } else {
            throw new IllegalStateException("wtf?");
        }
    }
    
    public static void main(String[] args) {
        StreetName[] valuesTrimmed = valuesTrimmed(TURN);
        for(StreetName streetName : valuesTrimmed) {
            System.out.println(streetName);
        }
    }
}
