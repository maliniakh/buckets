package pl.maliniak.poker.heuristics.buckets.postflop.provider;

import junit.framework.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.PostflopBucketSet;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.BackdoorFlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.straightdraw.StraightDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.highcard.HighCardBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.HighPair;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.pair.SecondHighPair;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips1Hcu;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.trips.Trips2Hcu;

/**
 */
public class PostflopBucketSetProviderGPImplTest {
    private static final Logger logger = LoggerFactory.getLogger(PostflopBucketSetProviderGPImplTest.class);

    @Test
    public void testGetBuckets() throws Exception {
        PostflopBucketSetProvider pfbsProvider = new PostflopBucketSetProviderGPImpl();

        TableCards tc = new TableCards("Ac2h4c");
        HoleCards hc = new HoleCards("AsKs");
        PostflopBucketSet pfbs = pfbsProvider.getBuckets(tc, hc);
        PostflopBucketSet expected = new PostflopBucketSet(new HighPair());
        Assert.assertEquals(expected, pfbs);

        tc = new TableCards("Ac2h4c");
        hc = new HoleCards("4sKs");
        pfbs = pfbsProvider.getBuckets(tc, hc);
        expected = new PostflopBucketSet(new SecondHighPair());
        Assert.assertEquals(expected, pfbs);

        tc = new TableCards("As2h4c");
        hc = new HoleCards("4sKs");
        pfbs = pfbsProvider.getBuckets(tc, hc);
        expected = new PostflopBucketSet(new SecondHighPair(), new BackdoorFlushDraw());
        System.out.println(expected);
        Assert.assertEquals(expected, pfbs);

        tc = new TableCards("Kh5h6c");
        hc = new HoleCards("5s5c");
        pfbs = pfbsProvider.getBuckets(tc, hc);
        expected = new PostflopBucketSet(new Trips2Hcu());
        Assert.assertEquals(expected, pfbs);

        tc = new TableCards("Kh5h5c");
        hc = new HoleCards("5s6c");
        pfbs = pfbsProvider.getBuckets(tc, hc);
        expected = new PostflopBucketSet(new Trips1Hcu());
        Assert.assertEquals(expected, pfbs);

        tc = new TableCards("Kh7h8c");
        hc = new HoleCards("5s6c");
        pfbs = pfbsProvider.getBuckets(tc, hc);
        expected = new PostflopBucketSet(new StraightDraw(), new HighCardBucket());
        Assert.assertEquals(expected, pfbs);
    }
}