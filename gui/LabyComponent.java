package com.benjamindebotte.labyrinth.gui;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Observable;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.benjamindebotte.labyrinth.containers.Labyrinth;
import com.benjamindebotte.labyrinth.entities.Bonus;
import com.benjamindebotte.labyrinth.entities.Entity;
import com.benjamindebotte.labyrinth.entities.FinishLine;
import com.benjamindebotte.labyrinth.entities.LabyObject;
import com.benjamindebotte.labyrinth.entities.Monster;
import com.benjamindebotte.labyrinth.entities.Player;
import com.benjamindebotte.labyrinth.entities.Wall;
import com.benjamindebotte.labyrinth.events.input.KeyboardEvent;
import com.benjamindebotte.labyrinth.gameplay.Game;

public class LabyComponent extends JComponent {

	private class LabyKeyListener extends Observable implements KeyListener {
		@Override
		public void keyPressed(KeyEvent evt) {
			this.setChanged();
			this.notifyObservers(new KeyboardEvent(evt.getKeyCode()));
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}

	private static final long serialVersionUID = 2337634060590226403L;
	private final Game game;
	private JLabel[][] GUI;

	private ImageIcon iconFinish;
	private ImageIcon iconFloor;
	private ImageIcon iconItem;
	private ImageIcon iconMonster;
	/* Icônes pré-chargés */
	private ImageIcon iconPlayer;
	private boolean iconsRescaled;
	private ImageIcon iconWall;

	private LabyKeyListener listener;

	public LabyComponent(Game gameHandler) {
		if (gameHandler == null)
			throw new NullPointerException();

		this.game = gameHandler;

		this.initKeyListener();

		this.loadIcons();

		this.initGUI();

		this.setFocusable(true);

	}

	public Game getGame() {
		return this.game;
	}

	private void initGUI() {
		if (this.game.getLabyrinth() == null)
			throw new NullPointerException();
		Labyrinth laby = this.game.getLabyrinth();
		this.setLayout(new GridLayout(laby.getMap().getLength(), laby.getMap()
				.getWidth()));
		this.GUI = new JLabel[laby.getMap().getLength()][laby.getMap()
		                                                 .getWidth()];
		for (int i = 0; i < laby.getMap().getLength(); i++) {
			for (int j = 0; j < laby.getMap().getWidth(); j++) {
				this.GUI[i][j] = new JLabel();
				this.add(this.GUI[i][j]);
			}
		}
	}

	private void initKeyListener() {
		this.listener = new LabyKeyListener();
		this.addKeyListener(this.listener);
		this.listener.addObserver(this.game);
	}

	private void loadIcons() {
		/* On charge les icônes */
		this.iconPlayer = new ImageIcon("./img/Player.png");
		this.iconWall = new ImageIcon("./img/Wall.png");
		this.iconItem = new ImageIcon("./img/Item.jpg");
		this.iconFloor = new ImageIcon("./img/Floor.png");
		this.iconMonster = new ImageIcon("./img/Monster.png");
		this.iconFinish = new ImageIcon("./img/FinishLine.png");
		this.iconsRescaled = false;
	}

	public void objectToGraphics(JLabel graphObject, LabyObject obj) {
		graphObject.setOpaque(true);
		ImageIcon icon = null;
		if (obj instanceof Wall) {
			icon = this.iconWall;
		} else if (obj == null) {
			icon = (this.iconFloor);
		} else if (obj instanceof Entity) {
			if (obj instanceof Monster) {
				icon = (this.iconMonster);
			} else if (obj instanceof Player) {
				icon = (this.iconPlayer);
			}
		} else if (obj instanceof Bonus) {
			icon = (this.iconItem);
		} else if (obj instanceof FinishLine) {
			icon = this.iconFinish;
		}
		if (icon == null)
			return;

		graphObject.setIcon(icon);

	}

	public void refreshGUI() {

		this.requestFocusInWindow();

		if (!this.iconsRescaled) {
			if (this.GUI[0][0].getWidth() != 0
					&& this.GUI[0][0].getHeight() != 0) {
				this.iconPlayer.setImage(this.iconPlayer.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconWall.setImage(this.iconWall.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconItem.setImage(this.iconItem.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconFloor.setImage(this.iconFloor.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconMonster.setImage(this.iconMonster.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconFinish.setImage(this.iconFinish.getImage()
						.getScaledInstance(this.GUI[0][0].getWidth(),
								this.GUI[0][0].getWidth(), Image.SCALE_FAST));
				this.iconsRescaled = true;
			}
		}

		Labyrinth laby = this.game.getLabyrinth();

		for (int i = 0; i < laby.getMap().getLength(); i++) {
			for (int j = 0; j < laby.getMap().getWidth(); j++) {
				this.objectToGraphics(this.GUI[i][j],
						laby.getMap().getCase(i, j).getObj());
			}
		}

		this.repaint();

	}

	@Override
	public void repaint() {
		super.repaint();
	}

}
