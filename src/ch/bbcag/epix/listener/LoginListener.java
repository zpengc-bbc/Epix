package ch.bbcag.epix.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import ch.bbcag.epix.controller.EpixController;
import ch.bbcag.epix.entity.User;
import ch.bbcag.epix.view.EpixView;
import ch.bbcag.epix.view.Registration;

/**
 * Listener fuers LoginView
 * @author  Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *			LoginListener.java.java Copyright Berufsbildungscenter 2015
 */

public class LoginListener implements ActionListener {

	private JFrame loginView;
	private JTextField username;
	private JPasswordField password;

	
	/**
	 * Konstruktor fuer Login
	 * @param username {@link JTextField}
	 * @param password {@link JPasswordField}
	 * @param loginView {@link JFrame}
	 */
	public LoginListener(JTextField username, JPasswordField password, JFrame loginView) {
		this.setLoginView(loginView);
		this.setUsername(username);
		this.setPassword(password);
	}
	
	
	/**
	 * Konstruktor fuer Registrieren
	 * @param loginView {@link JFrame}
	 */
	public LoginListener(JFrame loginView) {
		this.setLoginView(loginView);
	}
	
	

	public void actionPerformed(ActionEvent e) {

		if(e.getActionCommand().equals("Login")){
			User loginUser = new User();
			String user = this.getUsername().getText();
			@SuppressWarnings("deprecation")
			String password = this.getPassword().getText();
	
			loginUser.setPassword(password);
			loginUser.setUsername(user);
	
			if (EpixController.getInstance().login(loginUser) == true) {
				loginView.dispose();
				User player = EpixController.getInstance().playerLogin(user);
				new EpixView(player);
			}
		} else if(e.getActionCommand().equals("Registrieren")) {
			getLoginView().dispose();	
			new Registration();
		}
	}

	
	/*
	 * Getter und Setter
	 */
	public JFrame getLoginView() {
		return loginView;
	}

	public void setLoginView(JFrame login2) {
		this.loginView = login2;
	}

	public JPasswordField getPassword() {
		return password;
	}

	public void setPassword(JPasswordField password) {
		this.password = password;
	}

	public JTextField getUsername() {
		return username;
	}

	public void setUsername(JTextField username) {
		this.username = username;
	}

}
