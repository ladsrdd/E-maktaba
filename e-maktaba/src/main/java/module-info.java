module esi.emaktaba {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.sql;
  requires org.controlsfx.controls;


//  opens esi.emaktaba.controller;
  exports esi.emaktaba;
  opens esi.emaktaba;
  exports esi.emaktaba.controller;
  opens esi.emaktaba.controller;
}
