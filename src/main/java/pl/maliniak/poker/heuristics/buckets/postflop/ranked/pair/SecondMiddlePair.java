package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.cards.CardsHelper;

/**
 * Parka na reku, pomiedzy druga najwyzsza a trzecia najwyzsza karta na stole, np
 * TT @ KJ54.
 * @author meliniak
 */
public class SecondMiddlePair extends Pair2Hcu {

    public SecondMiddlePair() {
        super();
    }
    
    @Override
    public RankEnum getRank() {
        return RankEnum.PAIR;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        Cardinal cardinal1 = hc.get(0).getCardinal();
        Cardinal cardinal2 = hc.get(1).getCardinal();
        
        // karty na rece musza byc takie same jezeli ma byc to parka
        if(cardinal1.equals(cardinal2) == false) {
//            assert holeCardsUsed != 2;
            return false;
        }

        Cardinal c;
        c = hc.get(0).getCardinal();

        // jezeli jedna karta na stole jest wyzsza wtedy klasyfikujemy do tego bucketu
        int cardsHigher = CardsHelper.cardsHigher(c, tc);
        if(cardsHigher == 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
