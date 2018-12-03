package parser;

import model.Hand;
import model.Stock;
import model.Tile;
import model.observable.ObservableTile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileParser {
	private static final Logger logger = LogManager.getLogger(FileParser.class.getName());
	private final String[] VALID_COLOURS = {
			"R", "B", "G", "O"
	};
	private final String[] VALID_RANKS = {
			"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"
	};

	private Stock stock;

	public FileParser() {
		stock = new Stock();
	}

	public Stock getStock() {
		return stock;
	}

	public ArrayList<Hand> getHandsFromFile(String filepath) {
		ArrayList<Hand> hands = new ArrayList<>();
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			stream.forEachOrdered(line -> {
				List<String> items = Arrays.asList(line.split(" "));
				if (items.size() != 14) {
					logger.error("Invalid number of tiles given to hand: " + line);
				}
				Hand hand = new Hand();
				for (String item : items) {
					ObservableTile t = getTileFromString(item);
					hand.addTile(t);
				}
				hands.add(hand);
			});
		} catch (Exception ignore) {

		}
		return hands;
	}

	public boolean isValidFile(String filepath, int numPlayers) {
		// Similar logic to getHandsFromFile, but we want to just return false as soon as we see a problem with the file
		try (Stream<String> stream = Files.lines(Paths.get(filepath))) {
			for (String line : stream.collect(Collectors.toList())) {
				List<String> items = Arrays.asList(line.split(" "));
				if (items.size() != 14) {
					return false;
				}
				for (String item : items) {
					if (item.length() < 1 || item.length() > 3) {
						return false;
					}
					String colourString = item.substring(0, 1);
					String rankString = item.substring(1);
					if (!isValidColour(colourString)) {
						return false;
					}
					if (!isValidRank(rankString)) {
						return false;
					}
				}
			}
		} catch (Exception ignore) {
			return false;
		}

		ArrayList<Hand> hands = getHandsFromFile(filepath);
		return hands.size() == numPlayers;
	}

	private ObservableTile getTileFromString(String s) {
		if (s.length() < 1 || s.length() > 3) {
			logger.error("Input is too long, cannot convert to Tile object: ", s);
			return null;
		}
		String colourString = s.substring(0, 1);
		String rankString = s.substring(1);
		if (!isValidColour(colourString)) {
			logger.error("Invalid colour: " + colourString);
		}
		if (!isValidRank(rankString)) {
			logger.error("Invalid rank: " + rankString);
		}
		Tile.Colours colour = null;
		switch (colourString) {
			case "R":
				colour = Tile.Colours.RED;
				break;
			case "B":
				colour = Tile.Colours.BLUE;
				break;
			case "G":
				colour = Tile.Colours.GREEN;
				break;
			case "O":
				colour = Tile.Colours.ORANGE;
				break;
			default:
				logger.error("Could not map colour to a Tile.Colours: " + colourString);
				break;
		}
		final Tile.Colours finalColour = colour;
		final int finalRank = Integer.parseInt(rankString);
		ObservableTile tile = stock.getStock().stream()
				.filter(t -> t.getColour().equals(finalColour) && t.getRank() == finalRank)
				.findFirst()
				.orElseThrow();
		stock.getStock().remove(tile);
		return tile;
	}

	private boolean isValidColour(String colour) {
		String tmp = Arrays.stream(VALID_COLOURS)
				.filter(s -> s.equals(colour.toUpperCase()))
				.findFirst()
				.orElse("");
		return tmp.equals(colour.toUpperCase());
	}

	private boolean isValidRank(String rank) {
		String tmp = Arrays.stream(VALID_RANKS)
				.filter(s -> s.equals(rank.toUpperCase()))
				.findFirst()
				.orElse("");
		return tmp.equals(rank.toUpperCase());
	}
}
