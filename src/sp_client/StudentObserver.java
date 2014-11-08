package sp_client;

import java.util.Observable;

public class StudentObserver extends MainObserver {
	public StudentObserver(MainModel model) {
		super(model);
	}
	public void update(Observable o, Object obj) {
		//TODO
		System.out.println("student update");
	}	
}
