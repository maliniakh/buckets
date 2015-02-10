package pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips;

import com.google.common.base.Joiner;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Trzy takie same karty.
 * @author meliniak
 */
public class Trips2Hcu extends Trips {
    
    public Trips2Hcu() {
        super(2);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        if(hc.isPocketPair()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        Trips2Hcu trips2Hcu = new Trips2Hcu();
        TableCards tc = new TableCards("3s6s7s8s");

        long time1 = System.currentTimeMillis();
        HoleCardsSet hcs = trips2Hcu.getHoleCardSet(tc);
        long time2 = System.currentTimeMillis();
        long diff = time2 - time1;


        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
        System.out.println("diff = " + diff);

        System.out.println(hcs.size());
    }
}
