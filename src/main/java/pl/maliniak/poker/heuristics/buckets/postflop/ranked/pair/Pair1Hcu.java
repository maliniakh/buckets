package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 *
 * @author meliniak
 */
public class Pair1Hcu extends Pair {

    public Pair1Hcu() {
        super(1);
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        if(hc.get(0).getCardinal() != hc.get(1).getCardinal()) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        TableCards tc = new TableCards("Ac2h4c");
        Pair p = new Pair1Hcu();

        HoleCardsSet hcs = p.getHoleCardSet(tc);

        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
        System.out.println(hcs.size());
    }
    // <editor-fold defaultstate="collapsed" desc="delegateBucket(...)">
//    @Override
//    public PostflopBucket delegateBucket(TableCards tableCards, HoleCards holeCards, StreetName street) {
//        // mapowanie baseNameHcu2postflopBucketTmp.put(new GetterBaseNameHcuPK("2pair", 1), new Pair1Hcu());
//        // w PFBSetProviderDBImpl moze byc niewlasciwe z powodu buga w PT3
//        // 3 5 @ 2 4 3 5 daje 2 pairs, 1 hcu, a powinno byc 2 hcu, dlatego sprawdzamy recznie
//
//        // moze byc 2 pair 2 hcu
//        HoleWTableCards hctc = new HoleWTableCards(holeCards, tableCards);
//        Rank rank = hctc.getRank();
//
//        if(rank.getPokerRank() == RankEnum.TWO_PAIRS) {
//            // w takim wypadku musimy miec 2 hcu
//            Cardinal c1 = holeCards.get(0).getCardinal();
//            Cardinal c2 = holeCards.get(1).getCardinal();
//
//            // warunek na 2 hcu
//            if(tableCards.contains(c1) && tableCards.contains(c2)) {
//                return new TwoPairs(tableCards, holeCards, street);
//            } else {
//                throw new IllegalStateException("Powinno byc 2 hcu, jezeli go nie ma to to dokonalismy zlego zalozenia i trzeba je przemyslec");
//            }
//        } else {
//            return super.delegateBucket(tableCards, holeCards, street);
//        }
//    }// </editor-fold>
}
