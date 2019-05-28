package cribbage;

import java.util.ArrayList;

public class Hand 
{
	private ArrayList<Card> cards; //array of cards, with the cut as the last card in the hand when scoring

	public Hand(ArrayList<Card> input)
	{
		cards = input;
	}
	
	public Hand() 
	{
		cards = new ArrayList<Card>();
	}
	
	public String toString()
	{
		return cards.toString();
	}

	public ArrayList<Card> getCards()
	{
		return cards;
	}
	
	public void addCard(Card toAdd)
	{
		cards.add(toAdd);
	}
	
	public Card removeCard(int index)
	{
		return cards.remove(index);
	}
	
	public int score()
	{
		int score = 0;
		
		int pairs = sPairs();
		score += pairs;
		//System.out.println("pairs: " + pairs);
		int runs = sRuns();
		score += runs;
		//System.out.println("Runs: " + runs);
		int fifteens = sFifteens();
		score += fifteens;
		//System.out.println("15s: " + fifteens);
		int flush = sFlush();
		int rtJack = sRtJack();
		score += flush;
		score += rtJack;//right jack, flush. "Crib" class should overwrite for flush rule
		//System.out.println("Other: " + (flush + rtJack));	
		return score;
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
		if(cardsInSuit >= 4)
		{
			subScore += cardsInSuit;
			if(cards.get(cards.size() - 1).getSuit().equals(suit))
			{
				subScore++;
			}
		}
		return subScore;
	}

	private int sRtJack() 
	{
		int subScore = 0;
		for(int i = 0; i < cards.size() - 1; i++)
		{
			if(cards.get(i).getValue() == 11)
			{
				if(cards.get(i).getSuit().equals(cards.get(cards.size() - 1).getSuit()))
				{
					subScore++;
				}
			}
		}
		return subScore;
	}

	int sRuns()
	{
		int subScore = 0;
		ArrayList<Card> sorted = sortHand();
		for(int i = 0; i < sorted.size() - 2; i++)
		{
			int cardsInRun = 1;
			int j = i + 1;
			int lastVal = sorted.get(i).getValue();
			int doubles = 0;
			int triples = 0;
			boolean tripMaybe = false;
			while(j < sorted.size() && (sorted.get(j).getValue() == lastVal || sorted.get(j).getValue() == lastVal + 1))
			{
				if(sorted.get(j).getValue() == lastVal)
				{
					doubles++;
					if(!tripMaybe)
						tripMaybe = true;
					else if(tripMaybe)
					{
						triples = 1;
						tripMaybe = false;
					}
				}
				if(sorted.get(j).getValue() == lastVal + 1)
				{
					cardsInRun++;
					lastVal = sorted.get(j).getValue();
					tripMaybe = false;
				}
				j++;
			}
			if(cardsInRun >= 3)
			{
				if(doubles != 0 && triples == 0)
				{
					for(int d = 0; d < doubles; d++)
					{
						subScore += 2 * cardsInRun;
					}
				}
				else if(triples == 1)
				{
					subScore += 3 * cardsInRun;
				}
				else
				{
					subScore += cardsInRun;
				}
				i += j - i - 1;
			}
		}
		return subScore;
	}

	public ArrayList<Card> sortHand()
	{
		ArrayList<Card> toSort = new ArrayList<Card>(cards);
		for(int i = 0; i < toSort.size(); i++)
		{
			int j = i;
			while(j > 0 && toSort.get(j).compareTo(toSort.get(j - 1)) < 0)
			{
				Card holder = toSort.get(j);
				toSort.set(j, toSort.get(j - 1));
				toSort.set(j - 1, holder);
				j--;
			}
		}
		return toSort;
	}
	
	private int sFifteen()
	{
		int subScore = 0;
		for(int i = 0; i < cards.size() - 1; i++)
			fifteenRecurse(subScore, i);
		return subScore;
	}
	
	private int fifteenRecurse(int subScore, int i)
	{
		for(int j = i+1; j < cards.size(); j++)
		{
			int subtotal = cards.get(i).getScoreVal();
			if(subtotal + cards.get(j).getScoreVal() == 15)
			{
				subScore += 2;
				System.out.print("+");
			}
			else if(subtotal + cards.get(j).getScoreVal() < 15)
				fifteenRecurse(subScore, j);
		}
		return subScore;
	}

	private int sFifteens() //make a recursive version!
	{
		int subScore = 0;
		for(int i = 0; i < cards.size() - 1; i++)
		{				
			for(int j = i + 1; j < cards.size(); j++)	//from here
			{
				int subtotal = cards.get(i).getScoreVal();
				if(subtotal + cards.get(j).getScoreVal() == 15)
				{
					subScore += 2;
				}
				else if(subtotal + cards.get(j).getScoreVal() < 15)
				{
//through to here
					for(int k = j + 1; k < cards.size(); k++)
					{					
						subtotal = cards.get(i).getScoreVal() + cards.get(j).getScoreVal();
						if(subtotal + cards.get(k).getScoreVal() == 15)
						{
							subScore += 2;
						}
						else if(subtotal + cards.get(k).getScoreVal() < 15)
						{
							for(int m = k + 1; m < cards.size(); m++)
							{
								subtotal = cards.get(i).getScoreVal() + cards.get(j).getScoreVal() + cards.get(k).getScoreVal();
								if(subtotal + cards.get(m).getScoreVal() == 15)
								{
									subScore += 2;
								}
								else if(subtotal + cards.get(m).getScoreVal() < 15)
								{
									for(int n = m + 1; n < cards.size(); n++)
									{
										subtotal = cards.get(i).getScoreVal() + cards.get(j).getScoreVal() + cards.get(k).getScoreVal() + cards.get(m).getScoreVal();
										if(subtotal + cards.get(n).getScoreVal() == 15)
										{
											subScore += 2;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return subScore;
	}
	
	private int sPairs() 
	{
		int subScore = 0;
		for(int i = 0; i < cards.size() - 1; i++)
		{
			for(int j = i + 1; j < cards.size(); j++)
			{
				if(cards.get(i).getValue() == cards.get(j).getValue())
				{
					subScore += 2;
				}
			}
		}
		return subScore ;
	}
}
