package stakeholders;
 
import java.util.List;
 
import Enum.BrokerState;
import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import sharingRegions.*;

public class Broker extends Thread {
    private volatile boolean running = true;
       
    private BrokerState state;
    private final IBroker_Control monitorControl;
    private final IBroker_BettingCenter monitorBettingCenter;
    private final IBroker_Stable monitorStable;
    private final IBroker_Track monitorTrack;
    Repository repo;
   
    public Broker(IBroker_Control mControl, IBroker_BettingCenter mBettingCenter, IBroker_Stable monitorStable, IBroker_Track monitorTrack, Repository repo) {
        this.monitorControl=mControl;
        this.monitorBettingCenter=mBettingCenter;
        this.monitorStable = monitorStable;
        this.monitorTrack = monitorTrack;
        this.state = BrokerState.OPENING_THE_EVENT;
        this.repo=repo;
    }
   
   
   
    @Override
    public void run() {
           
 
        while(running) {
            switch (state) {
                case OPENING_THE_EVENT:
                    repo.setbrokerstate(state);
                    System.out.print("Opening the event \n");
                    monitorStable.summonHorsesToPaddock();
                    state=BrokerState.ANNOUNCING_NEXT_RACE;
                    repo.toLog();
                    break;
                case ANNOUNCING_NEXT_RACE:
                    repo.setbrokerstate(state);
                    monitorBettingCenter.acceptTheBets();
                    state=BrokerState.WAITING_FOR_THE_BETS;
                    repo.toLog();
                    break;
                case WAITING_FOR_THE_BETS:
                    repo.setbrokerstate(state);
                    monitorTrack.startTheRace();
                    state=BrokerState.SUPERVISING_THE_RACE;
                    repo.toLog();
                    break;
                case SUPERVISING_THE_RACE:
                    repo.setbrokerstate(state);
                    //passar do track para o control que cavalos ganharam!
                    List<Integer> horseWinners;
                    horseWinners = monitorTrack.reportResults();   
                    monitorControl.reportResults(horseWinners);    
                    if(monitorBettingCenter.areThereAnyWinners(horseWinners)) {
                        monitorBettingCenter.honourTheBets();
                        state=BrokerState.SETTING_ACCOUNTS;
                    }else {
                    	 if(repo.getNumberOfRacesMissing()==0) {
                             monitorControl.entertainTheGuests();
                             monitorStable.summonHorsesToEnd();
                             state=BrokerState.PLAYING_HOST_AT_THE_BAR;
                         }else {
                             repo.clearhorsesRunning();
                             repo.raceDone();
                             monitorStable.summonHorsesToPaddock();
                             state=BrokerState.ANNOUNCING_NEXT_RACE;
                         }
                    }
                    repo.toLog();
                    break;
                case SETTING_ACCOUNTS:
                    repo.setbrokerstate(state);
                    if(repo.getNumberOfRacesMissing()==0) {
                        monitorControl.entertainTheGuests();
                        monitorStable.summonHorsesToEnd();
                        state=BrokerState.PLAYING_HOST_AT_THE_BAR;
                    }else {
                        repo.clearhorsesRunning();
                        repo.raceDone();
                        monitorStable.summonHorsesToPaddock();
                        state=BrokerState.ANNOUNCING_NEXT_RACE;
                    }
                    repo.toLog();
                    break;
                case PLAYING_HOST_AT_THE_BAR:
                    repo.setbrokerstate(state);
                    System.out.println("EVENT END");
                    stopRunning();
                    repo.toLog();
                    break;
                default:
                    break;
            }
            try {
                Thread.sleep(500);
            } catch(Exception e) {
               
            }

        }
    }
   
    public void stopRunning(){
        running = false;
        System.out.println("Broker exit");
    }
 
}