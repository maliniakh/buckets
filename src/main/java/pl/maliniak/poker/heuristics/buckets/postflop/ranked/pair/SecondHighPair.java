package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.cards.CardsHelper;

/**
 * Para utworzona z druga najwyzsza karta na stole.
 * @author meliniak
 */
public class SecondHighPair extends Pair1Hcu {

    public SecondHighPair() {
        super();
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        int cardsHigher;
        Cardinal c = null;

        Cardinal c1, c2;
        c1 = hc.getCardinalIndex(0);
        c2 = hc.getCardinalIndex(1);

        // sprawdzenie osobliwego przypadku, np. Q9@7779Q, wtedy mamy HighPair
        if(tc.contains(c1) && tc.contains(c2)) {
            return false;
        }

        // sprawdzamy ktory cardinal tworzy parke
        c = hc.getCardinalIndex(0);
        if(tc.contains(c)) {
            cardsHigher = CardsHelper.cardsHigher(c, tc);

            if(cardsHigher == 1) {
                return true;
            } else {
                return false;
            }
        }
        
        // to samo dla drugiej karty z reki
        c = hc.getCardinalIndex(1);
        if(tc.contains(c)) {
            cardsHigher = CardsHelper.cardsHigher(c, tc);

            if(cardsHigher == 1) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        Pair mp = new SecondHighPair();

        HoleCardsSet hcs = mp.getHoleCardSet(new TableCards("2c2d3c7h"));

        System.out.println(Joiner.on("\n").join(hcs));
        System.out.println(hcs.size());
    }
}
