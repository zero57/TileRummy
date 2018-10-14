package ui;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class UIHelper {
	public static Node makeDraggable(final Node node, final Node root) {
		final DragContext dragContext = new DragContext();

		node.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
			// Make the root node (and children) untargetable for MouseEvents
			// This is because the node we are dragging is always under the mouse cursor, and so the EventTarget will be
			// always targetting the node we're dragging unless we make it untargetable for MouseEvents.
			root.setMouseTransparent(true);
			dragContext.setMouseX(e.getSceneX());
			dragContext.setMouseY(e.getSceneY());
			dragContext.setInitialTranslateX(node.getTranslateX());
			dragContext.setInitialTranslateY(node.getTranslateY());
			e.consume();
		});

		node.addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
			node.startFullDrag();
			e.consume();
		});

		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
			double dx = dragContext.getInitialTranslateX() + e.getSceneX() - dragContext.getMouseX();
			double dy = dragContext.getInitialTranslateY() + e.getSceneY() - dragContext.getMouseY();
			node.setTranslateX(dx);
			node.setTranslateY(dy);
			e.consume();
		});

		node.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
			// Make the root node (and children) be targetable for MouseEvents again
			root.setMouseTransparent(false);
			node.setTranslateX(0);
			node.setTranslateY(0);
			e.consume();
		});

		return node;
	}

	public static Node makeDraggable(final Node node) {
		return makeDraggable(node, node);
	}
}
