package battlechallenge.bot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import battlechallenge.Coordinate;
import battlechallenge.ShipAction;
import battlechallenge.ShipIdentifier;
import battlechallenge.client.ClientGame;
import battlechallenge.ship.Ship;
import battlechallenge.ship.Ship.Direction;
import battlechallenge.structures.Base;
import battlechallenge.structures.City;

public class JoshuaBot extends ClientPlayer{
	
	private static List<Coordinate> nextCoord = new ArrayList<Coordinate>();
	private static int[][] goodness;
	private static int turnCounter = 0;
//	private static ArrayList<Coordinate> wallPos = new ArrayList<Coordinate>();

	public JoshuaBot(String playerName, int mapWidth, int mapHeight,
			int networkID) {
		super(playerName, mapWidth, mapHeight, networkID);
		// TODO Auto-generated constructor stub
	}
	
	public List<ShipAction> doTurn() {
		nextCoord.clear();
		if(turnCounter==0)
		{
			goodness = new int[ClientGame.getMap().getNumCols()][ClientGame.getMap().getNumRows()];
//			System.out.println(goodness.length);
//			System.out.println(goodness[1].length);
			for(int i = 0; i < ClientGame.getBarriers().size(); i++)
			{
//				wallPos.add(ClientGame.getBarriers().get(i).getLocation());
				goodness[ClientGame.getBarriers().get(i).getLocation().getCol()][ClientGame.getBarriers().get(i).getLocation().getRow()] = -1;
			}
			
			
			
//			for(int i = 0; i < ClientGame.getAllBases().size(); i++)
//			{
//				wallPos.add(ClientGame.getBarriers().get(i).getLocation());
				goodness[ClientGame.getMyBase().getLocation().getCol()][ClientGame.getMyBase().getLocation().getRow()] = -1;
//			}
			
		}
		
		for(City c : ClientGame.getAllCities())
			goodness[c.getLocation().getCol()][c.getLocation().getRow()] = 1000000;
		for(City c : ClientGame.getMyCities())
		{
			goodness[c.getLocation().getCol()][c.getLocation().getRow()] = 1000;
		}
//		for(Ship s : ClientGame.getOpponentShips())
//		{
//			goodness[s.getLocation().getCol()][s.getLocation().getRow()] = 1000;
//		}
		turnCounter++;
		/*if(turnCounter==20)
		{
			if(ClientGame.getMyBase().getLocation().getCol()>=31/2)
			{
				for(int l = 0; l < 5; l++)
				{
					for(int i = 0; i < goodness[1].length; i++)
					{
						for(int k = goodness.length-1; k >= goodness.length/2; k--)
						{
							if(goodness[k][i]==1000000)
							{
								continue;
							}
							if(goodness[k][i]==-1)
							{
								continue;
							}
							if(i==0&&k==0)
							{
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							}
							else if(i==0 && k==30)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else if(k==0 && i==18)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(i==18 && k==30)
								goodness[k][i]=(goodness[k-1][i]+goodness[k][i-1])/2;
							else if(i==0)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
							else if(k==0)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k][i-1])/3;
							else if(i==18)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1]+goodness[k-1][i])/3;
							else if(k==30)
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1]+goodness[k-1][i])/3;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/4;
						}
					}
				}
			}
			else
			{
				for(int l = 0; l < 5; l++)
				{
					for(int i = 0; i < goodness[1].length; i++)
					{
						for(int k = 0; k >= goodness.length/2; k++)
						{
							if(goodness[k][i]==1000000)
							{
								continue;
							}
							if(goodness[k][i]==-1)
							{
								continue;
							}
							if(i==0&&k==0)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							else if(i==0 && k==30)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else if(k==0 && i==18)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(i==18 && k==30)
								goodness[k][i]=(goodness[k-1][i]+goodness[k][i-1])/2;
							else if(i==0)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
							else if(k==0)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k][i-1])/3;
							else if(i==18)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1]+goodness[k-1][i])/3;
							else if(k==30)
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1]+goodness[k-1][i])/3;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/4;

						}
					}
				}
			}
		}*/
//		System.out.println(turnCounter);

		
		if(ClientGame.getMyBase().getLocation().getCol()>=31/2 || turnCounter>15)
		{
			if(turnCounter<=goodness.length || turnCounter<=goodness[1].length)
			{
				for(int i = 0; i < goodness[1].length; i++)
				{
					for(int k = goodness.length/2; k < goodness.length; k++)
					{
						if(goodness[k][i]==1000000)
						{
							continue;
						}
						if(goodness[k][i]==-1)
						{
							continue;
						}
						if(i==0&&k==0)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]);
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]);
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
						}
						else if(i==0 && k==goodness.length-1)
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k-1][i]);
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]);
							else
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
						}
						else if(k==0 && i==goodness[1].length-1)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]);
							else if(goodness[k][i-1] == -1)
								goodness[k][i]=(goodness[k+1][i]);
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
						}
						else if(i==goodness[1].length-1 && k==goodness.length-1)
						{
							if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]);
							else if(goodness[k][i-1] == -1)
								goodness[k][i]=(goodness[k-1][i]);
							else
								goodness[k][i]=(goodness[k-1][i]+goodness[k][i-1])/2;
						}
						else if(i==0)
						{
							// System.out.println(k);
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
							else if(goodness[k-1][i]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
						}
						else if(k==0)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k][i-1])/2;
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k][i-1])/3;
						}
						else if(i==goodness[1].length-1)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1]+goodness[k-1][i])/3;
						}
						else if(k==goodness.length-1)
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1]+goodness[k-1][i])/3;
						}
						else
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i]+goodness[k-1][i-1])/3;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i-1])/3;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
							else if(goodness[k+1][i]==-1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/3;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/4;
						}
					}
				}
			}
			else
			{
				for(int i = 0; i < goodness[1].length; i++)
				{
					for(int k = goodness.length/2; k < goodness.length; k++)
					{
						if(goodness[k][i]==1000000)
						{
							continue;
						}
						if(goodness[k][i]==-1)
						{
							continue;
						}
						if(i==0&&k==0)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]);
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]);
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
						}
						else if(i==0 && k==goodness.length-1)
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k-1][i]);
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]);
							else
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
						}
						else if(k==0 && i==goodness[1].length-1)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]);
							else if(goodness[k][i-1] == -1)
								goodness[k][i]=(goodness[k+1][i]);
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
						}
						else if(i==goodness[1].length-1 && k==goodness.length-1)
						{
							if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]);
							else if(goodness[k][i-1] == -1)
								goodness[k][i]=(goodness[k-1][i]);
							else
								goodness[k][i]=(goodness[k-1][i]+goodness[k][i-1])/2;
						}
						else if(i==0)
						{
							// System.out.println(k);
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
							else if(goodness[k-1][i]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
						}
						else if(k==0)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k][i-1])/2;
							else if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k][i-1])/3;
						}
						else if(i==goodness[1].length-1)
						{
							if(goodness[k+1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1]+goodness[k-1][i])/3;
						}
						else if(k==goodness.length-1)
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1])/2;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
							else
								goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1]+goodness[k-1][i])/3;
						}
						else
						{
							if(goodness[k][i+1] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i]+goodness[k-1][i-1])/3;
							else if(goodness[k-1][i] == -1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i-1])/3;
							else if(goodness[k][i-1]==-1)
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
							else if(goodness[k+1][i]==-1)
								goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/3;
							else
								goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/4;
						}
					}
				}
			}
		}
		if(ClientGame.getMyBase().getLocation().getCol()<=31/2 || turnCounter>15)
		{
			for(int i = 0; i < goodness[1].length; i++)
			{
				for(int k = goodness.length/2; k >= 0; k--)
				{
					if(goodness[k][i]==1000000)
					{
						continue;
					}
					if(goodness[k][i]==-1)
					{
						continue;
					}
					if(i==0&&k==0)
					{
						if(goodness[k+1][i] == -1)
							goodness[k][i]=(goodness[k][i+1]);
						else if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k+1][i]);
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
					}
					else if(i==0 && k==goodness.length-1)
					{
						if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k-1][i]);
						else if(goodness[k-1][i] == -1)
							goodness[k][i]=(goodness[k][i+1]);
						else
							goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
					}
					else if(k==0 && i==goodness[1].length-1)
					{
						if(goodness[k+1][i] == -1)
							goodness[k][i]=(goodness[k][i-1]);
						else if(goodness[k][i-1] == -1)
							goodness[k][i]=(goodness[k+1][i]);
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
					}
					else if(i==goodness[1].length-1 && k==goodness.length-1)
					{
						if(goodness[k-1][i] == -1)
							goodness[k][i]=(goodness[k][i-1]);
						else if(goodness[k][i-1] == -1)
							goodness[k][i]=(goodness[k-1][i]);
						else
							goodness[k][i]=(goodness[k-1][i]+goodness[k][i-1])/2;
					}
					else if(i==0)
					{
						// System.out.println(k);
						if(goodness[k+1][i] == -1)
							goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
						else if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
						else if(goodness[k-1][i]==-1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
					}
					else if(k==0)
					{
						if(goodness[k+1][i] == -1)
							goodness[k][i]=(goodness[k][i+1]+goodness[k][i-1])/2;
						else if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
						else if(goodness[k][i-1]==-1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1])/2;
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k][i-1])/3;
					}
					else if(i==goodness[1].length-1)
					{
						if(goodness[k+1][i] == -1)
							goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
						else if(goodness[k-1][i] == -1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1])/2;
						else if(goodness[k][i-1]==-1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i])/2;
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i-1]+goodness[k-1][i])/3;
					}
					else if(k==goodness.length-1)
					{
						if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k][i-1]+goodness[k-1][i])/2;
						else if(goodness[k-1][i] == -1)
							goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1])/2;
						else if(goodness[k][i-1]==-1)
							goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i])/2;
						else
							goodness[k][i]=(goodness[k][i-1]+goodness[k][i+1]+goodness[k-1][i])/3;
					}
					else
					{
						if(goodness[k][i+1] == -1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k-1][i]+goodness[k-1][i-1])/3;
						else if(goodness[k-1][i] == -1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i-1])/3;
						else if(goodness[k][i-1]==-1)
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i])/3;
						else if(goodness[k+1][i]==-1)
							goodness[k][i]=(goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/3;
						else
							goodness[k][i]=(goodness[k+1][i]+goodness[k][i+1]+goodness[k-1][i]+goodness[k-1][i-1])/4;
					}
				}
			}
		}
//		for(int i = 0; i < goodness[1].length; i++)
//		{
//			for(int k = 0; k < goodness.length; k++)
//			{
//				System.out.print(goodness[k][i]+"  ");
//			}
//			System.out.println();
//		}
//		
//		System.out.println();
//		System.out.println();



		List<ShipAction> actions = new LinkedList<ShipAction>();
		for (Ship s : ClientGame.getMyShips()) {
			actions.add(new ShipAction(s.getIdentifier(), shots(s), directions(s)));
		}
		return actions;

	}
	
	private Direction directions(Ship s){
		if(turnCounter>20)
		{
			for(City c : ClientGame.getMyCities())
			{
				if(c.getLocation().equals(s.getLocation()))
				{
					goodness[s.getLocation().getCol()][s.getLocation().getRow()] = 0;
					return Direction.STOP;
				}
			}
			
			int max = Integer.MIN_VALUE;
			Direction x = Direction.STOP;
			int check = 0;
			
			if(s.getLocation().getCol()-1>=0)
			{
				x = Direction.WEST;
				max = goodness[s.getLocation().getCol()-1][s.getLocation().getRow()];
				check = 1;
				
			}
			
			if(s.getLocation().getCol()+1<=goodness.length-1)
			if(goodness[s.getLocation().getCol()+1][s.getLocation().getRow()]>=max)
			{
				max = goodness[s.getLocation().getCol()+1][s.getLocation().getRow()];
				x = Direction.EAST;
				check = 2;
			}
			if(s.getLocation().getRow()-1>=0)
			if(goodness[s.getLocation().getCol()][s.getLocation().getRow()-1]>=max)
			{
				max = goodness[s.getLocation().getCol()][s.getLocation().getRow()-1];
				x = Direction.NORTH;
				check = 3;
			}
			if(s.getLocation().getRow()+1<=goodness[1].length-1)
			if(goodness[s.getLocation().getCol()][s.getLocation().getRow()+1]>=max)
			{
				max = goodness[s.getLocation().getCol()][s.getLocation().getRow()+1];
				x = Direction.SOUTH;
				check = 4;
			}
			
			int min = 0;
			
			switch (check) {
			case 0: goodness[s.getLocation().getCol()][s.getLocation().getRow()]=min;
				break;
			case 1: goodness[s.getLocation().getCol()-1][s.getLocation().getRow()]=min;
				break;
			case 2: goodness[s.getLocation().getCol()+1][s.getLocation().getRow()]=min;
				break;
			case 3: goodness[s.getLocation().getCol()][s.getLocation().getRow()-1]=min;
				break;
			case 4: goodness[s.getLocation().getCol()][s.getLocation().getRow()+1]=min;
				break;
			}
					
			return x;
		}
		else
		{
			for(City c : ClientGame.getMyCities())
				{
					if(c.getLocation().equals(s.getLocation()))
					{
						nextCoord.add(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()));
						return Direction.STOP;
					}
				}
				City temp = null;
				int closest = -1;
				for(City c : ClientGame.getAllCities())
				{
					if(closest == -1)
						closest = manhattanDistance(c.getLocation(),s.getLocation());
					if(temp == null || manhattanDistance(c.getLocation(),s.getLocation())<closest)
					{
						temp = c;
						closest = manhattanDistance(c.getLocation(),s.getLocation());
					}
				}
				
				if(temp.getLocation().getRow()-s.getLocation().getRow()>0 && !nextCoord.contains(new Coordinate(s.getLocation().getRow()+1, s.getLocation().getCol())))
				{
					nextCoord.add(new Coordinate(s.getLocation().getRow()+1, s.getLocation().getCol()));
					return Direction.SOUTH;
				}
				else if(temp.getLocation().getRow()-s.getLocation().getRow()<0 && !nextCoord.contains(new Coordinate(s.getLocation().getRow()-1, s.getLocation().getCol())))
				{
					nextCoord.add(new Coordinate(s.getLocation().getRow()-1, s.getLocation().getCol()));
					return Direction.NORTH;
				}
				else if(temp.getLocation().getCol()-s.getLocation().getCol()<0 && !nextCoord.contains(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()-1)))
				{
					nextCoord.add(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()-1));
					return Direction.WEST;
				}
				else if(temp.getLocation().getCol()-s.getLocation().getCol()>0 && !nextCoord.contains(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()+1)))
				{
					nextCoord.add(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()+1));
					return Direction.EAST;
				}
				
				nextCoord.add(new Coordinate(s.getLocation().getRow(), s.getLocation().getCol()));
				return Direction.STOP;
		}
	}
	
	
	
	public int manhattanDistance(Coordinate c1, Coordinate c2)
	{
		return Math.abs(c1.getRow() - c2.getRow()) + Math.abs(c1.getCol() - c2.getCol());
	}
	
	public Coordinate shots(Ship s)
	{
		for(Ship e : ClientGame.getOpponentShips())
		{
			if(e.getLocation().distanceTo(s.getLocation())<=s.getRange()+2)
			{
				City temp = null;
				int closest = -1;
				for(City c : ClientGame.getAllCities())
				{
					if(c.getLocation().equals(e.getLocation()))
						return e.getLocation();
					
					if(closest == -1)
						closest = manhattanDistance(c.getLocation(),e.getLocation());
					if(temp == null || manhattanDistance(c.getLocation(),e.getLocation())<closest)
					{
						temp = c;
						closest = manhattanDistance(c.getLocation(),e.getLocation());
					}
				}
				if(temp.getLocation().getCol()-e.getLocation().getCol()<0)
					return new Coordinate(e.getLocation().getRow(), e.getLocation().getCol()-1);
				else if(temp.getLocation().getCol()-e.getLocation().getCol()>0)
					return new Coordinate(e.getLocation().getRow(), e.getLocation().getCol()+1);
				else if(temp.getLocation().getRow()-e.getLocation().getRow()>0)
					return new Coordinate(e.getLocation().getRow()+1, e.getLocation().getCol());
				else if(temp.getLocation().getRow()-e.getLocation().getRow()<0)
					return new Coordinate(e.getLocation().getRow()-1, e.getLocation().getCol());
				return e.getLocation();
					
			}
		}
		return null;
	}

}
