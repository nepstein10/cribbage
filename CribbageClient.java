package cribbage;

import java.util.ArrayList;
import java.util.Scanner;

public class CribbageClient 
{
	private Deck deck;
	
	private int score1;
	private int score2;
	private Scanner c = new Scanner(System.in);
	
	public static void main(String[] args)
	{
		CribbageClient bob = new CribbageClient();
		//bob.testHand();
		bob.newGame();
		bob.playGame();
	}
	
	public CribbageClient()
	{
		deck = new Deck();
		deck.shuffle();
		newGame();
	}
	
	public void newGame()
	{
		score1 = 0;
		score2 = 0;
		System.out.println("Let's play Cribbage!");
	}
	
	public void playGame()
	{
		int dealer = (int) (Math.random() * 2 + 1);
		System.out.println("Player " + dealer + " will deal first.");
		while(!gameOver())
		{
			dealer = playHand(dealer);
		}
		if(score1 >= 121)
			System.out.println("Player one wins!");
		else if(score2 >= 121)
			System.out.println("Player two wins!");
		System.out.println("Final Score:     Player 1: " + score1 + "   Player 2: " + score2);
		System.out.println("Want to play again? (y/n)");
		if(c.nextLine().equalsIgnoreCase("y"))
		{
			newGame();
			playGame();
		}
		else if(c.nextLine().equalsIgnoreCase("n"))
			System.out.print("Thanks for playing!");
	}
	
	private int playHand(int dealer)
	{
		deck.shuffle();
		
		System.out.println("Score:     Player 1: " + score1 + "   Player 2: " + score2);
		Hand hand1 = new Hand();
		for(int i = 0; i < 6; i++)
			hand1.addCard(deck.deal());
		Hand hand2 = new Hand();
		for(int i = 0; i < 6; i++)
			hand2.addCard(deck.deal());
		
		Crib crib = new Crib();
		sendToCrib(1, dealer, hand1, crib);
		sendToCrib(2, dealer, hand2, crib);
		
		Card cut = deck.deal();
		System.out.println("The cut: " + cut);
		if(cut.getValue() == 11)
		{
			score(dealer, 2);
			if(gameOver())
				return 0;
		}
		
		if(dealer == 1)
		{
			pegging(dealer, hand1, hand2);
			if(!gameOver())
				handScoring(dealer, hand1, hand2, crib, cut);
		}
		else if(dealer == 2)
		{
			pegging(dealer, hand2, hand1);
			if(!gameOver())
				handScoring(dealer, hand2, hand1, crib, cut);
		}
		
		return dealer % 2 + 1;
	}
	
	private void handScoring(int dealer, Hand dealerHand, Hand otherHand, Crib crib, Card cut) 
	{
		dealerHand.addCard(cut);
		otherHand.addCard(cut);
		crib.addCard(cut);
		int otherScore = otherHand.score();
		System.out.println("Player "+(dealer%2+1)+"'s hand: "+otherHand+"   Hand score: "+otherScore);
		score(dealer%2+1, otherScore);
		System.out.println();
		if(gameOver())
			return;
		int dealerScore = dealerHand.score();
		System.out.println("Player "+dealer+"'s hand: "+dealerHand+"   Hand score: "+dealerScore);
		score(dealer, dealerScore);
		System.out.println();
		if(gameOver())
			return;
		int cribScore = crib.score();
		System.out.println("Player "+dealer+"'s crib: "+crib+"   Crib score: "+cribScore);
		score(dealer, cribScore);
		System.out.println();
	}

	private void sendToCrib(int player, int dealer, Hand hand, Crib crib)
	{
		System.out.println("Player " + player +" ready? (press enter to proceed)");
		c.nextLine();
		if(player == dealer)
			System.out.println("Pick 2 cards to place in your crib");
		else
			System.out.println("Pick 2 cards to place in Player " + dealer + "'s crib");
		System.out.println(hand);
		System.out.print("Pick the first card (1-" + hand.getCards().size() + "):");
		int index = conToInt(c.nextLine()) - 1;
		while(!inRange(hand, index))
		{
			System.out.print("Please type a number between 1 and " + hand.getCards().size());
			index = conToInt(c.nextLine()) - 1;
		}
		crib.addCard(hand.removeCard(index));
		System.out.println(hand);
		System.out.print("Pick the second card (1-" + hand.getCards().size() + "):");
		index = conToInt(c.nextLine()) - 1;
		while(!inRange(hand, index))
		{
			System.out.print("Please type a number between 1 and " + hand.getCards().size());
			index = conToInt(c.nextLine()) - 1;
		}
		crib.addCard(hand.removeCard(index));
		for(int i = 0; i < 10; i++)
			System.out.println();
	}
	
	private void pegging(int dealer, Hand dealerHand, Hand otherHand) 	//runs are still having trouble scoring, otherwise pegging is operational
	{																	//maybe make a message so that what happened previously is visible?
		int total = 0;													//also need to do a check for in range, maybe a boolean method with a try/catch
		ArrayList<Card> played = new ArrayList<Card>();					//check after each score, somehow kick out if someone wins
		ArrayList<Card> dTemp = new ArrayList<Card>();
		dTemp.addAll(dealerHand.getCards());
		Hand dealerPeg = new Hand(dTemp);
		ArrayList<Card> oTemp = new ArrayList<Card>();
		oTemp.addAll(otherHand.getCards());
		Hand otherPeg = new Hand(oTemp);
		int player = dealer % 2 + 1;
		System.out.println("Time for pegging!");
		while(dealerPeg.getCards().size() > 0 || otherPeg.getCards().size() > 0)
		{
			System.out.println("Score:     Player 1: " + score1 + "   Player 2: " + score2);
			System.out.println("Player " + player +" ready? (press enter to proceed)");
			c.nextLine();
			if(player == dealer)
			{
				if(gameOver())
					return;
				else
					total = pegCard(player, dealerPeg, otherPeg, played, total);
			}
			else
			{
				if(gameOver())
					return;
				else
					total = pegCard(player, otherPeg, dealerPeg, played, total);
			}

			for(int i = 0; i < 10; i++)
				System.out.println();
			player = player % 2 + 1;
		}
	}
	
	private int pegCard(int player, Hand playerHand, Hand otherHand, ArrayList<Card> played, int total) 
	{
		if(playerHand.getCards().size() == 0)
			return 0;
		System.out.println("Current count: " + total);
		if(played.size() > 0)
			System.out.println("Played cards: " + played);
		System.out.println("Pick a card to play (1-" + playerHand.getCards().size() + "): " + playerHand);
		int selected = conToInt(c.nextLine()) - 1;
		while(!inRange(playerHand, selected))
		{
			System.out.println("Please type a number between 1 and " + playerHand.getCards().size());
			selected = conToInt(c.nextLine()) - 1;
		}
		while(!validPeg(playerHand.getCards().get(selected), total))
		{
			System.out.println("Invalid play. Select a new card: " + playerHand);
			selected = conToInt(c.nextLine()) - 1;
			while(!inRange(playerHand, selected))
			{
				System.out.println("Please type a number between 1 and " + playerHand.getCards().size());
				selected = conToInt(c.nextLine()) - 1;
			}
		}
		Card peg = playerHand.getCards().remove(selected);
		played.add(peg);
		total += peg.getScoreVal();
		
		total = scorePeg(player, playerHand, otherHand, played, total);
		
		return total;	
	}

	private boolean validPeg(Card selected, int total) 
	{
		if(selected.getScoreVal() + total > 31)
			return false;
		else
			return true;
	}
	
	private int scorePeg(int player, Hand playerHand, Hand otherHand, ArrayList<Card> played, int total)
	{
		boolean turnover = false;
		pegPairsScore(player, played);
		pegRunsScore(player, played, 3);
		if(total == 31)
		{
			System.out.println("It's a go!");
			score(player, 2);
			turnover = true;
		}
		else if(total == 15)
		{
			System.out.println("Fifteen for 2!");
			score(player, 2);
		}
		else if(!canPlay(otherHand, total))	//other player can't play, it's a go!
		{
			System.out.println("It's a go!");
			if(canPlay(playerHand, total))
			{
				System.out.println("Play another card, Player " + player + "!");
				if(!gameOver())
					total = pegCard(player, playerHand, otherHand, played, total);	//not sure yet if this will work. don't see why it wouldn't though...
				else
					return 0;
			}
			else
			{
				score(player, 1);
				turnover = true;
			}
		}
		if(turnover)
		{
			total = 0;
			played.add(null);
		}
		return total;
	}

	private boolean canPlay(Hand hand, int total)
	{
		for(int i = 0; i < hand.getCards().size(); i++)
			if(hand.getCards().get(i).getScoreVal() + total <= 31)
				return true;
		return false;
	}
	
	private void score(int player, int score)
	{
		if(player == 1)
			score1 += score;
		if(player == 2)
			score2 += score;
		System.out.println("Score:     Player 1: " + score1 + "   Player 2: " + score2);
	}
	
	private void pegPairsScore(int player, ArrayList<Card> played)
	{
		int thisRound = cardsThisRound(played);
		if(thisRound > 1 && played.get(played.size()-1).getValue() == played.get(played.size()-2).getValue())
		{
			if(thisRound > 2 && played.get(played.size()-1).getValue() == played.get(played.size()-3).getValue())
				score(player, 6);
			else
				score(player, 2);
		}
	}
	
	private void pegRunsScore(int player, ArrayList<Card> played, int cards) // aw crud i have to go all the way back through no matter what
	{
		int i = played.size()-1;
		int count = 0;
		while(i >= 0 && played.get(i) != null)
		{
			count++;
			i--;
		}
		if(count >= 3)
		{
			for(int j = count; j >= 3; j--)
			{
				TestHand test = new TestHand();
				for(int k = played.size()-1; k >= played.size()-j; k--)
					test.addCard(played.get(k));
				if(test.pegRuns())
				{
					System.out.println("It's a run of " + j + "!");
					score(player, j);
					return;
				}
			}
		}
	}
	
	public int cardsThisRound(ArrayList<Card> played)
	{
		int cards = 0;
		int i = played.size() - 1;
		while(i >= 0 && played.get(i) != null)
		{
			cards++;
			i--;
		}
		return cards;
	}
	
	private boolean inRange(Hand hand, int index)
	{
		if(index < hand.getCards().size() && index >= 0)
			return true;
		else
			return false;
	}
	
	private boolean gameOver()
	{
		if(score1 >= 121 || score2 >= 121)
			return true;
		else
			return false;
	}
	
	private boolean isNum(String test)
	{
		try
		{
			Integer.parseInt(test);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	private int conToInt(String input)
	{
		while(!isNum(input))
		{
			System.out.println("Please enter a number");
			input = c.nextLine();
		}
		return Integer.parseInt(input);
	}
	
	public void testHand()
	{
		ArrayList<Card> toHand = new ArrayList<Card>();
		toHand.add(new Card(6, "Hearts"));
		toHand.add(new Card(7, "Clubs"));
		toHand.add(new Card(7, "Hearts"));
		toHand.add(new Card(8, "Hearts"));
		toHand.add(new Card(7, "Diamonds"));
		Crib player1 = new Crib(toHand);
		score1 = player1.score();
		System.out.println(score1);
	}
	
	public void testRandomHand()
	{
		ArrayList<Card> toHand = new ArrayList<Card>();
		for(int i = 0; i < 5; i++)
			toHand.add(deck.deal());
		Hand player1 = new Hand(toHand);
		score1 = player1.score();
		System.out.println(score1);
	}
}
