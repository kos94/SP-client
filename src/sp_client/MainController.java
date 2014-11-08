package sp_client;

import sp_entities.UserStatus;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JList;

public class MainController implements MouseListener {
	private final MainModel model;
	private final UserStatus status;
	public MainController(MainModel model, UserStatus status) {
		this.model = model;
		this.status = status;
		switch(status) {
		case TEACHER:
			model.addObserver(new TeacherObserver(model));
			break;
		case CURATOR:
			model.addObserver(new CuratorObserver(model));
			break;
		case DEPWORKER:
			model.addObserver(new DepWorkerObserver(model));
			break;
		case STUDENT:
			model.addObserver(new StudentObserver(model));
			break;
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 1) {
			JList<String> list = (JList<String>)e.getSource();
			System.out.println(list.getSelectedIndex());
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
