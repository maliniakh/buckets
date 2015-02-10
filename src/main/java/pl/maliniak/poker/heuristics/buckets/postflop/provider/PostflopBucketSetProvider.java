package pl.maliniak.poker.heuristics.buckets.postflop.provider;

import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucketSet;

/**
 *
 * @author meliniak
 */
public interface PostflopBucketSetProvider {
    /**
     * @return Liste wiaderek w sytuacji okreslonej przez MozgInputArgument;
     * null gdy karty goscia nei sa znane.
     */
    public PostflopBucketSet getBuckets(TableCards tc, HoleCards hc);
}
