package ch.bbcag.epix.popup;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author  Miguel Jorge, Penglerd Chiramet Phong || ICT Berufsbildungs AG
 *			PasswordEmpty.java.java Copyright Berufsbildungscenter 2015
 */

public class PasswordEmpty extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordEmpty() {
		JOptionPane.showMessageDialog(null,"Bitte Passwort ausf�llen!", null, JOptionPane.WARNING_MESSAGE, null);
	};

}
