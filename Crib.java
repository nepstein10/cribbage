package cribbage;

import java.util.ArrayList;

public class Crib extends Hand
{
	private ArrayList<Card> cards;
	
	public Crib(ArrayList<Card> toHand) 
	{
		super(toHand);
	}

	public Crib() 
	{
		super();
	}

	private int sFlush() 
	{
		int subScore = 0;
		String suit = cards.get(0).getSuit();
		int cardsInSuit = 1;
		int index = 1;
		while(cards.get(index).getSuit().equals(suit) && index < cards.size() -1)
		{
			cardsInSuit++;
			index++;
		}
		if(cardsInSuit >= 5)
		{
			subScore += cardsInSuit;
			if(cards.get(cards.size() - 1).getSuit().equals(suit))
			{
				subScore++;
			}
		}
		return subScore;
	}

}
