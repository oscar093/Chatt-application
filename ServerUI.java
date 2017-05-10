package chatt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;

/**
 * A class that represents the UI for the server
 * 
 * @author Group 2
 *
 */
public class ServerUI extends JFrame {

	private ServerController sc;
	private JButton b_start;
	private JScrollPane jScrollPane1;
	private JLabel lb_name;
	private JTextArea ta_chat;

	/**
	 * Constructor and graphical components for the UI.
	 * 
	 * @param sc
	 *            - the serverController.
	 */
	public ServerUI(ServerController sc) {
		this.sc = sc;
		new JFrame("Server");
		jScrollPane1 = new JScrollPane();
		ta_chat = new JTextArea();
		b_start = new JButton();
		lb_name = new JLabel();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("Server");
		setName("server");
		setResizable(false);
		setVisible(true);

		ta_chat.setColumns(20);
		ta_chat.setRows(5);
		jScrollPane1.setViewportView(ta_chat);

		b_start.setText("START");
		b_start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sc.start();
				ta_chat.append("Server started\n");
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup()
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jScrollPane1).addGroup(layout.createSequentialGroup()
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)

										.addComponent(b_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75,
												Short.MAX_VALUE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 291,
										Short.MAX_VALUE)
								.addGroup(
										layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false))))
				.addContainerGap())
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lb_name).addGap(209, 209, 209)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap()
				.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
				.addGap(18, 18, 18)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(b_start))
				.addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE))
				.addGap(4, 4, 4).addComponent(lb_name)));
		pack();
	}
	
	public void setUIText(String txt){
		ta_chat.append(txt);
	}
}