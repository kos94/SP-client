package sp_client;

import java.util.Observable;

public class CuratorObserver extends MainObserver {
	public CuratorObserver(MainModel model) {
		super(model);
	}
	public void update(Observable o, Object obj) {
		//TODO
		System.out.println("curator update");
	}	
}
