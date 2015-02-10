package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Czyli pocket pair.
 * @author meliniak
 */
public class Pair2Hcu extends Pair {

    public Pair2Hcu() {
        super(2);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        if(hc.get(0).getCardinal() == hc.get(1).getCardinal()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        TableCards tc = new TableCards("2c3c5h6h");
        Pair2Hcu pair2Hcu = new Pair2Hcu();

        HoleCardsSet hcs = pair2Hcu.getHoleCardSet(tc);
        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
    }
}
