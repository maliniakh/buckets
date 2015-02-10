package pl.maliniak.poker.heuristics.buckets.postflop;

import pl.maliniak.cards.CardsHelper;
import pl.maliniak.poker.cardsets.HoleCards;
import pl.maliniak.poker.cardsets.HoleCardsSet;
import pl.maliniak.poker.cardsets.TableCards;
import pl.maliniak.poker.heuristics.buckets.Bucket;
import pl.maliniak.poker.heuristics.buckets.postflop.nonranked.flushdraw.BackdoorFlushDraw;
import pl.maliniak.poker.heuristics.buckets.postflop.provider.BasePostflopBucketSetProvider;
import pl.maliniak.poker.street.StreetName;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 *
 * @author meliniak
 */
public abstract class PostflopBucket extends Bucket {

    /**
     * number of hole cards used, if null then any number of cards might be used (0, 1 oraz 2 obv)
     */
    protected Integer holeCardsUsed;

    protected PostflopBucket() {
        super();
    }

    public PostflopBucket(Integer holeCardsUsed) {
        super();
        this.holeCardsUsed = holeCardsUsed;
    }

    public Integer getHoleCardsUsed() {
        return holeCardsUsed;
    }

    /**
     * @return buckets set, which implementing delegator might delegate.
     */
    public final Set<? extends PostflopBucket> getDelegates() {
        Set<? extends PostflopBucket> delegates = BasePostflopBucketSetProvider.getDelegates(this);

        return delegates;
    }

    @Override
    public HoleCardsSet getHoleCardSet(TableCards tc) {
        HoleCardsSet resultHcs = new HoleCardsSet();
        HoleCardsSet allPossibleHcs = CardsHelper.generatePossibleHoleCards(tc);


        for(HoleCards hc : allPossibleHcs) {
            if(this.belongsTo(tc, hc)) {
                resultHcs.add(hc);
            }
        }

        return resultHcs;
    }

    /**
     * @param tableCards
     * @param holeCards
     * @param street
     * @return New instance of a bucket. Might be this instance or an instance which extends it.
     */
    public PostflopBucket delegateBucket(TableCards tableCards, HoleCards holeCards, StreetName street) {
        boolean foundDelegate = false;
        PostflopBucket toInstatiate = null;

        Set<? extends PostflopBucket> delegates = this.getDelegates();
        boolean belongsTo;
        for(PostflopBucket pfb : delegates) {
            belongsTo = pfb.belongsTo(tableCards, holeCards);

            if(belongsTo) {
                toInstatiate = pfb;
                foundDelegate = true;
            }
        }

        try {
            // if found no delegating buckets, make new this.getClass() instance
            if(foundDelegate == false) {
                toInstatiate = this;
            }
            Class<? extends PostflopBucket> _class = toInstatiate.getClass();

            if(_class.equals(BackdoorFlushDraw.class)) {
                System.out.println("");
            }

            Constructor<? extends PostflopBucket> constructor = _class.getConstructor();

            // nowa instancja
            PostflopBucket newInstance = constructor.newInstance();

            return newInstance;
        } catch(NoSuchMethodException ex) {
            throw new IllegalStateException(ex);
        } catch(SecurityException ex) {
            throw new IllegalStateException(ex);
        } catch(InstantiationException ex) {
            throw new IllegalStateException(ex);
        } catch(IllegalAccessException ex) {
            throw new IllegalStateException(ex);
        } catch(IllegalArgumentException ex) {
            throw new IllegalStateException(ex);
        } catch(InvocationTargetException ex) {
            throw new IllegalStateException(ex);
        }
    }

    /**
     * Checks if the bucket suits these cards.<br/><br/>
     * <b>An assumption is made that belonging to super class is not tested. For exmaple,
     * if AceHighCard.belongsTo() is called, then the assumpation is that it already belongs to the HighCardBucket.
     * @param tc
     * @param hc
     * @return True, if bucket suits for these cards.
     */
    public abstract boolean belongsTo(TableCards tc, HoleCards hc);

    @Override
    public int hashCode() {
        // should be safe and unique
        String name = this.getClass().getName();
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(getClass() != obj.getClass()) {
            return false;
        }

        /**
         * not standard but should be safe.
         */
        return this.hashCode() == obj.hashCode();
    }
}
