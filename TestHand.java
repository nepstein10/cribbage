package cribbage;

import java.util.ArrayList;

public class TestHand extends Hand 
{
	private ArrayList<Card> cards;

	public TestHand(ArrayList<Card> input) 
	{
		super(input);
	}

	public TestHand() 
	{
		super();
	}

	public boolean pegRuns() 
	{
		ArrayList<Card> sorted = sortHand();
		for(int i = 0; i < sorted.size()-1; i++)
		{
			if(sorted.get(i).getValue() != sorted.get(i+1).getValue() - 1)
				return false;
		}
		return true;
	}

}
