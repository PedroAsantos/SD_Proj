package src;

import java.util.*;

public class Repository {

	private int[] horsesFinalPos;
	private	HashMap<Integer,List<double[]>> spectatorBets;
	private List<Integer> horsesRunnning;
	private List<Integer> bestofTheBests;
	private HashMap<Integer,Integer> horsePerformance;
	private int numberOfSpectators;
	private int numberOfRaces;
	private int horsesPerRace;
	public Repository(int totalHorses, int numberOfSpectators,int numberOfRaces, int horsesPerRace){
		this.horsesFinalPos = new int[totalHorses];
		this.spectatorBets = new HashMap<Integer,List<double[]>>(numberOfSpectators);
		this.bestofTheBests = new ArrayList<Integer>(totalHorses);
		horsePerformance = new HashMap<Integer,Integer>();
		this.numberOfRaces=numberOfRaces;
		this.horsesPerRace=horsesPerRace;
		this.horsesRunnning = new ArrayList<Integer>(horsesPerRace);
	}
	public boolean addHorsesToRun(int horseId) {
		if(horsesRunnning.size()==horsesPerRace) {
			return false;
		}
		horsesRunnning.add(horseId);
		return true;
		
	}
	public List<Integer> getHorsesRunning(){
		return horsesRunnning;
	}
	public void clearhorsesRunning() {
		horsesRunnning.clear();
	}
	public int[] gethorsesFinalPos(){
		return this.horsesFinalPos;
	}
	public int getNumberOfSpectators() {
		return numberOfSpectators;
	}
	public void sethorsesFinalPos(int[] horsesFinalPos){
		this.horsesFinalPos = horsesFinalPos;
	}
	public int getNumberOfRaces() {
		return numberOfRaces;
	}
	public void raceDone() {
		numberOfRaces--;
	}
	public HashMap<Integer,List<double[]>> getspectatorBets(){
		return this.spectatorBets;
	}

	public void setspectatorBets(HashMap<Integer,List<double[]>> spectatorBets){
		this.spectatorBets = spectatorBets;
	}
	
	public HashMap<Integer,List<int[]>> getspectatorBets(HashMap<Integer,List<int[]>> spectatorBets){
		return spectatorBets;
	}

	public List<Integer> getbestofTheBests(){
		return this.bestofTheBests;
	}

	public void setbestofTheBests(List<Integer> bestofTheBests){
		this.bestofTheBests = bestofTheBests;
	}

	public HashMap<Integer,Integer> gethorsePerformance(){
		return this.horsePerformance;
	}

	public void sethorsePerformance(HashMap<Integer,Integer> horsePerformance){
		this.horsePerformance = horsePerformance;
	}

}