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

	public void writeLog(){
		String header = "         AFTERNOON AT THE RACE TRACK - "
                + "Description of the internal state of the problem\n";
		String header1 = "MAN/BRK           SPECTATOR/BETTER              "
                    + "HORSE/JOCKEY PAIR AT RACE" + " " + numberOfRaces;
		String header2 = "  Stat  St0  Am0 St1  Am1 St2  Am2 St3  Am3 RN "
                    + "St0 Len0 St1 Len1 St2 Len2 St3 Len3";
		String header3 = "                                        "
                    + "RACE" + " " + numberOfRaces + " " + "Status";
		String header4 = " RN Dist BS0  BA0 BS1  BA1 BS2  BA2 BS3  BA3   Od0 "
                    + "N0 PS0 SD0 Od1 N1 PS1 SD1 Od2 N2 PS2 SD2 Od3 N3 PS3 SD3";          
		
		File file = new File("log.txt");
		try{
			PrintWriter writer = new PrintWriter(file);
			writer.println(header);
			writer.println(header1);
			writer.println(header2);
			writer.println(header3);
			writer.println(header4);
			writer.close();
		}catch (FileNotFoundException ex)  
	    {
	        System.err.println("Error writing");
	    }

	}

	public void toLog(){
		
		try{
			PrintWriter writer = new PrintWriter(new FileOutputStream(
				new File("log.txt"),
				true));
			String stat="";
			if (brokerstate != null) {
				stat=""+brokerstate;	
			}
			else{
				stat="-";
			}
			
			String[] specStAll = new String[numberOfSpectators];
			for (int i = 0;i<numberOfSpectators; i++ ) {
				if (specStat.get(i)!=null) {
					specStAll[i]=""+specStat.get(i);	
				}else{
					specStAll[i]="-";
				}
	
			
			}
			
			double[] specAmAll = new double[numberOfSpectators];
			for (int i = 0;i<numberOfSpectators; i++ ) {
				if(specMoney.get(i)!=null){
					specAmAll[i]=specMoney.get(i);
					//System.out.println("money_"+i+" "+specMoney.get(i));
				}else{
					specAmAll[i]=0.0;
				}

			}
			
			//VER ISTO
		//	int rn=numberOfRaces;
			
			String[] horseStAll = new String[horsesPerRace];
			if(horsesRunnning.size()>0) {
			for (int i = 0;i<horsesPerRace; i++ ) {
				if (i<horsesRunnning.size() && horseStat.get(horsesRunnning.get(i))!=null) {
					horseStAll[i]=""+horseStat.get(horsesRunnning.get(i));	
				}
				else{
					horseStAll[i]="AT_THE_STABLE";
				}
			}
			}else {
				for (int i = 0; i < horseStAll.length; i++) {
					horseStAll[i]="AT_THE_STABLE";
				}
			}
			
			
			int[] lenAll = new int[horsesPerRace];
			for (int i = 0;i<horsesPerRace; i++ ) {
				if(i<horsesRunnning.size() && horsePerformance.get(horsesRunnning.get(i))!=null){
					lenAll[i]=horsePerformance.get(horsesRunnning.get(i));
				}else{
					lenAll[i]=0;
				}
			}
			
		
			
			int dist=raceLength;
			
			String[] bsAll = new String[numberOfSpectators];
			for (int i = 0;i<numberOfSpectators; i++ ) {
				if(specbets.get(i)!=null){
					bsAll[i]=""+specbets.get(i);
					//System.out.println(bsAll[i]);
				}else{
					bsAll[i]="-";
				}
								
			}
			double[] baAll = new double[numberOfSpectators];
			for (int i = 0;i<numberOfSpectators; i++ ) {
			
				if (specbetamount.get(i) != null) {
					baAll[i]=specbetamount.get(i);	
				}else{
					baAll[i]=0.0;
				}
			

			}
			double[] odAll = new double[horsesPerRace];
			for (int i = 0;i<horsesPerRace; i++ ) {
				if (i<horsesRunnning.size() && horseProbabilities.get(horsesRunnning.get(i))!=null) {
					odAll[i]=horseProbabilities.get(horsesRunnning.get(i));	

				}else{
					odAll[i]=0.0;
				}

			}

			
			int[] nAll = new int[horsesPerRace];
			for (int i = 0;i<horsesPerRace; i++ ) {
				if (i<horsesRunnning.size() && horseruns.get(horsesRunnning.get(i))!=null) {
					nAll[i]=horseruns.get(horsesRunnning.get(i));	
				}else{
					nAll[i]=0;
				}

			}

			int[] psAll = new int[horsesPerRace];
			for (int i = 0;i<horsesPerRace; i++ ) {
				if(i<horsesRunnning.size() && horseposition.get(horsesRunnning.get(i))!=null){
					psAll[i]=horseposition.get(horsesRunnning.get(i));
				}else{
					psAll[i]=0;
				}
			}
			//NAO TA BEM!!!!!!!!!!!!
			String[] sdAll = new String[horsesPerRace];
			if (horsesRunnning.size()>0) {
				for (int i = 0;i<horsesPerRace; i++ ) {
					
					if(i<horsesRunnning.size() && horserank.get(horsesRunnning.get(i))==null){
						sdAll[i]="-";
						//sdAll[i]=""+horserank.get(i);
					}
					else{
						if(i<horsesRunnning.size() && horserank.containsKey(horsesRunnning.get(i))) {
							sdAll[i]=""+horserank.get(horsesRunnning.get(i));
						}else {
							sdAll[i]="-";
						}
					}
					//System.out.println("++"+sdAll[i]+"i"+i);
				}	
			}else{
				for (int i = 0;i<horsesPerRace; i++ ) {
					sdAll[i]="-";
					
				}
			}
			
			
			//System.out.println(bestofTheBests);

			l = "  " + stat+ "  " + 
                specStAll[0] + "  " + 
                String.format("%.2f", specAmAll[0]) + " " + 
                specStAll[1] + "  " + 
                String.format("%.2f", specAmAll[1]) + " " + 
                specStAll[2] + "  " + 
                String.format("%.2f", specAmAll[2]) + " " + 
                specStAll[3] + "  " + 
                String.format("%.2f", specAmAll[3]) + "  " + 
                numberOfRaces + " " + horseStAll[0] + 
                "  " + String.format("%02d", lenAll[0]) + "  " + 
                horseStAll[1] + "  " + 
                String.format("%02d", lenAll[1]) + "  " + 
                horseStAll[2] + "  " + 
                String.format("%02d", lenAll[2]) + "  " + 
                horseStAll[3] + "  " + 
                String.format("%02d", lenAll[3]);
			
			/*if (horserank.size()==4) {
				l1 = "  " + numberOfRaces + "  " + dist + "   " + bsAll[0] 
                + "  " + String.format("%.2f", baAll[0]) + "  " + 
                bsAll[1] + "  " + 
                String.format("%.2f", baAll[1]) + "  " + 
                bsAll[2] + "  " + 
                String.format("%.2f", baAll[2]) + "  " + 
                bsAll[3] + "  " + 
                String.format("%.2f", baAll[3]) + "  " + 
                String.format("%.2f", odAll[0]) + " " + 
                String.format("%02d", nAll[0]) + "  " + 
                String.format("%02d", psAll[0]) + "  " +
                sdAll[horsesRunnning.get(0)] + " " + 
                String.format("%.2f", odAll[1]) + " " + 
                String.format("%02d", nAll[1]) + "  " + 
                String.format("%02d", psAll[1]) + "  " +
                sdAll[horsesRunnning.get(1)] + " " + 
                String.format("%.2f", odAll[2]) + " " + 
                String.format("%02d", nAll[2]) + "  " + 
                String.format("%02d", psAll[2]) + "  " +
                sdAll[horsesRunnning.get(2)] + " " + 
                String.format("%.2f", odAll[3]) + " " + 
                String.format("%02d", nAll[3]) + "  " + 
                String.format("%02d", psAll[3]) + "  " +
                sdAll[horsesRunnning.get(3)];	
			}
            else{*/
            	l1 = "  " + numberOfRaces + "  " + dist + "   " + bsAll[0] 
                + "  " + String.format("%.2f", baAll[0]) + "  " + 
                bsAll[1] + "  " + 
                String.format("%.2f", baAll[1]) + "  " + 
                bsAll[2] + "  " + 
                String.format("%.2f", baAll[2]) + "  " + 
                bsAll[3] + "  " + 
                String.format("%.2f", baAll[3]) + "  " + 
                String.format("%.2f", odAll[0]) + " " + 
                String.format("%02d", nAll[0]) + "  " + 
                String.format("%02d", psAll[0]) + "  " +
                sdAll[0] + " " + 
                String.format("%.2f", odAll[1]) + " " + 
                String.format("%02d", nAll[1]) + "  " + 
                String.format("%02d", psAll[1]) + "  " +
                sdAll[1] + " " + 
                String.format("%.2f", odAll[2]) + " " + 
                String.format("%02d", nAll[2]) + "  " + 
                String.format("%02d", psAll[2]) + "  " +
                sdAll[2] + " " + 
                String.format("%.2f", odAll[3]) + " " + 
                String.format("%02d", nAll[3]) + "  " + 
                String.format("%02d", psAll[3]) + "  " +
                sdAll[3];
            //}
            
            writer.println(l);
            writer.println(l1);
            writer.println("----------------------------------------------------------");
			writer.close();
		}catch(FileNotFoundException ex){
			System.err.println("Error Writing");
		}
		
	}


	public boolean addHorsesToRun(int horseId) {
		boolean boo=true;
		mutex.lock();
		try {
			if(horsesRunnning.size()==horsesPerRace) {
				boo=false;
			}
			horsesRunnning.add(horseId);
		} finally {
			mutex.unlock();
		}
		return boo;
		
	}
	public void clearhorsesRunning() {
		horsesRunnning.clear();
	}
	/*public int[] gethorsesFinalPos(){
		return this.horsesFinalPos;
	}*/
	public int getNumberOfSpectators() {
		return numberOfSpectators;
	}
	/*
	public void sethorsesFinalPos(int[] horsesFinalPos){
		this.horsesFinalPos = horsesFinalPos;
	}*/
	
	public int getNumberOfRaces() {
		return numberOfRaces;
	}
	public int getNumberOfRacesMissing() {
		return numberOfRacesMissing;
	}
	public void raceStarted() {
		numberOfRacesMissing--;
	}
	public void raceDone() {
		numberOfRaces--;
	}
	
	/*public void setspectatorBets(HashMap<Integer,List<double[]>> spectatorBets){
		this.spectatorBets = spectatorBets;
	}*/

	public void setSpecStat(int spectator_id, SpectatorState state){
		mutex.lock();
		try {
			this.specStat.put(spectator_id,state);
		} finally {
			mutex.unlock();
		}
		
		//toLog();
	}

	public void setHorseStat(int horse_id, HorseState state){
		mutex.lock();
		try {
			this.horseStat.put(horse_id,state);
		} finally {
			mutex.unlock();
		}
		
		//toLog();
	}

	public void sethorseruns(int horse_id, int runs){
		mutex.lock();
		try {
			this.horseruns.put(horse_id,runs);
		} finally {
			mutex.unlock();
		}
		
		toLog();
	}

	public void sethorseposition(int horse_id, int position){
		mutex.lock();
		try {
			this.horseposition.put(horse_id,position);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	public void setspecbetamount(int spectator_id, double amount){
		mutex.lock();
		try {
			this.specbetamount.put(spectator_id,amount);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	public void setspecMoney(int spectator_id, double money){
		mutex.lock();
		try {
			this.specMoney.put(spectator_id,money);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	public BrokerState getbrokerstate(){
		return brokerstate;
	}

	public void setbrokerstate(BrokerState brokerstate){
		this.brokerstate=brokerstate;
		//toLog();
	}

	public void setspecbets(int spec_id,int horse_id){
		mutex.lock();
		try {
			this.specbets.put(spec_id,horse_id);	
		} finally {
			mutex.unlock();
		}
		toLog();
	}


	public void sethorserank(int horse_id, int rank){
		mutex.lock();
		try {
			this.horserank.put(horse_id,rank);
		} finally {
			mutex.unlock();
		}
		toLog();
	}

	public void clearhorserank() {
		this.horserank.clear();
		
	}
	public int getHorsesPerRace() {
		return this.horsesPerRace;
	}
	public int getTotalHorses() {
		return this.totalHorses;
	}
}