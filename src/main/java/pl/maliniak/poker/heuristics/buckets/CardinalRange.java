package pl.maliniak.poker.heuristics.buckets;

import pl.maliniak.cards.Cardinal;

/**
 * Definiuje zakres wartosci kart, do definiowania wiaderek.
 * @author meliniak
 */
public class CardinalRange {
    /**
     * Dolna granica.
     */
    private Cardinal lowBound;
    /**
     * Gorna granica.
     */
    private Cardinal highBound;
    
    public CardinalRange(Cardinal highBound, Cardinal lowBound) {
        if(lowBound.ordinal() > highBound.ordinal()) {
            throw new IllegalStateException("Nieprawidlowa relacja wiekszosci w zakresie kart (" + lowBound + " > " + highBound + ")");
        }
        
        this.lowBound = lowBound;
        this.highBound = highBound;
    }    
    
    /**
     * Zakres 1kartowy.
     * @param cardinal Wartość karty. 
     */
    public CardinalRange(Cardinal cardinal) {
        this.lowBound = cardinal;
        this.highBound = cardinal;
    }
    
    /**
     * Sprawdza, czy wartosc karty lezy w zakresie.
     * @param cardinal Karta ktora nas interesuje.
     * @return True, jezeli lezy, tzn >= && <=.
     */
    public boolean belongsToRange(Cardinal cardinal) {
        if(cardinal.ordinal() >= this.lowBound.ordinal() && cardinal.ordinal() <= this.highBound.ordinal()) {
            return true;
        } else {
            return false;
        }
    }

    public Cardinal getHighBound() {
        return highBound;
    }

    public Cardinal getLowBound() {
        return lowBound;
    }

    @Override
    public String toString() {
        String string;
        if (this.lowBound != this.highBound) {
            string = "(" + this.lowBound + "-" + this.highBound + ")";
        } else {
            string = "(" + this.lowBound + ")";
        }
        return string;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final CardinalRange other = (CardinalRange) obj;
        if(this.lowBound != other.lowBound && (this.lowBound == null || !this.lowBound.equals(other.lowBound))) {
            return false;
        }
        if(this.highBound != other.highBound && (this.highBound == null || !this.highBound.equals(other.highBound))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.lowBound != null ? this.lowBound.hashCode() : 0);
        hash = 17 * hash + (this.highBound != null ? this.highBound.hashCode() : 0);
        return hash;
    }
}
