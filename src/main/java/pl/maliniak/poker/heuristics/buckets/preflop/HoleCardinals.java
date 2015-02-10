package pl.maliniak.poker.heuristics.buckets.preflop;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.cards.Cardinal;

/**
 * Karty na rece, ale bez okreslonych kolorow - do definiowania zakresow wiaderek.
 * @author maliniak
 */
public class HoleCardinals {
    private static final Logger logger = LoggerFactory.getLogger(HoleCardinals.class);

    private Cardinal c1;

    private Cardinal c2;

    public HoleCardinals(Cardinal c1, Cardinal c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public Cardinal getC1() {
        return c1;
    }

    public Cardinal getC2() {
        return c2;
    }

    @Override
    public String toString() {
        return c1.toString() + c2.toString();
    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        HoleCardinals other = (HoleCardinals) obj;

        if(c1 == other.c1 && c2 == other.c2) {
            return true;
        }

        if(c1 == other.c2 && c2 == other.c1) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.c1 != null ? this.c1.hashCode() : 0);
        hash = 79 * hash + (this.c2 != null ? this.c2.hashCode() : 0);
        return hash;
    }
}
