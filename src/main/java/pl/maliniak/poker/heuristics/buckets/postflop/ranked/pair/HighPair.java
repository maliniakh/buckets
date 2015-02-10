package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Card;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 *
 * @author meliniak
 */
public class HighPair extends Pair1Hcu {

    public HighPair() {
        super();
    }
    
    //    // <editor-fold defaultstate="collapsed" desc="getHoleCardSet(...) stare">
//    @Override
//    public HoleCardsSet getHoleCardSet(final TableCards tc) {
//        HoleCardsSet hcs = new HoleCardsSet();
//
//        List<Cardinal> cardinalsSorted = tc.getCardinalsSorted(true);
//
//        // spodziewana uklad
//        // uklady rozne od tego beda odrzucane z wynikowego zbioru
//        RankEnum expectedRank;
//        int cardinalDiff = tc.size() - tc.getCardinalsSorted(true).size();
//        if(cardinalDiff == 0) {
//            expectedRank = RankEnum.PAIR;
//        } else if(cardinalDiff == 1) {
//            expectedRank = RankEnum.TWO_PAIRS;
//        } else if(cardinalDiff == 2) {
//            // w takim przypadku na boardzie mozemy miec dwie pary albo tripsa
//            // trzeba roztrzygnac
//            // ktorakolwiek z cardinali ma liczebnosc 3
//            boolean anyCardinal3Count = Iterables.any(cardinalsSorted, new Predicate<Cardinal>() {
//                @Override
//                public boolean apply(Cardinal input) {
//                    int cardinalCount = tc.getCardinalCount(input);
//                    if(cardinalCount == 3) {
//                        return true;
//                    } else {
//                        return false;
//                    }
//                }
//            });
//
//            if(anyCardinal3Count == false) {
//                expectedRank = RankEnum.TWO_PAIRS;
//            } else {
//                expectedRank = RankEnum.FULL_HOUSE;
//            }
//        } else {
//            throw new IllegalStateException();
//        }
//
//        // iterujemy po liscie cardinali, zaczynajac od najwiekszego
//        // jezeli jest wiecej niz jedna karta o takim cardinalu bierzemy nastepna
//        Cardinal cardinal = null;
//        for(int i = cardinalsSorted.size() - 1; i >= 0; i--) {
//            cardinal = cardinalsSorted.get(i);
//
//            int cardinalCount = tc.getCardinalCount(cardinal);
//            if(cardinalCount == 1) {
//                break;
//            }
//        }
//
//        // wyegenrowanie zbioru kart
//        // czesc HC zostanie usunieta z wynikowego zbioru jezeli nie bedzie
//        // odpowiadac spodziewanemu rankowi
//        HoleCardsSet cardinalHcs = CardsHelper.generate1CardinalHoleCardsSet(cardinal, tc);
//        hcs.addAll(cardinalHcs);
//
//        // iterejumey po wygenerowanym secie kart
//        for(HoleCards hc : cardinalHcs) {
//            HoleWTableCards hctc = new HoleWTableCards(hc, tc);
//            Rank rank = hctc.getRank();
//
//            if(rank.getPokerRank() != expectedRank) {
//                hcs.remove(hc);
//            }
//        }
//
//        if(hcs.size() == 0) {
//            throw new IllegalStateException();
//        }
//
//        return hcs;
//    }// </editor-fold>

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        Card highestCard = tc.getHighestCard();

        // jezeli ktoras z kart na rece jest rowna najwyzszej karcie na stole to
        // mamy trafienie
        if(highestCard.compareTo(hc.get(0)) == 0) {
            return true;
        }

        if(highestCard.compareTo(hc.get(1)) == 0) {
            return true;
        }

        return false;
    }

    public static void main(String[] args) {
        HighPair hp = new HighPair();

        HoleCardsSet hcs = hp.getHoleCardSet(new TableCards("2c2d3c7h"));

        System.out.println(Joiner.on("\n").join(hcs));
        System.out.println(hcs.size());
    }
}
