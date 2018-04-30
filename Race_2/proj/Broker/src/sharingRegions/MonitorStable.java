package sharingRegions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import Interfaces.IBroker_Stable;
import Interfaces.IHorse_Stable;
import communication.Stub;

public class MonitorStable implements IHorse_Stable, IBroker_Stable {
	private final ReentrantLock mutex;
	private final Condition horse_condition;
	private final Condition broker_condition;

	private boolean goingToPaddock;
	private int horsesAtStable;
	private int horsesPerRace;
	private int totalHorses;
	private int horsesPaddock;
	private boolean horseCanNotGo;
	Repository repo;

	public MonitorStable(Repository repo) {
		mutex = new ReentrantLock(true);
		horse_condition = mutex.newCondition();
		broker_condition = mutex.newCondition();
		horsesAtStable = 0;
		horseCanNotGo = true;
		this.totalHorses = repo.getTotalHorses();
		this.repo = repo;
		this.horsesPerRace = repo.getHorsesPerRace();
		this.horsesPaddock = 0;
		this.goingToPaddock = true;

	}

	/**
	 * Wait that all the horses arrives to stable to wake up the broker and then a
	 * returns an boolean that indicates if a horse is going to paddock
	 *
	 * @param horseId
	 *            Horse ID
	 * @return boolean goingToPaddock
	 */
	@Override
	public boolean proceedToStable(int horseId) {
		mutex.lock();
		try {
			horsesAtStable++;
			System.out.print("Horse_" + horseId + " now on stable!\n");

			if (horsesAtStable == totalHorses) {
				broker_condition.signal();
			}
			while (horseCanNotGo) {
				try {
					horse_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			horsesAtStable--;
			if (goingToPaddock) {
				horsesPaddock++;
				repo.addHorsesToRun(horseId);
				if (horsesPaddock == horsesPerRace) {
					horseCanNotGo = true;
					horsesPaddock = 0;
				}
			}
		} finally {
			mutex.unlock();
		}
		return goingToPaddock;

	}

	/**
	 * When the event are ending the horses are going to the end of event
	 *
	 * 
	 */
	@Override
	public void summonHorsesToEnd() {
		mutex.lock();
		try {
			goingToPaddock = false;
			while (horsesAtStable < totalHorses) {
				try {
					broker_condition.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			horseCanNotGo = false;
			horse_condition.signalAll();

			System.out.println("ALL HORSES ARE GOING TO END OF EVENT");
		} finally {
			mutex.unlock();
		}
	}

	/**
	 * All horses go to paddock
	 *
	 * 
	 */
	@Override
	public void summonHorsesToPaddock() {
		sendMessage("summonHorsesToPaddock");
	}

	public String sendMessage(String payload) {

		String hostName; // nome da máquina onde está o servidor
		int portNumb = 9991; // número do port

		hostName = "localhost";

		/* troca de mensagens com o servidor */

		Stub stub; // stub de comunicação

		stub = new Stub(hostName, portNumb);
		return stub.exchange(payload);	
	}
	
}
