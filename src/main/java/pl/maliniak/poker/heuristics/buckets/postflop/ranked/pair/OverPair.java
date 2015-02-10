package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.cards.CardsHelper;

/**
 *
 * @author meliniak
 */
public class OverPair extends Pair2Hcu {
    
    public OverPair() {
        super();
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        // czy mamy parke na rece
        if(hc.get(0).compareTo(hc.get(1)) != 0) {
            return false;
        }

        // jezeli mamy tyle samo: nizszych kart od karty na rece i kart na stole,
        // to znaczy ze mamy doczynienia z overpair
        Cardinal cardinal = hc.get(0).getCardinal();
        int cardsLower = CardsHelper.cardsLower(cardinal, tc);
        if(cardsLower == tc.size()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Pair op = new OverPair();

        long time1 = System.currentTimeMillis();
        HoleCardsSet hcs = op.getHoleCardSet(new TableCards("2c2d3c7h"));
        long time2 = System.currentTimeMillis();

        System.out.println(time2 - time1);

        System.out.println(Joiner.on("\n").join(hcs));
        System.out.println(hcs.size());
    }
}
