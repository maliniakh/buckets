package pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw;

import java.util.Set;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.street.StreetName;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.Suit2NumberMap;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.FlopBucketOnly;

/**
 *
 * @author meliniak
 */
@Deprecated
public class BackdoorFlushDraw2Hcu extends BackdoorFlushDraw
        implements FlopBucketOnly {

    public BackdoorFlushDraw2Hcu() {
        super(2);
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean belongsTo(TableCards tableCards, HoleCards holeCards) {
        Suit2NumberMap suit2NumberMap = tableCards.getSuit2NumberMap();
        Set<Suit> keySet = suit2NumberMap.keySet();
        for(Suit suit : keySet) {
            int suitCount = suit2NumberMap.get(suit);

            if(suitCount == 1) {
                // jezeli dany kolor wystepuje 2 razy to na reku ten kolor musi wystepowac
                // 1 raz zeby byla mowa o BackdoorFlushDraw1Hcu
                int hcSuitCount = holeCards.getSuit2NumberMap().get(suit);
                if(hcSuitCount == 2) {
                    return true;
                }
            }
        }

        return false;
    }
}
