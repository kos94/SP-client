package sp_client;

import java.util.Observable;

public class DepWorkerObserver extends MainObserver {
	public DepWorkerObserver(MainModel model) {
		super(model);
	}
	public void update(Observable o, Object obj) {
		//TODO
		System.out.println("depworker update");
	}	
}
