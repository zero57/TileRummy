package model;

import javafx.collections.ObservableList;
import model.observable.ObservableMeld;

public class Originator {
   private ObservableList<ObservableMeld> table;
   private Hand hand;

   public void setState(ObservableList<ObservableMeld> table, Hand hand){
      this.table = table;
      this.hand = hand;
   }

   public ObservableList<ObservableMeld> getTableState() {
      return table;
   }

   public Hand getHandState() {
      return hand;
   }

   public Memento saveStateToMemento() {
      return new Memento(table, hand);
   }

   public void getStateFromMemento(Memento memento) {
      table = memento.getTableState();
      hand = memento.getHandState();
   }
}