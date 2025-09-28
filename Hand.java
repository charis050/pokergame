import java.util.*;
import java.lang.*;


/**
 * A poker hand is a list of cards, which can be of some "kind" (pair, straight, etc.)
 *
 */
public class Hand implements Comparable<Hand> {

    public enum Kind {HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT,
        FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH}

    private final List<Card> cards;


    /**
     * Create a hand from a string containing all cards (e,g, "5C TD AH QS 2D")
     */
    public Hand(String c) {
        cards = new ArrayList<Card>();

        String[] cards0 = c.split(" ");

        for(String card : cards0){
            cards.add(new Card(card));
        }

    }

    /**
     * @returns true if the hand has n cards of the same rank
	 * e.g., "TD TC TH 7C 7D" returns True for n=2 and n=3, and False for n=1 and n=4
     */
    protected boolean hasNKind(int n) {
        ArrayList<Card.Rank> ranks = new ArrayList<Card.Rank>();

        for(Card card : cards){
            ranks.add(card.getRank());
        }

        ArrayList<Integer> ranks2 = new ArrayList<Integer>();

        for(Card.Rank rank : ranks){
            ranks2.add(rank.ordinal());
        }
        Collections.sort(ranks2);
        for(int i = 0; i < (ranks2.size() - 1); i++){

        }
        int count = 1;
        for (int i = 1; i < ranks2.size(); i++) {
            if (ranks2.get(i).equals(ranks2.get(i - 1))) {
                count++;
                if (count == n) return true;
            } else {
                count = 1;
            }
        }

        return false;

    }

    /**
	 * Optional: you may skip this one. If so, just make it return False
     * @returns true if the hand has two pairs
     */
    public boolean isTwoPair() {
        // Collect rank ordinals
        ArrayList<Integer> ranks2 = new ArrayList<Integer>();
        for (Card c : cards) {
            ranks2.add(c.getRank().ordinal());
        }
        Collections.sort(ranks2);

        int pairs = 0;
        int i = 1;
        while (i < ranks2.size()) {
            if (ranks2.get(i).equals(ranks2.get(i - 1))) {
                pairs++;
                i += 2;              // skip both cards in this pair to avoid overlap (e.g., Q Q Q)
                if (pairs >= 2) return true;
            } else {
                i++;
            }
        }
        return false;
    }

    /**
     * @returns true if the hand is a straight
     */
    public boolean isStraight() {
        ArrayList<Card.Rank> ranks = new ArrayList<Card.Rank>();

        for(Card card : cards){
                ranks.add(card.getRank());
            }

        ArrayList<Integer> ranks2 = new ArrayList<Integer>();

        for(Card.Rank rank : ranks){
            ranks2.add(rank.ordinal());
        }
        Collections.sort(ranks2);
            // normal consecutive check (your original logic)
            for (int i = 0; i < ranks2.size() - 1; i++) {
                if (ranks2.get(i) + 1 != ranks2.get(i + 1)) {
                    // --- minimal Ace-low tweak (A-2-3-4-5) ---
                    // assume ACE is the highest ordinal in Card.Rank
                    int ace = Card.Rank.ACE.ordinal();
                    // first 4 must be consecutive, and last must be ACE
                    if (ranks2.get(ranks2.size() - 1) == ace) {
                        boolean firstFourConsecutive = true;
                        for (int j = 0; j < 3; j++) {
                            if (ranks2.get(j) + 1 != ranks2.get(j + 1)) {
                                firstFourConsecutive = false;
                                break;
                            }
                        }
                        if (firstFourConsecutive) return true; // treat Ace as low
                    }
                    // -----------------------------------------
                    return false;
                }
            }
            return true;
        }


        /**
         * @returns true if the hand is a flush
         */
    public boolean isFlush() {
        ArrayList<Card.Suit> suits = new ArrayList<Card.Suit>();
        for(Card card : cards){
            suits.add(card.getSuit());
        }
        ArrayList<Integer> suits2 = new ArrayList<Integer>();

        for(Card.Suit suit : suits){
            suits2.add(suit.ordinal());
        }

        for(int i = 0; i < (suits2.size() - 1); i++){
            if(suits2.get(i) != suits2.get(i + 1)){
                return false;
            }
        }
        return true;


    }

    @Override
    public int compareTo(Hand h) {
            return Integer.compare(this.kind().ordinal(), h.kind().ordinal());
        }

    /**
	 * This method is already implemented and could be useful!
     * @returns the "kind" of the hand: flush, full house, etc.
     */
    public Kind kind() {
        if (isStraight() && isFlush()) return Kind.STRAIGHT_FLUSH;
        else if (hasNKind(4)) return Kind.FOUR_OF_A_KIND;
        else if (hasNKind(3) && hasNKind(2)) return Kind.FULL_HOUSE;
        else if (isFlush()) return Kind.FLUSH;
        else if (isStraight()) return Kind.STRAIGHT;
        else if (hasNKind(3)) return Kind.THREE_OF_A_KIND;
        else if (isTwoPair()) return Kind.TWO_PAIR;
        else if (hasNKind(2)) return Kind.PAIR;
        else return Kind.HIGH_CARD;
    }

}
