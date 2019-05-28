package cribbage;

public class Card implements Comparable<Card>
{
	private int value;
	private String suit;
	private int scoreVal;

	public Card(int v, String s)
	{
		value = v;
		suit = s;
		if(v > 10)
			scoreVal = 10;
		else
			scoreVal = v;
	}

	public String toString()
	{
		String[] types = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};
		String cardType = types[value - 1];
		return cardType + " of " + suit;
	}
	
	public int getValue()
	{
		return value;
	}
	
	public int getScoreVal()
	{
		return scoreVal;
	}

	// for testing purposes
	public static void main(String[] args)
	{
		for (int i = 0; i < 5; i++)
		{
			Card c = new Card((int) (Math.random() * 13 + 1), "Clubs");
			System.out.println(c);
		}
	}

	@Override
	public int compareTo(Card other)
	{
		if(value > other.getValue())
			return 1;
		else if(value == other.getValue())
			return 0;
		else
			return -1;
	}

	public String getSuit() 
	{
		return suit;
	}

}