package com.example.vidalbet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class CasinoController {

    @FXML private Label lblSaldoCasino;
    private Random random = new Random();

    @FXML
    public void initialize() {
        actualitzarSaldo();
    }

    private void actualitzarSaldo() {
        lblSaldoCasino.setText(String.format("Saldo: %.2f €", SessioUsuari.saldo).replace(".", ","));
    }

    // --- NAVEGACIÓ TORNAR A L'INICI ---
    @FXML
    private void navInici(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // --- LÒGICA DE LA RULETA ---
    @FXML
    private void jugarRuleta(ActionEvent event) {
        if (SessioUsuari.saldo < 10) {
            mostrarAlerta("Saldo Insuficient", "Necessites almenys 10€ per jugar a la Ruleta.");
            return;
        }

        SessioUsuari.saldo -= 10; // Cobrem l'aposta

        boolean guanya = random.nextBoolean(); // 50% de probabilitat

        if (guanya) {
            SessioUsuari.saldo += 20; // Recupera els 10€ i guanya 10€ extres
            mostrarAlerta("🎰 Has guanyat!", "La bola ha caigut al teu color! Has guanyat 20€.");
        } else {
            mostrarAlerta("❌ Has perdut", "La bola ha caigut a l'altre color. Sort a la propera tirada!");
        }
        actualitzarSaldo();
    }

    // --- LÒGICA DE LES SLOTS ---
    @FXML
    private void jugarSlots(ActionEvent event) {
        if (SessioUsuari.saldo < 5) {
            mostrarAlerta("Saldo Insuficient", "Necessites almenys 5€ per jugar a les Slots.");
            return;
        }

        SessioUsuari.saldo -= 5; // Cobrem la tirada

        int tirada = random.nextInt(100); // Número aleatori del 0 al 99

        if (tirada < 10) {
            // 10% de probabilitat de guanyar el Pot Gros
            SessioUsuari.saldo += 50;
            mostrarAlerta("🔥 JACKPOT! 🔥", "TRES 777! Has guanyat el Pot Gros de 50€!");
        } else if (tirada < 35) {
            // 25% de probabilitat de premi menor
            SessioUsuari.saldo += 10;
            mostrarAlerta("🍒 Premi Menor!", "Dues cireres! Has guanyat 10€.");
        } else {
            // 65% de probabilitat de perdre
            mostrarAlerta("💸 Sense sort", "No hi ha hagut sort aquesta vegada. Torna a tirar la palanca!");
        }
        actualitzarSaldo();
    }

    // --- ALERTES VISUALS ---
    private void mostrarAlerta(String titol, String missatge) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titol);
        alert.setHeaderText(null);
        alert.setContentText(missatge);

        // Apliquem l'estil fosc a l'alerta
        try {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("dialog-pane");
        } catch (Exception ignored) {}

        alert.showAndWait();
    }
}