/**
 *
 */
package com.benjamindebotte.labyrinth.events.game;

import com.benjamindebotte.labyrinth.entities.LabyObject;
import com.benjamindebotte.labyrinth.events.Event;

/**
 * @author benjamindebotte
 * Classe représentant un événement affectant le jeu : il contient des informations sur l'entité qui a provoqué l'événement.
 */
public class GameEvent extends Event {

	private LabyObject sender;

	/**
	 *
	 */
	public GameEvent(LabyObject sender) {
		this.sender = sender;
	}

	public LabyObject getSender() {
		return this.sender;
	}

	public void setSender(LabyObject sender) {
		this.sender = sender;
	}

}
