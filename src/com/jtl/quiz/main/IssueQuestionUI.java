package com.jtl.quiz.main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jnetwork.ClientData;
import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

import com.jtl.quiz.model.Player;

public class IssueQuestionUI {
	private static JCheckBox[] checkBoxes = new JCheckBox[6];
	private static File file;
	private static File lastFolder;

	public static void initialize() {
		JDialog frame = new JDialog(ServerUI.MASTER_FRAME, "QSAP: Issue Question");
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0);

		JLabel preview = new JLabel();

		JFileChooser fileChooser = new JFileChooser(lastFolder);
		fileChooser.getComponent(4).setVisible(false);
		fileChooser.addActionListener((e) -> {
			String name = fileChooser.getSelectedFile().getName();
			if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("jpeg") || name.endsWith("gif")) {
				try {
					lastFolder = fileChooser.getSelectedFile().getParentFile();
					file = fileChooser.getSelectedFile();
					BufferedImage image = ImageIO.read(fileChooser.getSelectedFile());
					Image scaled = image.getScaledInstance(image.getWidth() * (int) (400 / (double) image.getHeight()),
							fileChooser.getPreferredSize().height, Image.SCALE_DEFAULT);
					preview.setIcon(new ImageIcon(scaled));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		panel.add(fileChooser, c);
		c.gridx++;

		c.gridx = 0;
		c.gridy++;

		for (int i = 0; i < 6; i++) {
			checkBoxes[i] = new JCheckBox();
			checkBoxes[i].setText(Character.toString((char) (65 + i)));
			panel.add(checkBoxes[i], c);
			c.gridy++;
		}

		JButton issue = new JButton("Issue");
		issue.addActionListener((e) -> {
			if (file != null) {
				String str = "";
				for (JCheckBox box : checkBoxes) {
					if (box.isSelected()) {
						str += box.getText();
					}
				}
				ServerMain.answers = str.split("");

				ServerUI.log("Sending the image...");
				for (ClientData data : ServerMain.server.getClients()) {
					new Thread(() -> {
						try {
							TCPConnection client = (TCPConnection) data.getConnection();
							client.getOutputStream().writeInt(1);
							client.getOutputStream().writeFile(file);
							boolean isCorrect = false;
							DataPackage back = (DataPackage) client.getInputStream().readObject();
							Player p = (Player) back.getObjects()[0];
							for (Player ply : Player.allPlayers) {
								if (ply.name.equals(p.name)) {
									if (ServerMain.checkAnswer(back.getObjects()[1].toString().toUpperCase())) {
										ServerMain.answeredCorrect = ServerMain.answeredCorrect + 1;

										isCorrect = true;
										ply.place = ServerMain.answeredCorrect;
									}
								}
							}
							ServerMain.answered++;
							ServerUI.log(
									p.name + ": " + back.getObjects()[1]
											+ (isCorrect
													? " (Correct), Answered in place: " + ServerMain.answeredCorrect
													: " (Incorrect)"));
							isCorrect = false;
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}).start();
				}
				ServerUI.log("Image located at " + file.getAbsolutePath() + " sent.");
				file = null;
				frame.dispose();
			}
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
