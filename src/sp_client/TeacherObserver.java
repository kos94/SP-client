package sp_client;

import java.util.Observable;

public class TeacherObserver extends MainObserver {
	public TeacherObserver(MainModel model) {
		super(model);
	}
	public void update(Observable o, Object obj) {
		//TODO
		System.out.println("teacher update");
	}
}
