import Enum.HorseState;
import src.*;
import Enum.SpectatorState;

public class Spectator extends Thread {
	private final int id;
	private final ISpectator_BettingCenter monitorBettingCenter;
	private final ISpectator_Control monitorControl;
	private final ISpectator_Paddock monitorPaddock;
	private SpectatorState state;
	public Spectator(int id,ISpectator_BettingCenter monitorBettingCenter, ISpectator_Control monitorControl, ISpectator_Paddock monitorPaddock) {
		this.id=id;
		this.monitorBettingCenter = monitorBettingCenter;
		this.monitorControl = monitorControl;
		this.monitorPaddock=monitorPaddock;
		this.state= SpectatorState.WAITING_FOR_A_RACE_TO_START;
		
	}
	
	@Override
	public void run() {
			
		while(true) {
			//monitor.divide();	
			switch (state) {
				case WAITING_FOR_A_RACE_TO_START:
					monitorPaddock.goCheckHorses(id);
					
					state=SpectatorState.APPRAISING_THE_HORSES;
					break;
				case APPRAISING_THE_HORSES:
					
					state=SpectatorState.PLACING_A_BET;
					break;
				case PLACING_A_BET:
					
					break;
				case WATCHING_THE_RACE:
					
					break;
				case COLLECTING_THE_GAINS:
					
					break;
				case CELEBRATING:
					
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
}
