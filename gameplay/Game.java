package com.benjamindebotte.labyrinth.gameplay;

/**
 *
 */

import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import com.benjamindebotte.labyrinth.containers.Labyrinth;
import com.benjamindebotte.labyrinth.entities.LabyObject;
import com.benjamindebotte.labyrinth.events.Event;
import com.benjamindebotte.labyrinth.events.game.GameEvent;
import com.benjamindebotte.labyrinth.events.game.GameOverEvent;
import com.benjamindebotte.labyrinth.events.game.GameWinEvent;
import com.benjamindebotte.labyrinth.events.input.InputEvent;
import com.benjamindebotte.labyrinth.events.input.KeyboardEvent;

/**
 * @author benjamindebotte
 *
 */
public class Game implements Observer {

	public enum GAME_STATE {
		LOST, NOT_FINISHED, VICTORY
	}

	private class GameTask extends TimerTask {

		private void processEvent(Event e) {
			if (e instanceof GameEvent) {
				if (e instanceof GameOverEvent) {
					Game.this.setEndGameAsLost();
				} else if (e instanceof GameWinEvent) {
					Game.this.setEndGameAsWon();
				} else {
					Game.this.gameplayHandler.processGameEvent((GameEvent) e);
				}
			} else if (e instanceof InputEvent) {
				if (e instanceof KeyboardEvent) {
					Game.this.moveHandler
					.processKeyboardEvent((KeyboardEvent) e);
				}
			} else {
			}
		}

		private void processEvents() {
			while (!Game.this.events.isEmpty()) {
				this.processEvent(Game.this.events.removeFirst());
			}
		}

		@Override
		public void run() {
			this.processEvents();
		}
	}

	private class MonstersTask extends TimerTask {
		@Override
		public void run() {
			Game.this.moveHandler.moveAll();
		}
	}

	private final static int GAME_RATE = 100;
	private final static int MONSTER_MOVE_RATE = 500; // ms

	private final LinkedList<Event> events;
	private final GameplayHandler gameplayHandler;

	private GAME_STATE gameState;

	private Timer gameTimer, monstersTimer;

	private final Labyrinth laby;

	private final MoveHandler moveHandler;

	/**
	 * @throws Exception
	 *
	 */
	public Game(int length, int width) throws Exception {
		this.laby = new Labyrinth(length, width);
		this.gameplayHandler = new GameplayHandler(this);
		this.moveHandler = new MoveHandler(this.laby);
		this.events = new LinkedList<Event>();
		this.gameState = GAME_STATE.NOT_FINISHED;

		for (LabyObject obj : this.laby.getObjects()) {
			obj.addObserver(this);
		}

		/* ********* Timers ******** */
		this.gameTimer = new Timer(true);
		this.monstersTimer = new Timer(true);

		this.gameTimer.scheduleAtFixedRate(new GameTask(), 0, GAME_RATE);
		this.monstersTimer.scheduleAtFixedRate(new MonstersTask(), 0,
				MONSTER_MOVE_RATE);

		/* * * * * * */

	}

	public Game(Labyrinth laby) {
		this.laby = laby;
		this.gameplayHandler = new GameplayHandler(this);
		this.moveHandler = new MoveHandler(laby);
		this.events = new LinkedList<Event>();
		this.gameState = GAME_STATE.NOT_FINISHED;

		for (LabyObject obj : laby.getObjects()) {
			obj.addObserver(this);
		}

		/* ********* Timers ******** */

		this.startTimers();

		/* * * * * * */
	}

	public void addEvent(Event e) {
		this.events.add(e);

	}

	public int getGameRate() {
		return GAME_RATE;
	}

	public GAME_STATE getGameState() {
		return this.gameState;
	}

	public Labyrinth getLabyrinth() {
		return this.laby;
	}

	public int getLives() {
		return this.gameplayHandler.getLives();
	}

	public int getScore() {
		return this.gameplayHandler.getScore();
	}

	public boolean isGameEnded() {
		return this.gameState != GAME_STATE.NOT_FINISHED;
	}

	public void pauseTimers() {
		this.gameTimer.cancel();
		this.monstersTimer.cancel();
	}

	private void setEndGameAsLost() {
		this.gameState = GAME_STATE.LOST;
		this.gameTimer.cancel();
		this.monstersTimer.cancel();
	}

	private void setEndGameAsWon() {
		this.gameState = GAME_STATE.VICTORY;
		this.gameTimer.cancel();
		this.monstersTimer.cancel();
	}

	public void setGameState(GAME_STATE gameState) {
		this.gameState = gameState;
	}

	public void startTimers() {
		this.gameTimer = new Timer(true);
		this.monstersTimer = new Timer(true);
		this.gameTimer.scheduleAtFixedRate(new GameTask(), 0, GAME_RATE);
		this.monstersTimer.scheduleAtFixedRate(new MonstersTask(), 0,
				MONSTER_MOVE_RATE);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (!(arg instanceof Event))
			return;
		this.addEvent((Event) arg);
	}

}
