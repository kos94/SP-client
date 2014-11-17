package sp_client;

import sp_entities.UserStatus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

public class MainController implements ActionListener, Observer {
	private final MainModel model;
	private final MainView view;
	
	public MainController(MainModel model, MainView view) {
		this.model = model;
		this.view = view;
		model.addObserver(this);
	}
//	@Override
//	public void mouseClicked(MouseEvent e) {
//		if(e.getClickCount() == 1) {
//			JList<String> list = (JList<String>)e.getSource();
//			System.out.println(list.getSelectedIndex());
//		}
//	}
//	@Override
//	public void mouseEntered(MouseEvent e) {}
//	@Override
//	public void mouseExited(MouseEvent e) {}
//	@Override
//	public void mousePressed(MouseEvent e) {}
//	@Override
//	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void update(Observable o, Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		System.out.println("actionPerformed");
		String command = evt.getActionCommand();
		
		switch(command) {
		case MainView.LOGIN_ACTION_COMMAND:
			String loginStr = view.getLogin();
			String pass = view.getPassword();
			if(pass.length() == 0 || loginStr.length() == 0) 
				return;
			try {
				int id = Integer.parseInt(loginStr);
				model.login(id, pass);
			} catch (Exception e) {
				model.badLogin("Неверный ID");
				return;
			}
			
//			System.out.println("login confirmation: " + view.getLogin() + " " + view.getPassword());
			break;
		}
	}
}
