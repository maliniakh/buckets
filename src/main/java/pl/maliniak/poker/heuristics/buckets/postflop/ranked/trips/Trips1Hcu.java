package pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips;

import com.google.common.base.Joiner;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Trzy takie same karty.
 * @author meliniak
 */
public class Trips1Hcu extends Trips {
    
    public Trips1Hcu() {
        super(1);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        if(hc.isPocketPair()) {
            return false;
        } else {
            return true;
        }
    }

    public static void main(String[] args) {
        Trips1Hcu trips1Hcu = new Trips1Hcu();
        TableCards tc = new TableCards("3s3h5s6h7c");

        long time1 = System.currentTimeMillis();
        HoleCardsSet hcs = trips1Hcu.getHoleCardSet(tc);
        long time2 = System.currentTimeMillis();
        long diff = time2 - time1;

        
        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
        System.out.println("diff = " + diff);

        System.out.println(hcs.size());
    }
}
