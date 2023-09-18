module Javafx.jdbc {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens application;
    opens gui;
    opens model.entities;
    opens model.services;
}