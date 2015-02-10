package pl.maliniak.poker.heuristics.buckets.preflop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import pl.maliniak.cards.Card;
import pl.maliniak.cards.Cardinal;
import pl.maliniak.cards.Suit;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.heuristics.buckets.Bucket;

/**
 * Wiaderko preflopowe.
 * @author meliniak
 */
public abstract class PreflopBucket extends Bucket {
    /**
     * Do implementacji singletonu.
     */
    private static Map<Class, PreflopBucket> class2BucketMap = new HashMap<Class, PreflopBucket>();
    /**
     * Wiaderka zarejestrowane w klasie.
     */
    private static Set<PreflopBucket> registeredBuckets = new HashSet<PreflopBucket>();

    /**
     * Czy kart sa w tym samym kolorze.
     */
    protected boolean suited = false;

    static {
        PreflopBucket.registerBuckets();
    }
    
    protected PreflopBucket(boolean suited) {
        super();
        this.suited = suited;
    }

    @SuppressWarnings("unchecked")
    public static <T extends PreflopBucket> T getInstance(Class<T> _class) {
        T bucket = (T) class2BucketMap.get(_class);

        if (bucket == null) {
            try {
                // TODO: zrobic tak zeby konstruktor wiaderek mogl byc protected
                bucket = _class.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(PreflopBucket.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PreflopBucket.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            class2BucketMap.put(_class, bucket);
        }

        return bucket;
    }

    private static void registerBuckets() {
    }
    
    /**
     * Sprawdza czy reka nalezy do danego bucketu.
     * @param hc Reka, ktora nas interesuje.
     * @return True, gdy nalezy do wiaderka.
     */
    public abstract boolean belongsTo(HoleCards hc);

    /**
     * @param hc
     * @param suited
     * @return True, jezeli takie cardinale i ich suitowosc naleza do bucketu.
     */
    public boolean belongsTo(HoleCardinals hc, boolean suited) {
        if(suited != this.suited) {
            return false;
        }

        HoleCards holeCards;

        // przykladowe kolorki dla suited badz offsuited
        if(suited) {
            holeCards = new HoleCards(new Card(hc.getC1(), Suit.CLUBS), new Card(hc.getC2(), Suit.CLUBS));
        } else {
            holeCards = new HoleCards(new Card(hc.getC1(), Suit.CLUBS), new Card(hc.getC2(), Suit.SPADES));
        }

        return this.belongsTo(holeCards);
    }
    
    /**
     * Wyszukuje klase odpowiedniego wiaderka na podstawie reki.
     * @param hand Reka, ktorej trzeba znalezc wiaderko.
     * @return Wyszukana klasa wiaderka.
     */
    // <editor-fold defaultstate="collapsed" desc="getPreflopBucket">
    @Deprecated
    public static PreflopBucket getPreflopBucket(HoleCards hand) {
        PreflopBucket bucket = null;

        // iterujemy po zarejestrowanych wiaderkach i sprawdzamy
        // czy karty naleza do ktoregos

        for(PreflopBucket pfb : PreflopBucket.registeredBuckets) {
            if(pfb.belongsTo(hand)) {
                bucket = pfb;
            }
        }

        if(bucket == null) {
            throw new IllegalStateException("Nie znaleziono bucketu do ktorego karta mialaby nalezec");
        }

        return bucket;
    }// </editor-fold>

    /**
     * Rejestruje wiaderka m.in. do wyszukiwania wiaderek na podstawie rak.
     * @param bucket Bucket co chcemy dodać.
     */
    public static void registerBucket(PreflopBucket bucket) {
        PreflopBucket.registeredBuckets.add(bucket);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }
        final PreflopBucket other = (PreflopBucket) obj;
        if(this.suited != other.suited) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (this.suited ? 1 : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "PFBucket";
    }
    
    public static void main(String[] args) {
        HoleCards hc = new HoleCards(new Card(Cardinal.ACE, Suit.DIAMONDS),
                new Card(Cardinal.TWO, Suit.SPADES));
    }
}
