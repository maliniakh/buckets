package pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw;

import java.util.List;
import java.util.Set;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.cards.Deck;
import pl.maliniak.poker.Rank;
import pl.maliniak.poker.RankEnum;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleWTableCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;

/**
 *
 * @author meliniak
 */
public class GutshotStraightDraw extends DrawBucket {

    public GutshotStraightDraw() {
        super(2);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        HoleWTableCards hctc = new HoleWTableCards(hc, tc);

        Rank rank = hctc.getRank();
        RankEnum pokerRank = rank.getPokerRank();

        // mamy wyzszy uklad -> false
        if(pokerRank == RankEnum.FLUSH ||
                pokerRank == RankEnum.FULL_HOUSE ||
                pokerRank == RankEnum.QUADS ||
                pokerRank == RankEnum.FLUSH) {
            return false;
        }

        Card c1 = hc.get(0);
        Card c2 = hc.get(1);
        Cardinal cr1 = c1.getCardinal();
        Cardinal cr2 = c2.getCardinal();

        // ile cardinali z HC znajduje sie wsrod TC
        int cardinalsContained = 0;

        if(tc.contains(cr1)) {
            cardinalsContained++;
        }
        if(tc.contains(cr2)) {
            cardinalsContained++;
        }

        // takie same cardinale z HC na TC, nie moze byc straightdrawa z HC
        if(cardinalsContained == 2) {
            return false;
        }

        // ile kart z talii daje strita, ta liczba musi byc <= 4 zeby
        int matchingCardsCount = 0;

        // iterujemy po kartach
        // dodajemy kazda i sprawdzamy czy tworzy ona strita
        List<Card> cards = Deck.getInstance().getCards();
        for(Card c : cards) {
            if(tc.contains(c)) {
                continue;
            }

            if(hc.contains(c)) {
                continue;
            }

            TableCards tcCloned = tc.newInstance();
            tcCloned.add(c);
            HoleWTableCards hctcCloned = new HoleWTableCards(hc, tcCloned);
            RankEnum pokerRankCloned = hctcCloned.getRank().getPokerRank();

            if(pokerRankCloned == RankEnum.STRAIGHT) {
                matchingCardsCount++;
            }
        }

        if(matchingCardsCount > 0 && matchingCardsCount <= 4) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        TableCards tc;
        HoleCards hc;
        boolean belongsTo;
        PostflopBucket gsd = new GutshotStraightDraw();

        tc = new TableCards("4c7c8c");
        hc = new HoleCards("6c3c");

        long time1 = System.currentTimeMillis();
        belongsTo = gsd.belongsTo(tc, hc);
        long time2 = System.currentTimeMillis();
        long diff = time2 - time1;

        System.out.println("diff = " + diff);
        
        System.out.println(belongsTo);
    }
}
