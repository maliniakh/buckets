package pl.maliniak.poker.cardsets;

import pl.maliniak.cards.Suit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * colors mapped to number of occurences.
 * @author meliniak
 */
public class Suit2NumberMap extends HashMap<Suit, Integer> {
    public static final long serialVersionUID = 341L;
    
    /**
     * @return color with highest occurences.
     */
    public int getMaxSuitNumber() {
        int maxNumber = 0;
        
        Set<Suit> keySet = this.keySet();
        int number;
        for(Suit suit : keySet) {
            number = this.get(suit);
            
            maxNumber = Math.max(number, maxNumber);
        }
        
        if(maxNumber == 0) {
            throw new IllegalStateException();
        }
        
        return maxNumber;
    }
    
    /**
     * @return
     */
    public Set<Suit> getMaxSuitSet() {
        Set<Suit> suitSet = new HashSet<>();
        int maxNumber = this.getMaxSuitNumber();
        
        Set<Suit> keySet = this.keySet();
        int number;
        for(Suit suit : keySet) {
            number = this.get(suit);
            
            if(number == maxNumber) {
                suitSet.add(suit);
            }
        }
        
        if(suitSet.size() == 0) {
            throw new IllegalStateException("Cos nieteges, zbior kolorow musi miec jakies wpisy");
        }
        
        return suitSet;
    }
    
    /**
     * zwraca ilosc wystepien koloru, ktory wystepuje najmniejsza ilosc razy.
     * @return
     */
    public int getMinSuitNumber() {
        int minNumber = Integer.MAX_VALUE;
        
        Set<Suit> keySet = this.keySet();
        int number;
        for(Suit suit : keySet) {
            number = this.get(suit);
            
            minNumber = Math.min(number, minNumber);
        }
        
        return minNumber;
    }
    
    /**
     * @return set zawierajacy kolory, ktore wystepuja najmniejsza ilosc razy.
     */
    public Set<Suit> getMinSuitSet() {
        Set<Suit> suitSet = new HashSet<Suit>();
        int minNumber = this.getMinSuitNumber();
        
        Set<Suit> keySet = this.keySet();
        int number;
        for(Suit suit : keySet) {
            number = this.get(suit);
            
            if(number == minNumber) {
                suitSet.add(suit);
            }
        }
        
        return suitSet;
    }
}
