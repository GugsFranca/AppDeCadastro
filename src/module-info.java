module Javafx.jdbc {

    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    opens application;
    opens gui;
    opens model.entities;
    opens model.services;
}