module ca.ucalgary.seng300.p4.condensation.condensation {
//    requires javafx.controls;
//    requires javafx.fxml;
//    requires javafx.web;
//
//    requires org.controlsfx.controls;
//    requires com.dlsc.formsfx;
//    requires net.synedra.validatorfx;
//    requires org.kordamp.ikonli.javafx;
//    requires org.kordamp.bootstrapfx.core;
//    requires eu.hansolo.tilesfx;
//    requires com.almasb.fxgl.all;
    requires json.simple;
    requires org.json;
    opens ca.ucalgary.seng300.p4.condensation.condensation to javafx.fxml;
    exports ca.ucalgary.seng300.p4.condensation.condensation;
    exports ca.ucalgary.seng300.p4.condensation.condensation.Backend;
    exports ca.ucalgary.seng300.p4.condensation.condensation.Networking;
}