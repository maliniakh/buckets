package pl.maliniak.poker.heuristics.buckets.preflop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import pl.maliniak.cards.Cardinal;
import static pl.maliniak.cards.Cardinal.*;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.heuristics.buckets.CardinalRange;
import pl.maliniak.poker.heuristics.buckets.preflop.connectors.ConnectorBucket;
import pl.maliniak.poker.heuristics.buckets.preflop.pairs.PairBucket;
import pl.maliniak.poker.heuristics.buckets.preflop.ranges.RangeBucket;

/**
 * Fabryka preflopowych wiaderek.
 * @author meliniak
 */
public class PreflopBucketProvider {

    /**
     * Instancja singletonowa.
     */
    static private PreflopBucketProvider instance = null;

    private Set<PreflopBucket> preflopBucketSet = new HashSet<PreflopBucket>();

    /**
     * Mapsuje dwie karty (figury) i to czy sa suited do konkretnych bucketow.
     */
    private Map<BucketMapKey, PreflopBucket> key2bucketMap = new HashMap();
    /**
     * stringowa reprezentacja bucketow do odpowiednich instancji.
     */
    private Map<String, PreflopBucket> string2prbMap = new HashMap<String, PreflopBucket>();
    
    static public PreflopBucketProvider getInstance() {
        if(instance == null) {
            instance = new PreflopBucketProvider();
        }

        return instance;
    }

    private PreflopBucketProvider() {
        this.registerBuckets();
        this.initBucketMap();
        this.initString2prbMap();
    }

    public PreflopBucket getBucket(HoleCards hc) {
        boolean suited = hc.isSuited();

        BucketMapKey key = new BucketMapKey(suited, new HoleCardinals(hc.get(0).getCardinal(), hc.get(1).getCardinal()));
        PreflopBucket pfb = key2bucketMap.get(key);

        return pfb;
    }

    /**
     * Inicjalizuje zbior wiaderek.
     */
    private void registerBuckets() {
        preflopBucketSet.add(new PairBucket(new CardinalRange(Cardinal.ACE, Cardinal.KING)));
        preflopBucketSet.add(new PairBucket(new CardinalRange(Cardinal.QUEEN, Cardinal.JACK)));
        preflopBucketSet.add(new PairBucket(new CardinalRange(Cardinal.TEN, Cardinal.SEVEN)));
        preflopBucketSet.add(new PairBucket(new CardinalRange(Cardinal.SIX, Cardinal.TWO)));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, KING)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, QUEEN), new HoleCardinals(ACE, JACK)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, TEN), new HoleCardinals(ACE, SEVEN)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, SIX), new HoleCardinals(ACE, TWO)));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, QUEEN), new HoleCardinals(KING, JACK)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, TEN), new HoleCardinals(QUEEN, EIGHT)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, SEVEN), new HoleCardinals(KING, TWO)));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(QUEEN, JACK)));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(QUEEN, SEVEN), new HoleCardinals(QUEEN, FIVE)));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(JACK, TEN), new HoleCardinals(JACK, EIGHT)));

        // connectory
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(NINE, SEVEN), 0, false));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(SIX, TWO), 0, false));
//        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(EIGHT, SIX), 1, false));               // troche mniej konektorow offsuited niz suited
//        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(FIVE, TWO), 1, false));
//        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(SEVEN, SIX), 2, false));
//        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(FIVE, TWO), 2, false));

        // SUITED ponizej
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, KING), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, QUEEN), new HoleCardinals(ACE, JACK), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, TEN), new HoleCardinals(ACE, SEVEN), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(ACE, SIX), new HoleCardinals(ACE, TWO), true));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, QUEEN), new HoleCardinals(KING, JACK), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, TEN), new HoleCardinals(QUEEN, EIGHT), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(KING, SEVEN), new HoleCardinals(KING, TWO), true));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(QUEEN, JACK), true));
        preflopBucketSet.add(new RangeBucket(new HoleCardinals(QUEEN, SEVEN), new HoleCardinals(QUEEN, FIVE), true));

        preflopBucketSet.add(new RangeBucket(new HoleCardinals(JACK, TEN), new HoleCardinals(JACK, EIGHT), true));

        // connectory
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(NINE, SEVEN), 0, true));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(SIX, TWO), 0, true));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(EIGHT, SIX), 1, true));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(FIVE, TWO), 1, true));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(SEVEN, SIX), 2, true));
        preflopBucketSet.add(new ConnectorBucket(new CardinalRange(FIVE, TWO), 2, true));
    }

    /**
     * @param bucketsStr
     * @return PFBS na podstawie ich tekstowej reprezentacji, czyli np wekowych
     * argumentow.
     */
    public PreflopBucket getBucket(String bucketsStr) {
        PreflopBucket prb = this.string2prbMap.get(bucketsStr);
        if(prb == null) {
            throw new IllegalArgumentException("nie ma bucketa: " + bucketsStr);
        }
        
        return prb;
    }
    
    /**
     * @param bucketStr
     * @return true jezeli jest bucket o takim toString, false w przeciwnym wypadku.
     */
    public boolean exists(String bucketStr) {
        return this.string2prbMap.containsKey(bucketStr);
    }
    
    private void initBucketMap() {
        // zewnetrzna petla nieelegancko zmienia suited na false i true
        // 2 wewnetrzne tworza kolejne mozliwe kombinacje cardinali
        for(int b = 0; b < 2; b++) {
            boolean suited;

            if(b == 0) {
                suited = false;
            } else {
                suited = true;
            }

            for(Cardinal c1 : Cardinal.valuesNoX()) {
                for(Cardinal c2 : Cardinal.valuesNoX()) {
                    // parka na rece nie moze byc suited
                    if(suited == true && c1 == c2) {
                        continue;
                    }

                    HoleCardinals hc = new HoleCardinals(c1, c2);

                    // iterujemy po bucketach i sprawdzamy do ktorego takie cardinale naleza.
                    PreflopBucket foundPfb = null;
                    for(PreflopBucket pfb : preflopBucketSet) {
                        boolean belongs = pfb.belongsTo(hc, suited);

                        if(belongs) {
                            if(foundPfb == null) {
                                foundPfb = pfb;
                            } else {
                                // wczesniej jakis bucket zostal juz znaleziony
                                throw new IllegalStateException(hc + " nalezy do 2 bucketow: " + pfb + " i " + foundPfb);
                            }
                        }
                    }

                    // do mapy dodajemy
                    // jezeli bucket nie zostal znaleziony to dodajemy TrashBucket
                    BucketMapKey bucketMapKey = new BucketMapKey(suited, hc);
                    if(foundPfb != null) {
                        key2bucketMap.put(bucketMapKey, foundPfb);
                    } else {
                        key2bucketMap.put(bucketMapKey, new TrashBucket());
                    }
                }
            }
        }
    }

    private void initString2prbMap() {
        for (PreflopBucket preb : preflopBucketSet) {
            String toString = preb.toString();
            string2prbMap.put(toString, preb);
        }
        
        // trashbucket na koniec dodajemy
        TrashBucket trashBucket = new TrashBucket();
        string2prbMap.put(trashBucket.toString(), trashBucket);
    }
    
    /**
     * Klucz do mapy z bucketami.
     */
    private static class BucketMapKey {

        private Boolean suited;

        private HoleCardinals hc;

        public BucketMapKey(Boolean suited, HoleCardinals hc) {
            this.suited = suited;
            this.hc = hc;
        }

        @Override
        public boolean equals(Object obj) {
            if(obj == null) {
                return false;
            }
            if(getClass() != obj.getClass()) {
                return false;
            }
            final BucketMapKey other = (BucketMapKey) obj;
            if(this.suited != other.suited && (this.suited == null || !this.suited.equals(other.suited))) {
                return false;
            }
            if(this.hc != other.hc && (this.hc == null || !this.hc.equals(other.hc))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + (this.suited != null ? this.suited.hashCode() : 0);
            hash = 47 * hash + (this.hc != null ? this.hc.hashCode() : 0);
            return hash;
        }
    }

    public static void main(String[] args) {
        PreflopBucket pfb;
        pfb = PreflopBucketProvider.getInstance().getBucket(new HoleCards("2s7c"));
        System.out.println(pfb);
    }
}


