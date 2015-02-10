package pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * Najwyzsza karta, czyli np ace high, a jak jest as ma stole, to king high itp.
 * @author meliniak
 */
public class HighestCard extends HighCardBucket {

    public HighestCard() {
        super();
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        Card highestCard = hc.getHighestCard();

        // sprawdzamy po kolei jaka mamy najwyzsza karte na stole
        if(tc.contains(Cardinal.ACE) == false) {
            if(highestCard.getCardinal() == Cardinal.ACE) {
                return true;
            } else {
                return false;
            }
        }

        if(tc.contains(Cardinal.KING) == false) {
            if(highestCard.getCardinal() == Cardinal.KING) {
                return true;
            } else {
                return false;
            }
        }

        if(tc.contains(Cardinal.QUEEN) == false) {
            if(highestCard.getCardinal() == Cardinal.QUEEN) {
                return true;
            } else {
                return false;
            }
        }

        if(tc.contains(Cardinal.JACK) == false) {
            if(highestCard.getCardinal() == Cardinal.JACK) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        HighestCard hcb = new HighestCard();
        
        TableCards tc = new TableCards("2c3c4h3h4c");
        long nanoTime1 = System.currentTimeMillis();
        HoleCardsSet hcs = hcb.getHoleCardSet(tc);
        long nanoTime2 = System.currentTimeMillis();
        long diff = nanoTime2 - nanoTime1;

        System.out.println("diff = " + diff);
        
        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
    }
}
