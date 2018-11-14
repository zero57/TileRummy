# Tile Rummy
A Tile Rummy game written in Java 11 and JavaFX. You will need Maven and OpenJDK
11 to compile the game.

You will need:
* `maven >= 3.5.4`
* `openjdk-11`

## Authors
Abrar Kazi  
An Ha  
Grayson Lafreniere  

# How to Run Game
`mvn compile exec:java`

# How to run tests

## In Headed Mode
This will run the Unit and GUI tests, where the GUI tests are ran in headed
mode (will use your mouse and screen to test). Note, when running the tests in
headed mode you should **not** move your mouse or the tests will fail.

`mvn test`

## In Headless Mode
This will run the Unit and GUI tests, where the GUI tests are ran in headless
mode. In headless mode, the GUI tests are ran not using your mouse/screen, but
rather its own internal display.

`mvn test -Dtestfx.headless=true -Dprism.order=sw`

### How to run UI tests faster (move mouse faster)
You can run the tests faster by adding the option
`-Dtestfx.robot.move_max_count=1`. The default is `200` and `1` is the minimum
you can set it to. Where the lower the number makes it faster.

**Example in Headed Mode**

`mvn test -Dtestfx.robot.move_max_count=1`

**Example in Headless Mode**

`mvn test -Dtestfx.headless=true -Dprism.order=sw -Dtestfx.robot.move_max_count=1`
