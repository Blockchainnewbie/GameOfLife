import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.Optional;
import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

// A*-Integration
import java.util.List;
import java.util.ArrayList;
import astar.AStarAlgorithmus;
import astar.GitterModell;
import astar.GitterPosition;
import astar.ManhattanHeuristik;
import astar.ZellenZustand;

/**
 * ➤ Minimales JavaFX‑Gerüst **zum SELBST Ausfüllen**.
 * Du übst dabei Schritt für Schritt:
 *   1. Model (Spielfeld) instanzieren
 *   2. update‑Logik aufrufen
 *   3. Zellen zeichnen
 */
// extends Application macht macht diese Klasse zur JavaFX App
public class GameOfLifeFX extends Application {

    // ---------- Fenster‑ & Raster‑Setup (war mal fix → nun geändert) ----------
    private static final int CELL_SIZE = 14;  // px je Zelle, bleibt konstant
    private int width;  // wird jetzt dynamisch gesetzt
    private int height; // wird jetzt dynamisch gesetzt

    // Wir speichern eine Referenz auf das Hauptfenster (die "Stage"),
    // damit wir seine Größe später anpassen können.
    private Stage primaryStage;
    // ---------- Canvas + GraphicsContext ----------
    // Objektreferenzen auf Instanzen dieser Klassen
    private Canvas canvas;
    private GraphicsContext gc;

    // ---------- Model ----------
    // Einen Zeiger auf das Spielfeld-Objekt anlegen, hier noch null
    // Klassen-Attribut da das field in mehreren Methoden benötigt wird, start, update & render
    private Spielfeld field;

    // ---------- Timing ----------
    private long lastStep = 0;                     // ns‑Zeitstempel, 0 sorgt dafür dass das Spiel sofort startet
    private static final long STEP_INTERVAL = 250_000_000; // 250 ms in ns, wird alle 250ms neu geladen, da ansonsten 60 x sek durch handle(now) im AnimationTimer

    // ---------- State ----------
    // Wir definieren Zustände, um die Simulation zu steuern.
    private enum SimulationState { RUNNING, PAUSED };
    private SimulationState simulationState = SimulationState.PAUSED; // Das Spiel startet im Pausenmodus.

    // Wir definieren, wie das Spielfeld am Anfang gefüllt werden soll.
    private enum StartPattern { ZUFAELLIG_ALLE, ZUFAELLIG_BEGRENZT };
    private StartPattern startPattern = StartPattern.ZUFAELLIG_ALLE; // Standardwert
    private int anzahlZellen = 100; // Standardwert für die begrenzte Anzahl

    private Button startPauseButton; // Eine Referenz auf den Button, um seinen Text zu ändern.

    /**
     * Die Haupt-Einstiegsmethode für eine JavaFX-Anwendung.
     * Wird von der JavaFX-Laufzeitumgebung aufgerufen, nachdem die Anwendung gestartet wurde.
     * @param stage Das Hauptfenster (die "Bühne") der Anwendung.
     */
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage; // Die Stage in der Instanzvariable speichern.

        // 1) Layout
        // Oberer Bereich - Header
        BorderPane root = new BorderPane(); // erstellt ein Layout-Objekt mit top, cneter, bottom
        root.setTop(createHeaderArea());
        // Wir erstellen ein leeres Canvas. Die Größe wird später gesetzt.
        canvas = new Canvas();
        gc = canvas.getGraphicsContext2D(); // Ist unser Stift um auf das Canvas zu zeichnen
        root.setCenter(canvas); // Plaziert unsere Blatt Papier mittig im Fenster

        // 2) Scene & Stage
        // Wir erstellen die Szene, aber zeigen das Fenster (Stage) noch NICHT an.
        stage.setScene(new Scene(root));
        stage.setTitle("Game of Life by Sonny – JavaFX");

        // 3) ZUERST den Dialog aufrufen, um die Größe und das Muster festzulegen.
        showSizeDialog();

        // 4) JETZT, wo alles konfiguriert ist, das Fenster anzeigen.
        stage.show();

        // 4) Animation‑Loop starten
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update(now);   // Logik
                render();      // Zeichnen
            }
        }.start();
    }

    /**
     * Zeigt einen Dialog an, um die Spielfeldgröße vom Benutzer abzufragen.
     * Setzt die 'width' und 'height' Instanzvariablen.
     */
    private void showSizeDialog() {
        // 1. Einen Dialog erstellen, der uns nur mitteilt, welcher Knopf gedrückt wurde.
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Spielfeldgröße festlegen");
        dialog.setHeaderText("Bitte gib die gewünschte Größe des Spielfelds in Zellen an.");

        // --- Styling des Dialogs ---
        // Wir holen uns das Haupt-Layout-Element des Dialogs, um es zu stylen.
        javafx.scene.control.DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #471e6dff;"); // Gleicher Hintergrund wie im Header

        // 2. Die Knöpfe "Starten" und "Abbrechen" hinzufügen.
        ButtonType okButtonType = new ButtonType("Starten", ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // 3. Die Eingabefelder und deren Anordnung (Layout) erstellen.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Labels und Textfelder erstellen
        Label breiteLabel = new Label("Breite (in Zellen):");
        Label hoeheLabel = new Label("Höhe (in Zellen):");
        TextField breiteFeld = new TextField("50");
        TextField hoeheFeld = new TextField("40");

        // Labels weiß färben, damit man sie auf dem dunklen Hintergrund sieht.
        String labelStyle = "-fx-text-fill: white; -fx-font-weight: bold;";
        breiteLabel.setStyle(labelStyle);
        hoeheLabel.setStyle(labelStyle);

        // Elemente zum Grid hinzufügen
        grid.add(breiteLabel, 0, 0);
        grid.add(breiteFeld, 1, 0);
        grid.add(hoeheLabel, 0, 1);
        grid.add(hoeheFeld, 1, 1);

        // --- Neue Elemente für die Startmuster-Auswahl ---
        grid.add(new Separator(), 0, 2, 2, 1); // Visuelle Trennlinie

        Label musterLabel = new Label("Startmuster wählen:");
        musterLabel.setStyle(labelStyle);
        grid.add(musterLabel, 0, 3, 2, 1);

        ToggleGroup musterGruppe = new ToggleGroup();

        RadioButton zufallAlleRadio = new RadioButton("Alle Zellen zufällig");
        zufallAlleRadio.setToggleGroup(musterGruppe);
        zufallAlleRadio.setSelected(true); // Standardauswahl
        zufallAlleRadio.setStyle(labelStyle);

        RadioButton zufallBegrenztRadio = new RadioButton("Anzahl festlegen:");
        zufallBegrenztRadio.setToggleGroup(musterGruppe);
        zufallBegrenztRadio.setStyle(labelStyle);

        TextField anzahlFeld = new TextField(String.valueOf(this.anzahlZellen));

        // Die Logik, ob der Wert benutzt wird, entscheidet sich erst nach dem Klick auf "Starten".

        grid.add(zufallAlleRadio, 0, 4, 2, 1);
        grid.add(zufallBegrenztRadio, 0, 5);
        grid.add(anzahlFeld, 1, 5);

        dialogPane.setContent(grid); // Das Layout zum Dialog hinzufügen.

        // Den "Starten"-Button holen und stylen, damit er aussieht wie im Hauptfenster.
        Button okButton = (Button) dialogPane.lookupButton(okButtonType);
        okButton.setStyle("-fx-background-color: #08ff08ff; -fx-text-fill: #471e6dff; -fx-font-weight: bold;");

        // 4. Den Dialog anzeigen und auf eine Benutzereingabe warten.
        Optional<ButtonType> result = dialog.showAndWait();

        // 5. Das Ergebnis Schritt für Schritt auswerten.
        if (result.isPresent() && result.get() == okButtonType) {
            // Der Benutzer hat "Starten" geklickt.
            try {
                int breiteInZellen = Integer.parseInt(breiteFeld.getText());
                int hoeheInZellen = Integer.parseInt(hoeheFeld.getText());

                StartPattern gewaehltesMuster = StartPattern.ZUFAELLIG_ALLE;
                
                int gewaehlteAnzahl = this.anzahlZellen;

                if (breiteInZellen > 0 && hoeheInZellen > 0) {
                    // Die Auswahl des Startmusters auslesen.
                    if (musterGruppe.getSelectedToggle() == zufallBegrenztRadio) {
                        gewaehltesMuster = StartPattern.ZUFAELLIG_BEGRENZT;
                        try {
                            gewaehlteAnzahl = Integer.parseInt(anzahlFeld.getText());
                        } catch (NumberFormatException ex) {
                            System.out.println("Ungültige Anzahl, verwende Standardwert: " + gewaehlteAnzahl);
                        }
                    }
                    // Die zentrale Methode zum Neuaufbau des Spiels aufrufen.
                    reinitializeGame(breiteInZellen, hoeheInZellen, gewaehltesMuster, gewaehlteAnzahl);
                } else {
                    // Fallback, falls negative Zahlen eingegeben wurden.
                    System.out.println("Negative Größe ist nicht erlaubt. Es wurde nichts geändert.");
                    if (field == null) { // Nur beim allerersten Start
                        reinitializeGame(40, 40, StartPattern.ZUFAELLIG_ALLE, 100);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Ungültige Eingabe. Es wurde nichts geändert.");
                if (field == null) { // Nur beim allerersten Start
                    reinitializeGame(40, 40, StartPattern.ZUFAELLIG_ALLE, 100);
                }
            }
        } else {
            // Der Benutzer hat "Abbrechen" geklickt oder das Fenster geschlossen.
            // Wenn das Spiel noch gar nicht existiert, erstellen wir ein Standard-Spielfeld.
            if (field == null) {
                reinitializeGame(40, 40, StartPattern.ZUFAELLIG_ALLE, 100);
            }
            // Ansonsten passiert bei "Abbrechen" einfach nichts.
        }
    }

    /**
     * Baut das Spiel (neu) auf. Setzt die Größe, erstellt das Spielfeld und das Canvas.
     * @param breiteInZellen Die neue Breite in Zellen.
     * @param hoeheInZellen Die neue Höhe in Zellen.
     * @param pattern Das zu verwendende Startmuster.
     * @param anzahl Die Anzahl der Zellen für das begrenzte Muster.
     */
    private void reinitializeGame(int breiteInZellen, int hoeheInZellen, StartPattern pattern, int anzahl) {
        // 1. Pixel-Größe berechnen und speichern.
        this.width = breiteInZellen * CELL_SIZE;
        this.height = hoeheInZellen * CELL_SIZE;

        // 2. Das Canvas auf die neue Größe anpassen.
        canvas.setWidth(this.width);
        canvas.setHeight(this.height);

        // 3. Startmuster speichern und verwenden
        this.startPattern = pattern;
        this.anzahlZellen = anzahl;
        
        // 4. Ein neues Spielfeld-Objekt mit den neuen Maßen erstellen.
        field = new Spielfeld(breiteInZellen, hoeheInZellen);
        if (pattern == StartPattern.ZUFAELLIG_BEGRENZT) {
            field.zufaelligBelebenBegrenzt(anzahl);
        } else {
            field.zufaelligBelebenAlle();
        }

        // 4. Den Zustand zurücksetzen und das neue Feld sofort zeichnen.
        this.simulationState = SimulationState.PAUSED;
        this.startPauseButton.setText("Start");
        render();

        // 5. Die Größe des Fensters an den neuen Inhalt anpassen.
        this.primaryStage.sizeToScene();
    }

    /**
     * Erstellt den Header-Bereich
     */
    private VBox createHeaderArea() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #471e6dff; -fx-text-fill: white;");
        
        // Haupttitel
        Label title = new Label("Conway`s Game Of Life!");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        
        // Untertitel
        Label subtitle = new Label("Ein Projekt zum vertiefen der OOP mit Java!");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.LIGHTGRAY);
        
        // Buttons erstellen
        // Der "Start"-Button wird zum Start/Pause-Umschaltknopf.
        startPauseButton = new Button("Start");
        Button resetButton = new Button("Reset");
        Button resizeButton = new Button("Größe ändern");
        
        // Neuer Button für A*-Start (vorerst nur UI + Log)
        Button startAStarButton = new Button("A* starten");

        

        // Button-Styling
        String buttonStyle = "-fx-background-color: #08ff08ff; -fx-text-fill: #471e6dff; -fx-font-weight: bold;";
        startPauseButton.setStyle(buttonStyle);
        resetButton.setStyle(buttonStyle);
        resizeButton.setStyle(buttonStyle);
        startAStarButton.setStyle(buttonStyle);

        // Event-Handler für den Start/Pause-Button
        startPauseButton.setOnAction(event -> {
            if (simulationState == SimulationState.PAUSED) {
                simulationState = SimulationState.RUNNING;
                startPauseButton.setText("Pause");
            } else {
                simulationState = SimulationState.PAUSED;
                startPauseButton.setText("Start");
            }
        });

        // Event-Handler für den Reset-Button
        resetButton.setOnAction(event -> {
            if (field != null) {
                // Die jeweilige Methode zum Beleben aufrufen.
                // Diese kümmert sich intern darum, das Feld vorher zu leeren.
                if (startPattern == StartPattern.ZUFAELLIG_BEGRENZT) {
                    field.zufaelligBelebenBegrenzt(anzahlZellen);
                } else {
                    field.zufaelligBelebenAlle();
                }
                // Nach dem Reset immer in den Pause-Zustand wechseln.
                simulationState = SimulationState.PAUSED;
                startPauseButton.setText("Start");
                render(); // Das zurückgesetzte Feld sofort anzeigen.
            }
        });

        // Event-Handler für A* Button
        startAStarButton.setOnAction(e -> {
            if(field == null)
            {
                System.out.println("Kein Spielfeld vorhanden!");
                return;
            }

            System.out.println("Spielfeld Breite: " + field.getBreite());
            System.out.println("Spielfeld Höhe: " + field.getHoehe());

        
            // Matrix aus dem aktuellen Spielfeld holen: true = lebend (Hindernisse)
            boolean[][] matrix = GridAdapter.toMatrix(field);

            // Matrixgröße ausgeben
            System.out.println("Matrix Größe: " + matrix.length + " x " + (matrix.length > 0 ? matrix[0].length : 0));

            // Beispiel: Ausgabe der ersten 5x5 Zellen
            for (int y = 0; y < Math.min(5, matrix.length); y++) {
                for (int x = 0; x < Math.min(5, matrix[y].length); x++) {
                    System.out.print(matrix[y][x] ? "1 " : "0 ");
                }
                System.out.println();
            }
            
            int h = matrix.length;
            int w = (h > 0) ? matrix[0].length : 0;

            System.out.println("Matrix Größe: " + h + " x " + w);
            System.out.println("Matrix-Inhalt:");

            for (int i = 0; i < h; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < w; j++) {
                    row.append(matrix[i][j] ? "1" : "0");
                }
                System.out.println(row.toString());
            }

            if (h > 0) {
                w = matrix[0].length;
            } else {
                w = 0;
            }

        });

        // Event-Handler für den "Größe ändern"-Button
        resizeButton.setOnAction(e -> showSizeDialog());

        // Alle Elemente zum Header hinzufügen
        header.getChildren().addAll(
                                    title, subtitle,
                                    startPauseButton, resetButton, resizeButton,
                                    startAStarButton
                                );


        return header;
    }

    /**
     * Aktualisiert den Zustand des Spiels (das "Model").
     * Diese Methode wird in jedem Frame des AnimationTimers aufgerufen.
     * @param now Der aktuelle Zeitstempel in Nanosekunden, wird vom AnimationTimer übergeben.
     */
    private void update(long now) {
        // (Schritt 2) Die Spiellogik wird nur ausgeführt, wenn der Zustand RUNNING ist.
        if (simulationState != SimulationState.RUNNING) { // Der "Wächter"
            return; // Nichts tun, wenn pausiert.
        }

        if (now - lastStep >= STEP_INTERVAL) { // Zeitsteuerung beibehalten
            field.berechneNaechsteGeneration();
            lastStep = now;
        }
        
    }

    /**
     * Zeichnet den aktuellen Zustand des Spielfelds auf das Canvas.
     */
    private void render() {
        // Hintergrund vorbereiten
        gc.setFill(Color.DARKMAGENTA);
        gc.fillRect(0, 0, this.width, this.height);
        
        // Hier die Schleifen zum Zeichnen
        gc.setFill(Color.LIMEGREEN);
        // Wir fragen das Spielfeld direkt nach seiner Größe.
        for (int y = 0; y < field.getHoehe(); y++) {
            for (int x = 0; x < field.getBreite(); x++) {
                if (field.raster[y][x].getIstLebendig()) {
                    // Die Koordinatenberechnung x * CELL_SIZE und y * CELL_SIZE transformiert die 
                    // Raster-Koordinaten in Pixel-Koordinaten, indem jede Zelle um die CELL_SIZE versetzt wird.
                    // Die Größe CELL_SIZE - 1 für Breite und Höhe sorgt für einen 1-Pixel-Abstand zwischen den Zellen, 
                    // wodurch ein Raster-Effekt entsteht und die einzelnen Zellen visuell voneinander abgegrenzt werden.
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE - 1, CELL_SIZE - 1);
                }
            }
        }
    }

    /**
     * Die main-Methode, die die JavaFX-Anwendung startet.
     * @param args Kommandozeilenargumente (werden hier nicht verwendet).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
