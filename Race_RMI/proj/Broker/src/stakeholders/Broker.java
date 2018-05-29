package stakeholders;
 
import Enum.BrokerState;
import Interfaces.IBroker_BettingCenter;
import Interfaces.IBroker_Control;
import Interfaces.IBroker_Paddock;
import Interfaces.IBroker_Stable;
import Interfaces.IBroker_Track;
import Interfaces.IMonitor_Paddock;
import Interfaces.IRepository;
import sharingRegions.*;

public class Broker extends Thread {
    private volatile boolean running = true;
       
    private BrokerState state;
    private final IBroker_Control monitorControl;
    private final IBroker_BettingCenter monitorBettingCenter;
    private final IBroker_Stable monitorStable;
    private final IBroker_Track monitorTrack;
    private final IMonitor_Paddock monitorPaddock;
    IRepository repo;
   
    public Broker(IBroker_Control mControl, IBroker_BettingCenter mBettingCenter, IBroker_Stable monitorStable, IBroker_Track monitorTrack, IMonitor_Paddock monitorPaddock,IRepository repo) {
        this.monitorControl=mControl;
        this.monitorBettingCenter=mBettingCenter;
        this.monitorStable = monitorStable;
        this.monitorTrack = monitorTrack;
        this.monitorPaddock=monitorPaddock;
        this.state = BrokerState.OPENING_THE_EVENT;
        this.repo=repo;
    }
   
   
   
    @Override
    public void run() {
           
 
        while(running) {
            try {
               switch (state) {
                case OPENING_THE_EVENT:
                    repo.setbrokerstate(state);
                    System.out.print("OPENING_THE_EVENT \n");
                    monitorStable.summonHorsesToPaddock();
                    state=BrokerState.ANNOUNCING_NEXT_RACE;
                //  state=BrokerState.PLAYING_HOST_AT_THE_BAR;
                    repo.toLog();
                    break;
                case ANNOUNCING_NEXT_RACE:
                    System.out.print("ANNOUNCING_NEXT_RACE\n");
                    repo.setbrokerstate(state);
                    monitorBettingCenter.acceptTheBets();
                    state=BrokerState.WAITING_FOR_THE_BETS;
                    repo.toLog();
                    break;
                case WAITING_FOR_THE_BETS:
               	   System.out.print("WAITING_FOR_THE_BETS\n");
                    repo.setbrokerstate(state);
                    monitorTrack.startTheRace();
                    state=BrokerState.SUPERVISING_THE_RACE;
                    repo.toLog();
                    break;
                case SUPERVISING_THE_RACE:
               	   System.out.print("SUPERVISING_THE_RACE\n");
                    repo.setbrokerstate(state);
                    //passar do track para o control que cavalos ganharam!
                    int[] horseAWinners;
                    horseAWinners = monitorTrack.reportResults();   
                    monitorControl.reportResults(horseAWinners);    
                    if(monitorBettingCenter.areThereAnyWinners(horseAWinners)) {
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
               	   System.out.print("SETTING_ACCOUNTS\n");
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
                	System.out.print("PLAYING_HOST_AT_THE_BAR\n");
                    repo.setbrokerstate(state);
                    System.out.println("EVENT END");
                    //monitorStable.turnOffServer();
                    //monitorControl.turnOffServer();
                    //monitorTrack.turnOffServer();
                    //monitorBettingCenter.turnOffServer();
                    //monitorPaddock.turnOffServer();
                    //repo.turnOffServer();
                    System.out.println("ALL DEAD");
                    stopRunning();
                    repo.toLog();
                    break;
                default:
                    break;
            } 
            } catch (Exception e) {
                e.printStackTrace();
            }
            
          /*  try {
                Thread.sleep(500);
            } catch(Exception e) {
               
            }*/

        }
    }
   
    public void stopRunning(){
        running = false;
        System.out.println("Broker exit");
    }
 
}