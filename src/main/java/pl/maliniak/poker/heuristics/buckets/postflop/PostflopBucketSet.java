package pl.maliniak.poker.heuristics.buckets.postflop;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.postflop.comp.PFBComparatorNaturalImpl;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.DrawBucket;
import pl.maliniak.poker.heuristics.buckets.postflop.provider.BasePostflopBucketSetProvider;
import pl.maliniak.poker.heuristics.buckets.postflop.ranked.RankedPostFlopBucket;

import java.util.*;

/**
 * Zbior wiaderek, reka moze stanowic parke i rownoczesnie strit draw.
 * @author meliniak
 */
public class PostflopBucketSet extends HashSet<PostflopBucket>  {

    public static final long serialVersionUID = 2489L;

    private static Comparator<PostflopBucket> bucketComparator = new PFBComparatorNaturalImpl();

    private static Logger logger = LoggerFactory.getLogger(PostflopBucketSet.class);

    public PostflopBucketSet() {
    }
    
    /**
     * @param buckets 
     */
    public PostflopBucketSet(PostflopBucket ... buckets) {
        this.addAll(Arrays.asList(buckets));
    }
    
    /**
     * @return Zbior wszystkich mozliwych kart na rece odpowiadajacych danemu zbiorowi bucketow.
     */
    public HoleCardsSet getHoleCardsSet(TableCards tc) {
        RankedPostFlopBucket rankedPfb = this.getRankedPfb();
        Set<DrawBucket> drawBucketSet = this.getDrawBucketSet();

        HoleCardsSet hcs = new HoleCardsSet();
        HoleCardsSet rankedHcs = rankedPfb.getHoleCardSet(tc);
        HoleCardsSet allDrawHcs = new HoleCardsSet();

        // zbieramy wszystkie karty ktore daja jakiegokolwiek draw'a
        Set<DrawBucket> allDrawBucketSet = BasePostflopBucketSetProvider.getDrawBucketSet();
        for(DrawBucket drawBucket : allDrawBucketSet) {
            HoleCardsSet drawHcs = drawBucket.getHoleCardSet(null);
            allDrawHcs.addAll(drawHcs);
        }

        // nastepnie odejmujemy je od ranked HCS
        SetView<HoleCards> differenceSet = Sets.difference(rankedHcs, allDrawHcs);
        hcs.addAll(differenceSet);

        // a na koncu dodajemy karty tylko z faktycznie majacego miejsce draw/drawow
        for(DrawBucket drawBucket : drawBucketSet) {
            HoleCardsSet drawHcs = drawBucket.getHoleCardSet(null);
            hcs.addAll(drawHcs);
        }
        
        return hcs;
    }

    public int rankedBucketsCount() {
        int rankedBucketsCount = 0;

        for(PostflopBucket pfb : this) {
            if(pfb instanceof RankedPostFlopBucket) {
                rankedBucketsCount++;
            }
        }

        return rankedBucketsCount;
    }

    public int drawBucketsCount() {
        int drawBucketsCount = 0;

        for(PostflopBucket pfb : this) {
            if(pfb instanceof DrawBucket) {
                drawBucketsCount++;
            }
        }

        return drawBucketsCount;
    }

    /**
     * @param _class
     * @return Zwraca true, jezeli set zawiera bucket zadanej klasy, lub bucket nadklasy
     * dla danej klasy (czyli instanceof operator jest uzywany).
     */
    public boolean contains(Class<? extends PostflopBucket> _class) {
        for(PostflopBucket pfb : this) {
            Class<? extends PostflopBucket> pfbClass = pfb.getClass();
            if(_class.isAssignableFrom(pfbClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return Zwraca bucket rankingowy (kazdy zbior musi miec dokladnie 1 taki bucket).
     */
    private RankedPostFlopBucket getRankedPfb() {
        for(PostflopBucket pfb : this) {
            if(pfb instanceof RankedPostFlopBucket) {
                return (RankedPostFlopBucket) pfb;
            }
        }

        return null;
    }

    /**
     * @return Zbior wiaderek drawowych.
     */
    private Set<DrawBucket> getDrawBucketSet() {
        Set<DrawBucket> drawBucketSet = new HashSet<DrawBucket>();
        
        for(PostflopBucket pfb : this) {
            if(pfb instanceof DrawBucket) {
                drawBucketSet.add((DrawBucket) pfb);
            }
        }

        return drawBucketSet;
    }

    @Override
    public String toString() {
        String str = "";

        // sortujemy w ten sposob ze najpierw wypisywane sa
        // wiaderka rankingowane
        List<PostflopBucket> list = new ArrayList<PostflopBucket>(this);
        Collections.sort(list, bucketComparator);

        for(PostflopBucket pfb : list) {
            str += pfb + " ";
        }

        str = str.trim();
        str = str.replace(" ", "_");

        return str;
    }
}
