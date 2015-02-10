package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.cards.CardsHelper;

/**
 * Parka na reku, pomiedzy najwyzsza a druga najwyzsza karta na stole, np
 * TT @ J54.
 * @author meliniak
 */
public class MiddlePair extends Pair2Hcu {

    public MiddlePair() {
        super();
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
            assert holeCardsUsed != 2;            
            return false;
        }

        Cardinal c;
        c = hc.get(0).getCardinal();

        // jezeli jedna karta na stole jest wyzsza wtedy klasyfikujemy do tego bucketu
        int cardsHigher = CardsHelper.cardsHigher(c, tc);
        if(cardsHigher == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Pair mp = new MiddlePair();

        HoleCardsSet hcs = mp.getHoleCardSet(new TableCards("2c2d3c7h"));

        System.out.println(Joiner.on("\n").join(hcs));
        System.out.println(hcs.size());
    }
}
