package com.benjamindebotte.labyrinth.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.benjamindebotte.labyrinth.containers.Labyrinth;

/**
 * @author benjamindebotte
 * Classe destinée à gérer le chargement et la sauvegarde de labyrinthes.
 */
public class LabyrinthFileHandler {

	String path;

	public LabyrinthFileHandler(String filepath) {
		this.path = filepath;

		if (!filepath.endsWith(".laby")) {
			this.path += ".laby";
		}
	}

	public Labyrinth load() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		Labyrinth laby = null;

		File f_laby = new File(this.path);
		if (!f_laby.exists())
			return laby;

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				f_laby));

		laby = (Labyrinth) ois.readObject();
		ois.close();

		return laby;
	}

	public void save(Labyrinth laby) throws IOException {
		File f_laby = new File(this.path);
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				f_laby));
		oos.writeObject(laby);
		oos.close();

	}

}
