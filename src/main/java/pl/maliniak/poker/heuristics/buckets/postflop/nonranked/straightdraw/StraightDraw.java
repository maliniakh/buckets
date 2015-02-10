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
public class StraightDraw extends DrawBucket {

    public StraightDraw() {
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
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        TableCards tc;
        HoleCards hc;
        boolean belongsTo;
        PostflopBucket sd = new StraightDraw();

        tc = new TableCards("4c5c3s");
        hc = new HoleCards("6c3h");
        belongsTo = sd.belongsTo(tc, hc);
        System.out.println(belongsTo);
    }
}
