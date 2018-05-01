package sharingRegions;



import Enum.*;

public class Repository {

	public Repository(){

	}
	/**
	*	Function to write in the header of the log.
	*/
	public void writeLog(){
	

	}
	/**
	*	Function to write in the log files the update values.
	*
	*/
	public void toLog(){
		
	}

	/**
	*	Function to add the horses that are running or they will run in the next race.
	*   @param horseId Horse ID 
	*   @return boolean return false if the race is already full.
	*/
	public boolean addHorsesToRun(int horseId) {
		return true;
		
	}
	/**
	*	Function to remove all the horses that were running.
	*
	*/
	public void clearhorsesRunning() {
		
	}
	/*public int[] gethorsesFinalPos(){
		return this.horsesFinalPos;
	}*/
	/**
	*	Function to know the number of spectors in the event.
	*   @return int the number of spectators.
	*/
	public int getNumberOfSpectators() {
		return 1001;
	}
	/*
	public void sethorsesFinalPos(int[] horsesFinalPos){
		this.horsesFinalPos = horsesFinalPos;
	}*/
	/**
	*	Function to return the number of races.
	*
	* 	@return int the number of races.
	*/
	public int getNumberOfRaces() {
		return 1001;
	}
	/**
	*	Function to return the number of races that left.
	*
	* 	@return int the number of races missing.
	*/
	public int getNumberOfRacesMissing() {
		return 1001;
	}
	/**
	*	Function to update the number of races that are missing.
	*
	* 	
	*/
	public void raceStarted() {
		
	}
	/**
	*	Function to update the number of races that were made.
	*	
	*/
	public void raceDone() {
		
	}
	
	/*public void setspectatorBets(HashMap<Integer,List<double[]>> spectatorBets){
		this.spectatorBets = spectatorBets;
	}*/
	/**
	*	Function to update the state of the spectator_id.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param state Spectator State
	*/
	public void setSpecStat(int spectator_id, SpectatorState state){
		
		
	}
	
	/**
	*	Function to update the probabilitie of the given horse winning.
	*
	* 	@param horse_id Horse ID.
	* 	@param probabilitie Horse winning probabilitie.
	*/
	public void setHorseProbabilitie(int horse_id,double probabilitie) {
		
	}
	/**
	*	Function to update the state of the horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param state Horse state.
	*/
	public void setHorseStat(int horse_id, HorseState state){
	
	
	}
	/**
	*	Function to update the number of runs of a horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param runs Runs of the horse.
	*/
	public void sethorseruns(int horse_id, int runs){
		
	}
	public void setHorsePerformance(int horse_id, int performance) {
		
	}
	/**
	*	Function to update the position of the horse.
	*
	* 	@param horse_id Horse ID.
	* 	@param position Horse position.
	*/
	public void sethorseposition(int horse_id, int position){
		
	}
	/**
	*	Function to update the money that a spectator put on a bet.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param amount bet amount.
	*/
	public void setspecbetamount(int spectator_id, double amount){
		
	}
	/**
	*	Function to update the money that a spectator has.
	*
	* 	@param spectator_id Spectator ID.
	* 	@param money Money of the specator.
	*/
	public void setspecMoney(int spectator_id, double money){
		
	}

/*	public BrokerState getbrokerstate(){
		return brokerstate;
	}*/
	/**
	*	Function to update the broker state
	*
	* 	@param brokerstate state.
	* 	
	*/
	public void setbrokerstate(BrokerState brokerstate){
		
	}
	/**
	*	Function to update the horse that a given spectator bet.
	*
	* 	@param spec_id Spectator ID.
	* 	@param horse_id Horse picked.
	*/
	public void setspecbets(int spec_id,int horse_id){
		
	}

	/**
	*	Function to update the horse place on the end of the race.
	*
	* 	@param horse_id Spectator ID.
	* 	@param rank Horse place.
	*/
	public void sethorserank(int horse_id, int rank){
	}
	/**
	*	Function to clear the horse place on the end of the race.
	*
	*/
	public void clearhorserank() {
		
		
	}
	/**
	*	Function to return the number of horses per race.
	*   @return int Horses in each race.
	*/
	public int getHorsesPerRace() {
		return 1001;
	}
	/**
	*	Function to return the number of total horses in the event.
	*   @return int Horses in the event.
	*/
	public int getTotalHorses() {
		return 1001;
	}
}