package cribbage;

/**
 * The Deck class represents a shuffled deck of cards.
 * It provides several operations including
 *      initialize, shuffle, deal, and check if empty.
 */
public class Deck 
{
	private String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
	
	private Card[] cards;
	
	private int undealt;

	public Deck() 
	{
		cards = new Card[52];
		int index = 0;
		for(int v = 1; v <= 13; v++)
		{
			for(int s = 0; s < suits.length; s++)
			{
				cards[index] = new Card(v, suits[s]);
				index++;
			}
		}
		undealt = cards.length;
	}


	/**
	 * Determines if this deck is empty (no undealt cards).
	 * @return true if this deck is empty, false otherwise.
	 */
	public boolean isEmpty() 
	{
		if(undealt == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Accesses the number of undealt cards in this deck.
	 * @return the number of undealt cards in this deck.
	 */
	public int getCardsLeft() 
	{
		return undealt;
	}

	/**
	 * Randomly permute the given collection of cards
	 * and reset the size to represent the entire deck.
	 */
	public void shuffle() 
	{
		for(int index = 0; index < cards.length; index++)
			{
				int place = (int) (Math.random() * cards.length);
				Card holder = cards[place];
				cards[place] = cards[index];
				cards[index] = holder;				
			}
		undealt = cards.length;
	}

	/**
	 * Deals a card from this deck.
	 * @return the card just dealt, or null if all the cards have been
	 *         previously dealt.
	 */
	public Card deal() 
	{
		if(isEmpty())
		{
			return null;
		}
		else
		{
			Card toReturn = cards[cards.length - undealt];
			undealt--;
			return toReturn;
		}
	}
}