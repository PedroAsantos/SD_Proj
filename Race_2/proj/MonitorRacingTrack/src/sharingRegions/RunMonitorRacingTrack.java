package sharingRegions;

public class RunMonitorRacingTrack {
	public static void main(String[] args) {
		
		int numberOfHorses = 20; //testar com numeros maiores.
		int numberOfSpectators=4;
		int numberOfRaces=5;
		int horsesPerRace=4;
		int raceLength=30;
		int maxPerformance=10;
		Repository repo = new Repository(numberOfHorses,numberOfSpectators,numberOfRaces,horsesPerRace,raceLength);
		repo.writeLog();

		MonitorBettingCenter mBettingCenter = new MonitorBettingCenter(repo);
		MonitorControlCenter mControlCenter = new MonitorControlCenter(repo);
		MonitorPaddock mPaddock = new MonitorPaddock(repo);
		MonitorRacingTrack mRacingTrack = new MonitorRacingTrack(raceLength, repo);
		MonitorStable mStable = new MonitorStable(repo);
		
		
	}
}
