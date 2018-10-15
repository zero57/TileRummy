package ui;

public class DragContext {

	private double mouseX;
	private double mouseY;
	private double initialTranslateX;
	private double initialTranslateY;

	public DragContext() {

	}

	public void setMouseX(double x) {
		this.mouseX = x;
	}

	public void setMouseY(double y) {
		this.mouseY = y;
	}

	public void setInitialTranslateX(double x) {
		this.initialTranslateX = x;
	}

	public void setInitialTranslateY(double y) {
		this.initialTranslateY = y;
	}

	public double getMouseX() {
		return mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}

	public double getInitialTranslateX() {
		return initialTranslateX;
	}

	public double getInitialTranslateY() {
		return initialTranslateY;
	}
}
