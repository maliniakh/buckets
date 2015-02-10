package pl.maliniak.poker.heuristics.buckets.preflop;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;

/**
 *
 * @author maliniak
 */
public class TrashBucket extends PreflopBucket {
    private static final Logger logger = LoggerFactory.getLogger(TrashBucket.class);

    public TrashBucket() {
        super(false);
    }

    @Override
    public boolean belongsTo(HoleCards hc) {
        throw new UnsupportedOperationException("Ta metoda nie jest wolana");
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
