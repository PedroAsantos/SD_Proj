package sharingRegions;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.io.*;

import Enum.*;

public class Repository {
	private final ReentrantLock mutex;

	//private int[] horsesFinalPos;
	private List<Integer> horsesRunnning;
	//private HashMap<Integer,Integer> horsePerformance;
	private int numberOfSpectators;
	//private int numberOfRaces;
	private int horsesPerRace;
	private int totalHorses;
	private String l;
	private String l1;
	private int numberOfRacesMissing;
	
	//Stat - broker state - Broker Class
	private BrokerState brokerstate;
	//St# - spectator/better state (# - 0 .. 3) - Spectator Class
	private	HashMap<Integer,SpectatorState> specStat;
	//Am# - spectator/better amount of money she has presently (# - 0 .. 3) - 
	private	HashMap<Integer,Double> specMoney;
	//RN - race number - RunRace -----------------------Ta mal
	private int numberOfRaces;
	//St# - horse/jockey pair state in present race (# - 0 .. 3)
	private	HashMap<Integer,HorseState> horseStat;
	//Len# - horse/jockey pair maximum moving length per iteration step in present race (# - 0 .. 3) - Hash Map para isto
	private	HashMap<Integer,Integer> horsePerformance;
	//Dist - race track distance in present race - MonitorRacingTrack
	private int raceLength;
	//BS# - spectator/better bet selection in present race (# - 0 .. 3) - MonitorBettingCenter
	private	HashMap<Integer,Integer> specbets;
	//BA# - spectator/better bet amount in present race (# - 0 .. 3)
	private	HashMap<Integer,Double> specbetamount;
	//Od# - horse/jockey pair winning probability in present race (# - 0 .. 3)
	private HashMap<Integer,Double> horseProbabilities;
	//N# - horse/jockey pair iteration step number in present race (# - 0 .. 3) - Hash Map para isto
	private	HashMap<Integer,Integer> horseruns;
	//Ps# - horse/jockey pair track position in present race (# - 0 .. 3) - Hash Map para isto ????????????
	private	HashMap<Integer,Integer> horseposition;
	//SD# - horse/jockey pair standing at the end of present race (# - 0 .. 3)
	private HashMap<Integer,Integer> horserank;
	//winnerHoreses
	public Repository(int totalHorses, int numberOfSpectators,int numberOfRaces, int horsesPerRace, int raceLength){
		mutex = new ReentrantLock(true);
	//	this.horsesFinalPos = new int[totalHorses];
		horsePerformance = new HashMap<Integer,Integer>();
		this.numberOfRaces=numberOfRaces;
		this.numberOfRacesMissing=numberOfRaces;
		this.totalHorses=totalHorses;
		this.horsesPerRace=horsesPerRace;
		this.numberOfSpectators=numberOfSpectators;
		this.horsesRunnning = new ArrayList<Integer>(horsesPerRace);
		this.brokerstate = BrokerState.OPENING_THE_EVENT;
		this.specStat = new HashMap<Integer,SpectatorState>();
		this.specMoney = new HashMap<Integer,Double>();
		this.horseStat = new HashMap<Integer,HorseState>();
		this.specbetamount = new HashMap<Integer,Double>();
		this.horseProbabilities = new HashMap<Integer,Double>();
		this.horseruns = new HashMap<Integer,Integer>();
		this.horseposition = new HashMap<Integer,Integer>();
		this.specbets = new HashMap<Integer,Integer>();
		this.horserank = new HashMap<Integer,Integer>();
		this.raceLength=raceLength;
	}
	
	
	/**
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() {
		return numberOfSpectators;
	}
	/**
	*	Function to update the money that a spectator put on a bet.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param amount bet amount.
	*/
	public void setspecbetamount(int spectator_id, double amount){
		mutex.lock();
		try {
			this.specbetamount.put(spectator_id,amount);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	/**
	*	Function to update the horse that a given spectator bet.
	*
	* 	@param spec_id Spectator ID.
	* 	@param horse_id Horse picked.
	*/
	public void setspecbets(int spec_id,int horse_id){
		mutex.lock();
		try {
			this.specbets.put(spec_id,horse_id);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	/**
	*	Function to clear the horse place on the end of the race.
	*
	*/
	public void clearhorserank() {
		this.horserank.clear();
		
	}
	
}