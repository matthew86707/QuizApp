package com.jtl.quiz.graphics;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.jtl.quiz.main.ClientMain;
import com.jtl.quiz.model.Heart;
import com.jtl.quiz.model.Option;
import com.jtl.quiz.model.Player;

public class MouseHandler implements MouseListener {
	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (!(ClientMain.finalOption == null)) {
			if (!(ClientMain.finalOption.heart.value == 0)) {
				if (SpinPanel.isFinished) {
					if (SpinPanel.spinValue == false) {
						ClientMain.currentOption = null;
						ClientMain.currentOptions = null;
						ClientMain.finalOption = null;
						ClientMain.changed = Player.allPlayers.get(0);
						return;
					}
				}
				if (ClientMain.finalOption.chance == 1.0f || SpinPanel.isFinished) {
					for (int i = 0; i < Player.allPlayers.size(); i++) {
						Player player = Player.allPlayers.get(i);
						for (int j = 0; j < player.hearts.size(); j++) {
							if (x < (Heart.STATIC_OFFSET_X + Heart.OFFSET_X)) {
								if (y > (Player.STATIC_OFFSET_Y + (i * Player.OFFSET_Y) - 30)
										&& y < (Player.STATIC_OFFSET_Y + (i * Player.OFFSET_Y) + Heart.SIZE_Y)) {
									if (ClientMain.finalOption.heart.value < 0) {
										player.hearts.remove(player.hearts.size() - 1);
										ClientMain.finalOption.heart.value++;
									} else {
										Player.allPlayers.get(i).hearts.add(new Heart(1));
										ClientMain.finalOption.heart.value--;
									}
									ClientMain.changed = player;
									GraphicsManager.getCurrentWindow().repaint();
								}
							}
						}
					}

				}
			} else {
				for (int i = 0; i < Player.allPlayers.size(); i++) {
					if (x < (Heart.STATIC_OFFSET_X + Heart.OFFSET_X)) {
						if (y > (Player.STATIC_OFFSET_Y + (i * Player.OFFSET_Y) - 30)
								&& y < (Player.STATIC_OFFSET_Y + (i * Player.OFFSET_Y) + Heart.SIZE_Y)) {
							Player.allPlayers.get(i).hearts.add(ClientMain.finalOption.heart);
							ClientMain.changed = Player.allPlayers.get(i);
							GraphicsManager.getCurrentWindow().repaint();
							break;
						}
					}
				}
			}
		}
		if (ClientMain.currentOptions != null) {
			for (int i = 0; i < ClientMain.currentOptions.options.size(); i++) {
				if (x > (Option.STATIC_OFFSET_X + (i * Option.OFFSET_X))
						&& x < (Option.STATIC_OFFSET_X + (i * Option.OFFSET_X) + Option.OFFSET_X)) {
					if (y > (Option.STATIC_Y) && y < (Option.STATIC_Y + Option.SIZE_Y)) {
						ClientMain.currentOption = ClientMain.currentOptions.options.get(i);
						GraphicsManager.getCurrentWindow().repaint();
					}
				}
			}
			if (ClientMain.currentOption != null) {
				if (x > GraphicsManager.CONFIRM_X && x < GraphicsManager.CONFIRM_X + GraphicsManager.CONFIRM_SIZE_X) {
					if (y > GraphicsManager.CONFIRM_Y
							&& y < GraphicsManager.CONFIRM_Y + GraphicsManager.CONFIRM_SIZE_Y) {

						if (ClientMain.currentOption.chance != 1.0f) {
							SpinWindow sp = new SpinWindow(0);
							sp.spin();
						}

						ClientMain.finalOption = ClientMain.currentOption;
						for (int i = 0; i < ClientMain.currentOptions.options.size(); i++) {
							if (!(ClientMain.currentOptions.options.get(i).text
									.equals(ClientMain.currentOption.text))) {
								ClientMain.currentOptions.options.remove(i);
								i--;
							}
						}
					}
				}
			}
			GraphicsManager.getCurrentWindow().repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
