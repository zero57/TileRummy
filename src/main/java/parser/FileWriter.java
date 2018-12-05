package parser;

import model.Hand;
import model.Tile;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileWriter {
	private static final Logger logger = LogManager.getLogger(FileParser.class.getName());

	public static File getRiggedHandFile(ArrayList<Hand> hands) throws IOException {
		File tmpFile = File.createTempFile("tilerummy", "tmp");
		tmpFile.deleteOnExit();
		ArrayList<String> lines = new ArrayList<>();
		for (Hand hand : hands) {
			lines.add(getStringOutputFromHand(hand));
		}
		Files.write(Paths.get(tmpFile.getAbsolutePath()), lines, Charset.forName("UTF-8"));

		FileParser fileParser = new FileParser();
		if (!fileParser.isValidFile(tmpFile.getAbsolutePath(), hands.size())) {
			logger.error("Problem with creating a rigged hand file!");
			for (String line : lines) {
				logger.error(line);
			}
		}

		return tmpFile;
	}

	private static String getStringOutputFromHand(Hand hand) {
		StringBuilder output = new StringBuilder();
		for (ObservableTile tile : hand.getTiles()) {
			String colour = convertColourToString(tile.getColour());
			output.append(colour).append(tile.getRank()).append(" ");
		}
		return output.toString().trim();
	}

	private static String convertColourToString(Tile.Colours colour) {
		switch (colour) {
			case RED:
				return "R";
			case BLUE:
				return "B";
			case GREEN:
				return "G";
			case ORANGE:
				return "O";
			default:
				return null;
		}
	}
}
