package com.jtl.quiz.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ServerUI {
	public static JFrame MASTER_FRAME;

	public static void log() {
		log("");
	}

	public static void log(Object msg) {
		System.out.println(msg);
		logArea.append(msg.toString() + "\n");
	}

	private static JTextArea logArea = new JTextArea(40, 80);

	public static void initialize() {
		JFrame frame = MASTER_FRAME = new JFrame("QuizGame Server Admin Panel (QSAP)");
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);

		logArea.setFont(new Font("Courier", Font.PLAIN, logArea.getFont().getSize()));
		logArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		logArea.setEditable(false);
		panel.add(logArea, c);

		JButton issueButton = new JButton("Issue Question");
		issueButton.addActionListener((e) -> {
			IssueQuestionUI.initialize();
		});
		c.gridx++;
		panel.add(issueButton, c);

		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
	}
}
