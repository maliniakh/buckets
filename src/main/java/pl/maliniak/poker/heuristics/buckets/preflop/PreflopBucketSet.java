package pl.maliniak.poker.heuristics.buckets.preflop;


import com.google.common.collect.Iterables;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zbior wiaderek preflopowych. Zawsze zawiera tylko jedno jedno wiaderko,
 * ale dziedziczy po HashSet, zeby mozna bylo przypisywac oba sety (preflop i flop )
 * do tego samego pola (w MozgDesiredDataPostFlopBucket).
 * @author maliniak
 */
public class PreflopBucketSet extends HashSet<PreflopBucket> {
    private static final Logger logger = LoggerFactory.getLogger(PreflopBucketSet.class);

    public PreflopBucketSet(PreflopBucket pfb) {
        super.add(pfb);
    }

    @Override
    public String toString() {
        PreflopBucket pfb = Iterables.getOnlyElement(this);
        return pfb.toString();
    }
}
