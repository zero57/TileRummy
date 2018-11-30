import controller.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Game;
import model.Meld;
import model.Stock;
import model.Tile;
import model.observable.ObservableTile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import ui.TileButton;
import model.OptionChoices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

@ExtendWith(ApplicationExtension.class)
class IntegrationTest {

	MainController controller;
	Game game;

	@Start
	void onStart(Stage stage) throws Exception {
		final var width = 640;
		final var height = 480;

		OptionChoices optionChoices = new OptionChoices();
		optionChoices.setNumPlayers(4);
		optionChoices.setPlayer1(0);
		optionChoices.setPlayer2(1);
		optionChoices.setPlayer3(2);
		optionChoices.setPlayer4(3);
		optionChoices.setRigHandChecked(false);
		optionChoices.setRigTileDrawChecked(false);
		optionChoices.setShowHandsChecked(false);
		optionChoices.setTimerChecked(false);
		controller = new MainController(optionChoices);
		FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("view/MainView.fxml"));
		loader.setControllerFactory(c -> controller);
		VBox root = loader.load();
		game = controller.getGame();

		// todo: figure out why using the decorator fails to work and we get async exception errors in TestFX
		// See https://github.com/TestFX/TestFX/issues/492
//		var decorator = new JFXDecorator(stage, root);
//		decorator.getStylesheets().add(Objects.requireNonNull(getClass().getClassLoader().getResource("css/window.css")).toExternalForm());
//		var scene = new Scene(decorator, width, height);
		var scene = new Scene(root, width, height);
		stage.setTitle("Tile Rummy");
		stage.setScene(scene);
		stage.show();
	}

	@BeforeEach
	void setup() {
		game.resetGame();
	}

	@AfterEach
	void cleanup() throws Exception {
		FxToolkit.cleanupStages();
	}

	@Test
	void testDisplayInitial14TilesInEachPlayersHand(FxRobot robot) {
		robot.interact(() -> {
			game.setStock(new Stock());
			game.dealInitialTiles();
		});
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(14));
		assertThat(controller.getPlayer2HandPane().getChildren().size(), is(14));
		assertThat(controller.getPlayer3HandPane().getChildren().size(), is(14));
		assertThat(controller.getPlayer4HandPane().getChildren().size(), is(14));
	}

	@Test
	void testPlayerHandShouldBeSorted(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(14));
		assertThat(controller.getPlayer1HandPane().getChildren(),
				contains(Arrays.asList(
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t1)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t2)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t3)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t4)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t5)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t6)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t7)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t8)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t9)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t10)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t11)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t12)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t13)),
						equalTo(findTileButtonInPane(controller.getPlayer1HandPane(), t14))
				)));
	}

	@Test
	void testPlayerCanDragTileOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(13));
	}

	@Test
	void testPlayerCanDragRunMeldOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(11));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanDragSetMeldOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(11));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerCanAddTileToRunMeldOnTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.RED), 0, 2);
			game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 3);
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(13));
		assertThat(game.getTable().get(0).getValue(), is(10));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanAddTileToSetMeldOnTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(3, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.BLUE), 0, 2);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.GREEN), 0, 3);
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t14))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(13));
		assertThat(game.getTable().get(0).getValue(), is(12));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerCanAddSeveralTilesToRunMeldOnTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		var t7 = new ObservableTile(7, Tile.Colours.RED);
		var t8 = new ObservableTile(8, Tile.Colours.RED);
		var t9 = new ObservableTile(9, Tile.Colours.RED);
		var t10 = new ObservableTile(10, Tile.Colours.RED);
		var t11 = new ObservableTile(11, Tile.Colours.RED);
		var t12 = new ObservableTile(12, Tile.Colours.RED);
		var t13 = new ObservableTile(13, Tile.Colours.RED);
		var t14 = new ObservableTile(1, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(1, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 0, 2);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.RED), 0, 3);
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 4));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 5));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 6));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 7));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t8))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 8));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t9))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 9));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t10))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 10));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t11))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 11));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t12))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 12));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t13))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 13));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(4));
		assertThat(game.getTable().get(0).getValue(), is(91));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanTakeMeldFromTableAndMakeASetAndRunMeldFromHand(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(8, Tile.Colours.RED);
		var t5 = new ObservableTile(8, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);

		var boardTile1 = new ObservableTile(4, Tile.Colours.RED);
		var boardTile2 = new ObservableTile(8, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(1, Tile.Colours.RED), 0, 0);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.RED), 0, 2);
			game.addTileToTable(boardTile1, 0, 3);
			game.addTileToTable(new ObservableTile(8, Tile.Colours.RED), 1, 0);
			game.addTileToTable(new ObservableTile(8, Tile.Colours.BLUE), 1, 1);
			game.addTileToTable(new ObservableTile(8, Tile.Colours.GREEN), 1, 2);
			game.addTileToTable(boardTile2, 1, 3);
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 5));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 6));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 7));
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 3), boardTile1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 8));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 5));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 6));
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 3), boardTile2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 7));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(9));
		assertThat(game.getTable().get(0).getValue(), is(6));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getValue(), is(24));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(2).getValue(), is(10));
		assertThat(game.getTable().get(2).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(3).getValue(), is(24));
		assertThat(game.getTable().get(3).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerDrawsOneTileIfTheyCannotPlay(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(8, Tile.Colours.RED);
		var t5 = new ObservableTile(8, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t15 = new ObservableTile(9, Tile.Colours.ORANGE);

		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14,
					t15
			))));
			for (int i = 0; i < 14; i++) {
				game.drawTile().ifPresent(t -> game.getPlayer1Hand().addTile(t));
			}
		});
		robot.clickOn("#btnEndTurn");
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(15));
		assertThat(game.getPlayer1Hand().getTiles().contains(t15), is(true));
	}

	@Test
	void testPlayerCanDragSeveralRunsOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t8))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t9))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t10))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t11))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t12))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 2));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(2));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(2).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(3).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanDragSeveralSetsOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(1, Tile.Colours.BLUE);
		var t3 = new ObservableTile(1, Tile.Colours.GREEN);
		var t4 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t5 = new ObservableTile(2, Tile.Colours.RED);
		var t6 = new ObservableTile(2, Tile.Colours.BLUE);
		var t7 = new ObservableTile(3, Tile.Colours.GREEN);
		var t8 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t9 = new ObservableTile(3, Tile.Colours.RED);
		var t10 = new ObservableTile(4, Tile.Colours.BLUE);
		var t11 = new ObservableTile(4, Tile.Colours.GREEN);
		var t12 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(5, Tile.Colours.RED);
		var t14 = new ObservableTile(5, Tile.Colours.BLUE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t8))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t9))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t10))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t11))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t12))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 2));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(2));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(2).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(3).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerCanDragSetAndRunMeldOntoTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(8, Tile.Colours.RED);
		var t5 = new ObservableTile(8, Tile.Colours.BLUE);
		var t6 = new ObservableTile(8, Tile.Colours.GREEN);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(3, Tile.Colours.ORANGE);

		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(8));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerCanPlayAMeldThatUsesATileFromAnExistingRunOnTheTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.BLUE);
		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		var t7 = new ObservableTile(7, Tile.Colours.RED);
		var t8 = new ObservableTile(8, Tile.Colours.RED);
		var t9 = new ObservableTile(9, Tile.Colours.RED);
		var t10 = new ObservableTile(10, Tile.Colours.RED);
		var t11 = new ObservableTile(11, Tile.Colours.RED);
		var t12 = new ObservableTile(12, Tile.Colours.RED);
		var t13 = new ObservableTile(13, Tile.Colours.RED);
		var t14 = new ObservableTile(1, Tile.Colours.ORANGE);
		var boardTile = new ObservableTile(4, Tile.Colours.RED);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(1, Tile.Colours.RED), 0, 0);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.RED), 0, 2);
			game.addTileToTable(boardTile, 0, 4);
		});
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 4), boardTile))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 3));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(11));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanPlayAMeldThatUsesATileFromAnExistingSetOnTheTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t3 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t4 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		var t7 = new ObservableTile(7, Tile.Colours.RED);
		var t8 = new ObservableTile(8, Tile.Colours.RED);
		var t9 = new ObservableTile(9, Tile.Colours.RED);
		var t10 = new ObservableTile(10, Tile.Colours.RED);
		var t11 = new ObservableTile(11, Tile.Colours.RED);
		var t12 = new ObservableTile(12, Tile.Colours.RED);
		var t13 = new ObservableTile(13, Tile.Colours.RED);
		var t14 = new ObservableTile(8, Tile.Colours.ORANGE);
		var boardTile = new ObservableTile(1, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(1, Tile.Colours.RED), 0, 0);
			game.addTileToTable(new ObservableTile(1, Tile.Colours.BLUE), 0, 1);
			game.addTileToTable(new ObservableTile(1, Tile.Colours.GREEN), 0, 2);
			game.addTileToTable(boardTile, 0, 3);
		});
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 3), boardTile))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 3));
		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(11));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerCanDragSeveralTilesToSeveralMeldsOnTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(new ObservableTile(4, Tile.Colours.RED), 0, 3);
			game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 4);
			game.addTileToTable(new ObservableTile(6, Tile.Colours.RED), 0, 5);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.RED), 1, 0);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.BLUE), 1, 1);
			game.addTileToTable(new ObservableTile(2, Tile.Colours.GREEN), 1, 2);
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t11))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 3));

		assertThat(controller.getPlayer1HandPane().getChildren().size(), is(10));
		assertThat(game.getTable().get(0).getValue(), is(21));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).getValue(), is(8));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
	}

	@Test
	void testPlayerCanDragAMeldByReorganizingSeveralMeldsOnTable(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(1, Tile.Colours.BLUE);
		var t5 = new ObservableTile(2, Tile.Colours.BLUE);
		var t6 = new ObservableTile(3, Tile.Colours.BLUE);
		var t7 = new ObservableTile(1, Tile.Colours.GREEN);
		var t8 = new ObservableTile(2, Tile.Colours.GREEN);
		var t9 = new ObservableTile(3, Tile.Colours.GREEN);
		var t10 = new ObservableTile(1, Tile.Colours.ORANGE);
		var t11 = new ObservableTile(2, Tile.Colours.ORANGE);
		var t12 = new ObservableTile(3, Tile.Colours.ORANGE);
		var t13 = new ObservableTile(4, Tile.Colours.ORANGE);
		var t14 = new ObservableTile(5, Tile.Colours.ORANGE);

		var boardTile1 = new ObservableTile(4, Tile.Colours.RED);
		var boardTile2 = new ObservableTile(3, Tile.Colours.RED);
		var boardTile3 = new ObservableTile(5, Tile.Colours.RED);
		robot.interact(() -> {
			game.setStock(new Stock(new ArrayList<>(List.of(
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14
			))));
			game.dealInitialTiles();
			game.addTileToTable(boardTile1, 0, 0);
			game.addTileToTable(new ObservableTile(5, Tile.Colours.RED), 0, 1);
			game.addTileToTable(new ObservableTile(6, Tile.Colours.RED), 0, 2);
			game.addTileToTable(new ObservableTile(7, Tile.Colours.RED), 0, 3);

			game.addTileToTable(boardTile2, 1, 0);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.BLUE), 1, 1);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.GREEN), 1, 2);
			game.addTileToTable(new ObservableTile(3, Tile.Colours.ORANGE), 1, 3);

			game.addTileToTable(boardTile3, 2, 0);
			game.addTileToTable(new ObservableTile(5, Tile.Colours.BLUE), 2, 1);
			game.addTileToTable(new ObservableTile(5, Tile.Colours.GREEN), 2, 2);
			game.addTileToTable(new ObservableTile(5, Tile.Colours.ORANGE), 2, 3);
		});
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0), boardTile2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 0));
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 0, 0), boardTile1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 1));
		robot.drag(findTileButtonInPane(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 0), boardTile3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 3, 2));

		assertThat(game.getTable().size(), is(4));
		assertThat(game.getTable().get(0).isValidLength(), is(true));
		assertThat(game.getTable().get(0).getType(), is(Meld.Types.RUN));
		assertThat(game.getTable().get(1).isValidLength(), is(true));
		assertThat(game.getTable().get(1).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(2).isValidLength(), is(true));
		assertThat(game.getTable().get(2).getType(), is(Meld.Types.SET));
		assertThat(game.getTable().get(3).isValidLength(), is(true));
		assertThat(game.getTable().get(3).getType(), is(Meld.Types.RUN));
	}

	@Test
	void testPlayerWinsOnFirstTurn(FxRobot robot) {
		var t1 = new ObservableTile(1, Tile.Colours.RED);
		var t2 = new ObservableTile(2, Tile.Colours.RED);
		var t3 = new ObservableTile(3, Tile.Colours.RED);
		var t4 = new ObservableTile(4, Tile.Colours.RED);
		var t5 = new ObservableTile(5, Tile.Colours.RED);
		var t6 = new ObservableTile(6, Tile.Colours.RED);
		var t7 = new ObservableTile(7, Tile.Colours.RED);
		var t8 = new ObservableTile(1, Tile.Colours.BLUE);
		var t9 = new ObservableTile(2, Tile.Colours.BLUE);
		var t10 = new ObservableTile(3, Tile.Colours.BLUE);
		var t11 = new ObservableTile(4, Tile.Colours.BLUE);
		var t12 = new ObservableTile(5, Tile.Colours.BLUE);
		var t13 = new ObservableTile(6, Tile.Colours.BLUE);
		var t14 = new ObservableTile(7, Tile.Colours.BLUE);
		robot.interact(() -> {
			game.setStock(new Stock().shuffle());
			game.setStock(new Stock(new ArrayList<>(List.of(
					// Player 1
					t1, t2, t3, t4, t5, t6, t7,
					t8, t9, t10, t11, t12, t13, t14,
					// Todo: clean this up
					// Player 2
					new ObservableTile(1, Tile.Colours.RED),
					new ObservableTile(2, Tile.Colours.RED),
					new ObservableTile(3, Tile.Colours.RED),
					new ObservableTile(4, Tile.Colours.RED),
					new ObservableTile(5, Tile.Colours.RED),
					new ObservableTile(6, Tile.Colours.RED),
					new ObservableTile(7, Tile.Colours.RED),
					new ObservableTile(8, Tile.Colours.RED),
					new ObservableTile(9, Tile.Colours.RED),
					new ObservableTile(10, Tile.Colours.RED),
					new ObservableTile(11, Tile.Colours.RED),
					new ObservableTile(12, Tile.Colours.RED),
					new ObservableTile(13, Tile.Colours.RED),
					new ObservableTile(8, Tile.Colours.RED),
					// Player 3
					new ObservableTile(9, Tile.Colours.RED),
					new ObservableTile(10, Tile.Colours.RED),
					new ObservableTile(11, Tile.Colours.RED),
					new ObservableTile(12, Tile.Colours.RED),
					new ObservableTile(13, Tile.Colours.RED),
					new ObservableTile(1, Tile.Colours.BLUE),
					new ObservableTile(2, Tile.Colours.BLUE),
					new ObservableTile(3, Tile.Colours.BLUE),
					new ObservableTile(4, Tile.Colours.BLUE),
					new ObservableTile(5, Tile.Colours.BLUE),
					new ObservableTile(6, Tile.Colours.BLUE),
					new ObservableTile(7, Tile.Colours.BLUE),
					new ObservableTile(8, Tile.Colours.BLUE),
					new ObservableTile(9, Tile.Colours.BLUE),
					// Player 4
					new ObservableTile(10, Tile.Colours.BLUE),
					new ObservableTile(11, Tile.Colours.BLUE),
					new ObservableTile(12, Tile.Colours.BLUE),
					new ObservableTile(13, Tile.Colours.BLUE),
					new ObservableTile(8, Tile.Colours.BLUE),
					new ObservableTile(9, Tile.Colours.BLUE),
					new ObservableTile(10, Tile.Colours.BLUE),
					new ObservableTile(11, Tile.Colours.BLUE),
					new ObservableTile(12, Tile.Colours.BLUE),
					new ObservableTile(13, Tile.Colours.BLUE),
					new ObservableTile(1, Tile.Colours.GREEN),
					new ObservableTile(2, Tile.Colours.GREEN),
					new ObservableTile(3, Tile.Colours.GREEN),
					// Remaining tiles
					new ObservableTile(4, Tile.Colours.GREEN),
					new ObservableTile(5, Tile.Colours.GREEN),
					new ObservableTile(6, Tile.Colours.GREEN),
					new ObservableTile(7, Tile.Colours.GREEN),
					new ObservableTile(8, Tile.Colours.GREEN),
					new ObservableTile(9, Tile.Colours.GREEN),
					new ObservableTile(10, Tile.Colours.GREEN),
					new ObservableTile(11, Tile.Colours.GREEN),
					new ObservableTile(12, Tile.Colours.GREEN),
					new ObservableTile(13, Tile.Colours.GREEN),
					new ObservableTile(1, Tile.Colours.GREEN),
					new ObservableTile(2, Tile.Colours.GREEN),
					new ObservableTile(3, Tile.Colours.GREEN),
					new ObservableTile(4, Tile.Colours.GREEN),
					new ObservableTile(5, Tile.Colours.GREEN),
					new ObservableTile(6, Tile.Colours.GREEN),
					new ObservableTile(7, Tile.Colours.GREEN),
					new ObservableTile(8, Tile.Colours.GREEN),
					new ObservableTile(9, Tile.Colours.GREEN),
					new ObservableTile(10, Tile.Colours.GREEN),
					new ObservableTile(11, Tile.Colours.GREEN),
					new ObservableTile(12, Tile.Colours.GREEN),
					new ObservableTile(13, Tile.Colours.GREEN),
					new ObservableTile(1, Tile.Colours.ORANGE),
					new ObservableTile(2, Tile.Colours.ORANGE),
					new ObservableTile(3, Tile.Colours.ORANGE),
					new ObservableTile(4, Tile.Colours.ORANGE),
					new ObservableTile(5, Tile.Colours.ORANGE),
					new ObservableTile(6, Tile.Colours.ORANGE),
					new ObservableTile(7, Tile.Colours.ORANGE),
					new ObservableTile(8, Tile.Colours.ORANGE),
					new ObservableTile(9, Tile.Colours.ORANGE),
					new ObservableTile(10, Tile.Colours.ORANGE),
					new ObservableTile(11, Tile.Colours.ORANGE),
					new ObservableTile(12, Tile.Colours.ORANGE),
					new ObservableTile(13, Tile.Colours.ORANGE),
					new ObservableTile(1, Tile.Colours.ORANGE),
					new ObservableTile(2, Tile.Colours.ORANGE),
					new ObservableTile(3, Tile.Colours.ORANGE),
					new ObservableTile(4, Tile.Colours.ORANGE),
					new ObservableTile(5, Tile.Colours.ORANGE),
					new ObservableTile(6, Tile.Colours.ORANGE),
					new ObservableTile(7, Tile.Colours.ORANGE),
					new ObservableTile(8, Tile.Colours.ORANGE),
					new ObservableTile(9, Tile.Colours.ORANGE),
					new ObservableTile(10, Tile.Colours.ORANGE),
					new ObservableTile(11, Tile.Colours.ORANGE),
					new ObservableTile(12, Tile.Colours.ORANGE),
					new ObservableTile(13, Tile.Colours.ORANGE)
			))));
			game.dealInitialTiles();
		});
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t1))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t2))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t3))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t4))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 3));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t5))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 4));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t6))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 5));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t7))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 1, 6));

		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t8))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 0));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t9))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 1));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t10))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 2));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t11))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 3));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t12))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 4));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t13))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 5));
		robot.drag(findTileButtonInPane(controller.getPlayer1HandPane(), t14))
				.dropTo(getCellFromGridPane(robot.lookup("#gpTable").queryAs(GridPane.class), 2, 6));

		assertThat(game.getWinnerProperty().get(), is(1));
	}

	TileButton findTileButtonInPane(Pane pane, ObservableTile tile) {
		return pane.getChildren()
				.stream()
				.filter(TileButton.class::isInstance)
				.map(TileButton.class::cast)
				.filter(btn -> btn.getTile().equals(tile))
				.findFirst()
				.orElseThrow();
	}

	HBox getCellFromGridPane(GridPane gridPane, int row, int col) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
				return (HBox) node;
			}
		}
		return null;
	}
}
