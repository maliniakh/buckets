package game.eval.stevebrecher;

import java.util.*;

/**
 * A set of distinct {@link Card}s.
 * <p>This implementation is a wrapper on {@link ArrayList}<{@link Card}>
 * that allows no duplicates.  A CardSet's iterator will provide elements
 * in FIFO order -- the order in which the elements were added -- if the
 * instance's shuffle method has not been invoked.
 * <p>It also provides a 52-card deck.
 * <p>Methods not otherwise documented forward to {@link ArrayList}<{@link Card}> or perform
 * as specified by the {@link Set} interface.
 * @version 2006Dec04.0
 * @author Steve Brecher
 *
 */
public final class SBCardSet implements Set<SBCard> {
	
	private ArrayList<SBCard> cards;
	
	private static final SBCardSet madeDeck = new SBCardSet(52);
	static {
		for (SBCard.Suit suit : SBCard.Suit.values())
			for (SBCard.Rank rank : SBCard.Rank.values())
				madeDeck.add(new SBCard(rank, suit));
	}

	/**
	 * Return an ordered 52-card deck.
	 * @return a 52-card deck in order from clubs to spades and within each suit from deuce to Ace.
	 */
	public static SBCardSet freshDeck( ) {
		return new SBCardSet(madeDeck);
	}

	/**
	 * Return a shuffled 52-card deck.
	 * @return a shuffled 52-card deck.
	 */  
	public static SBCardSet shuffledDeck() {
		SBCardSet result = new SBCardSet(madeDeck);
		Collections.shuffle(result.cards);
		return result;
	}

	public SBCardSet() {
		cards = new ArrayList<SBCard>();
	}

	public SBCardSet(int initialCapacity) {
		cards = new ArrayList<SBCard>(initialCapacity);
	}

	/**
	 * Copy constructor
	 */
	public SBCardSet(SBCardSet source) {
		cards = new ArrayList<SBCard>(source.cards);
	}

	/**
	 * Returns <code>true</code> if this CardSet did not already contain the specified Card.
	 * @return <code>true</code> if this CardSet did not already contain the specified Card.
	 */
	public boolean add(SBCard c) {
		if (cards.contains(c))
			return false;
		return cards.add(c);
	}
	
	/**
	 * Returns <code>true</code> if this CardSet changed as a result of the call.
	 * @return <code>true</code> if this CardSet changed as a result of the call; <code>false</code>
	 * if all of the Cards in the specified Collection were already present in this CardSet.
	 */
	public boolean addAll(Collection<? extends SBCard> coll) {
		boolean result = false;
		for (SBCard c : coll)
			result |= add(c);
		return result;
	}

	public void clear() {
		cards.clear();
	}

	public boolean contains(Object o) {
		return cards.contains(o);
	}

	public boolean containsAll(Collection<?> coll) {
		return cards.containsAll(coll);
	}

	@Override public boolean equals(Object that) {
		if (!(that instanceof Set) || ((Set)that).size() != cards.size())
			return false;
		for (SBCard c : cards)
			if (!((Set)that).contains(c))
				return false;
		return true;
	}

	@Override public int hashCode() {
		int result = 0;
		for (SBCard c : cards)
			result += c.hashCode();
		return result;
	}

	public boolean isEmpty() {
		return cards.isEmpty();
	}

	public Iterator<SBCard> iterator() {
		return cards.iterator();
	}

	public boolean remove(Object o) {
		return cards.remove(o);
	}

	public boolean removeAll(Collection<?> coll) {
		return cards.removeAll(coll);
	}

	public boolean retainAll(Collection<?> coll) {
		return cards.retainAll(coll);
	}

	public void shuffle() {
		Collections.shuffle(cards);
	}

	public int size() {
		return cards.size();
	}

	public SBCard[] toArray() {
		return cards.toArray(new SBCard[cards.size()]);
	}
	
	public <T> T[] toArray(T[] a) {
		return cards.toArray(a);
	}

	/**
	 * Returns a {@link String} containing a comma-space-separated list of cards.
	 * @return a {@link String} containing a comma-space-separated list of cards,
	 *			each the result of {@link Card#toString()}.
	 */
	@Override public String toString() {
		return cards.toString();
	}
}
