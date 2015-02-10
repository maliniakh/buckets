package pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair;

import com.google.common.base.Joiner;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 * parka z jedna karta z boardu, taka co nie stanowi highpair ani secondhighpair
 * @author meliniak
 */
public class LowPair extends Pair1Hcu {

    private HighPair hp = new HighPair();
    private SecondHighPair shp = new SecondHighPair();
    
    public LowPair() {
        super();
    }

    @Override
    public boolean belongsTo(TableCards tc, HoleCards hc) {
        if(super.belongsTo(tc, hc) == false) {
            return false;
        }

        Card lowestCard = tc.getLowestCard();

        // taki szczegolny przypadek: LowPair i HighPair dla 4s Qs@7c 4h Qc 7s 7h
        Cardinal c1, c2;
        c1 = hc.getCardinalIndex(0);
        c2 = hc.getCardinalIndex(1);
        if(tc.contains(c1) && tc.contains(c2)) {
            return false;
        }

        // jak nie nalezy do hp ani shp to nalezy wlasnie do tego bucketa
        // RFCTR: nie powinno byc zalezne od innych bucketow?
        if(hp.belongsTo(tc, hc) == false && shp.belongsTo(tc, hc) == false) {
            return true;
        } else {
            return false;
        }
        
        
        // jezeli ktoras z kart na rece jest rowna najnizszej karcie na stole to
        // mamy trafienie
//        if(lowestCard.compareTo(hc.get(0)) == 0) {
//            return true;
//        }
//
//        if(lowestCard.compareTo(hc.get(1)) == 0) {
//            return true;
//        }

//        return false;
    }

    public static void main(String[] args) {
        TableCards tc = new TableCards("Ac2h4c");
        Pair p = new LowPair();

        HoleCardsSet hcs = p.getHoleCardSet(tc);

        String join = Joiner.on("\n").join(hcs);
        System.out.println(join);
        System.out.println(hcs.size());
    }
}
