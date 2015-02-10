package pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw;

import com.google.common.base.Joiner;
import java.util.Set;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.Suit2NumberMap;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.cards.CardsHelper;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;

/**
 *
 * @author meliniak
 */
public class FlushDraw extends DrawBucket {

    public FlushDraw(int holeCardsUsed) {
        super(holeCardsUsed);
    }

    public FlushDraw() {
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        HoleCardsSet hcs = new HoleCardsSet();

        boolean flushDrawPossible = tc.isFlushDrawPossible();
        if(flushDrawPossible == false) {
            return hcs;
        }

        Suit2NumberMap suit2NumberMap = tc.getSuit2NumberMap();
        Set<Suit> maxSuitSet = suit2NumberMap.getMaxSuitSet();

        for(Suit suit : maxSuitSet) {
            Integer suitCount = suit2NumberMap.get(suit);

            // dwa karty tego samego koloru na stole
            if(suitCount == 2) {
                hcs.addAll(CardsHelper.generateSuitedHoleCardsSet(suit, tc));
            } else if(suitCount == 3) {                                         // 3 karty tego samego koloru na stole
                hcs.addAll(CardsHelper.generateXSuitHoleCardsSet(suit, tc));
            }
        }
        
        return hcs;
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        HoleCardsSet hcs = this.getHoleCardSet(tc);

        if(hcs.contains(hc)) {
            return true;
        } else {
            return false;
        }
    }



    public static void main(String[] args) {
        TableCards tc = new TableCards("Tc2cAsKs");
//        TableCards tc = new TableCards("Tc2cAcKh");
        FlushDraw flushDraw = new FlushDraw(2);

        long time1 = System.currentTimeMillis();
        HoleCardsSet holeCardSet = flushDraw.getHoleCardSet(tc);
        long time2 = System.currentTimeMillis();

        String join = Joiner.on("\n").join(holeCardSet);
        System.out.println(join);

        boolean belongsTo = flushDraw.belongsTo(tc, new HoleCards("4cKc"));
        System.out.println("belongsTo = " + belongsTo);
        
        belongsTo = flushDraw.belongsTo(tc, new HoleCards("Kc4c"));
        System.out.println("belongsTo = " + belongsTo);

        System.out.println(time2 - time1);
    }
}
