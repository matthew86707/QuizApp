package com.jtl.quiz.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class IssueQuestionUI {
	public static void initialize() {
		JDialog frame = new JDialog(ServerUI.MASTER_FRAME, "QSAP: Issue Question");
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);

		JLabel preview = new JLabel();

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.getComponent(4).setVisible(false);
		fileChooser.addActionListener((e) -> {
			String name = fileChooser.getSelectedFile().getName();
			if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("gif")) {
				try {
					BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
					Image scaled = image.getScaledInstance(image.getWidth() * (int) (400 / (double) image.getHeight()),
							fileChooser.getPreferredSize().height, Image.SCALE_DEFAULT);
					System.out.println(scaled.getWidth(panel) + " x " + scaled.getHeight(panel));
					preview.setIcon(new ImageIcon(scaled));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			if (e.getActionCommand().equals("ApproveSelection")) {
				ServerUI.log("kek");
			}
		});
		panel.add(fileChooser, c);
		c.gridx++;

		panel.add(preview, c);
		c.gridx = 0;
		c.gridy++;

		JButton issue = new JButton("Issue");
		issue.addActionListener((e) -> {
		});
		panel.add(issue, c);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((e) -> frame.dispose());
		c.anchor = GridBagConstraints.EAST;
		panel.add(cancel, c);

		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
	}
}
