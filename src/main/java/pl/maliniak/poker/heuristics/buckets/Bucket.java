package pl.maliniak.poker.heuristics.buckets;

import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Reprezentacja grup rak, jakie moze posiadac przeciwnik.
 * @author meliniak
 */
public abstract class Bucket {
    /**
     * Wiadro jest rozne dla roznych streetow.
     */
    protected StreetName street;

    protected Bucket(){
    }
    
    /**
     * @return Karty na rece, ktore kwalfikuja sie do tego bucketu.
     */
    public abstract HoleCardsSet getHoleCardSet(TableCards tc);


    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
