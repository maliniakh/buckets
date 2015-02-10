package pl.maliniak.poker.heuristics.buckets.postflop.ranked.straight;

import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Trzy takie same karty.
 * @author meliniak
 */
public class Straight2Hcu extends Straight {
    
    public Straight2Hcu() {
        super(2);
    }

    @Override
    public RankEnum getRank() {
        return RankEnum.STRAIGHT;
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /*@Override
    public PostflopBucket delegateBucket(TableCards tableCards, HoleCards holeCards, StreetName street,int holeCardsUsed) {
    return new Straight1Hcu(street, tableCards, holeCardsUsed);
    }*/
}
