package controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JList;

import view.MainView;
import model.MainModel;

public class MainController implements ActionListener, MouseListener, Observer {
	private final MainModel model;
	private final MainView view;
	
	public MainController(MainModel model, MainView view) {
		this.model = model;
		this.view = view;
		model.addObserver(this);
	}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getClickCount() != 1) return;
		Object source = e.getSource();
		
		if(source instanceof JList<?>) {
			JList<?> list = (JList<?>)e.getSource();
			int index = list.getSelectedIndex();
			String value = (String) list.getSelectedValue();
			model.setListChoice(index, value);
		}
	}
	
	@Override
	public void update(Observable o, Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		String command = evt.getActionCommand();
		System.out.println("controller actionPerformed\n" + command);
		if(command.equals(MainView.LOGIN_ACTION_COMMAND)) {
			String loginStr = view.getLogin();
			String pass = view.getPassword();
			if(pass.length() == 0 || loginStr.length() == 0) 
				return;
			int id = Integer.parseInt(loginStr);
			model.login(id, pass);
			
			System.out.println("login confirmation: " + view.getLogin() + " " + view.getPassword());
		} else if(command.matches("(" + MainView.HISTORY_BUTTON_COMMAND + ")[0-9]+")) {
			String strNumber = command.substring(
					MainView.HISTORY_BUTTON_COMMAND.length());
			int number = Integer.parseInt(strNumber);
			model.goBack(number);
		}
	}
}