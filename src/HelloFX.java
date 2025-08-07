import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * JavaFX Boilerplate - Visuelle Vorlage für Fensteranwendungen
 * Demonstriert verschiedene UI-Komponenten, Layouts und Animationen
 * JavaFX ist ein Framework zur Erstellung von GUI-Anwendungen in Java.
 */
public class HelloFX extends Application {
    
    // Instanzvariablen für UI-Komponenten
    private Timeline animationTimeline;
    private Text animatedText;
    private Rectangle coloredShape;
    private boolean movingRight = true; // Richtung der Textbewegung
    
    @Override
    public void start(Stage primaryStage) {
        // === FENSTER KONFIGURATION ===
        primaryStage.setTitle("JavaFX Boilerplate - Fenster Vorlage");
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(500);
        
        // === HAUPTLAYOUT ERSTELLEN ===
        BorderPane mainLayout = createMainLayout();
        
        // === SZENE ERSTELLEN UND ANZEIGEN ===
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Animation direkt starten
        startAnimation();
    }
    
    /**
     * Erstellt das Hauptlayout mit verschiedenen Bereichen
     */
    private BorderPane createMainLayout() {
        BorderPane borderPane = new BorderPane();
        
        // Oberer Bereich - Header
        borderPane.setTop(createHeaderArea());
        
        // Linker Bereich - Navigation/Buttons
        borderPane.setLeft(createNavigationArea());
        
        // Mittlerer Bereich - Hauptinhalt
        borderPane.setCenter(createContentArea());
        
        // Rechter Bereich - Eigenschaften/Einstellungen
        borderPane.setRight(createPropertiesArea());
        
        // Unterer Bereich - Status/Information
        borderPane.setBottom(createStatusArea());
        
        return borderPane;
    }
    
    /**
     * Erstellt den Header-Bereich
     */
    private VBox createHeaderArea() {
        VBox header = new VBox(10);
        header.setPadding(new Insets(15));
        header.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white;");
        
        // Haupttitel
        Label title = new Label("JavaFX Anwendung - Boilerplate");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        
        // Untertitel
        Label subtitle = new Label("Beispiel für verschiedene UI-Komponenten und Layouts");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setTextFill(Color.LIGHTGRAY);
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    /**
     * Erstellt den Navigationsbereich mit Buttons
     */
    private VBox createNavigationArea() {
        VBox navigation = new VBox(10);
        navigation.setPadding(new Insets(15));
        navigation.setPrefWidth(150);
        navigation.setStyle("-fx-background-color: #34495e;");
        
        Label navTitle = new Label("Navigation");
        navTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        navTitle.setTextFill(Color.WHITE);
        
        // Verschiedene Button-Typen
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resetButton = new Button("Reset");
        Button exitButton = new Button("Beenden");
        
        // Button-Styling
        String buttonStyle = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;";
        startButton.setStyle(buttonStyle);
        pauseButton.setStyle(buttonStyle);
        resetButton.setStyle(buttonStyle);
        exitButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        
        // Button-Breite anpassen
        startButton.setPrefWidth(120);
        pauseButton.setPrefWidth(120);
        resetButton.setPrefWidth(120);
        exitButton.setPrefWidth(120);
        
        // Event-Handler für Buttons
        startButton.setOnAction(e -> System.out.println("Start gedrückt"));
        pauseButton.setOnAction(e -> toggleAnimation());
        resetButton.setOnAction(e -> resetAnimation());
        exitButton.setOnAction(e -> Platform.exit());
        
        navigation.getChildren().addAll(navTitle, startButton, pauseButton, resetButton, 
                                      new Separator(), exitButton);
        return navigation;
    }
    
    /**
     * Erstellt den Hauptinhaltsbereich
     */
    private VBox createContentArea() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #ecf0f1;");
        
        // Bereichstitel
        Label contentTitle = new Label("Hauptinhaltsbereich");
        contentTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        // Container für animierten Text (Pane ermöglicht freie Positionierung)
        Pane animationPane = new Pane();
        animationPane.setPrefHeight(60);
        animationPane.setPrefWidth(500); // Feste Breite für Bewegungsbereich
        animationPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 1;");
        
        // Animierter Text
        animatedText = new Text(10, 30, "← Text pendelt hin und her →");
        animatedText.setFill(Color.BLUE);
        animatedText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // Text zum Pane hinzufügen
        animationPane.getChildren().add(animatedText);
        
        // Farbige Form
        coloredShape = new Rectangle(100, 50);
        coloredShape.setFill(Color.GREEN);
        coloredShape.setStroke(Color.DARKGREEN);
        coloredShape.setStrokeWidth(2);
        
        // GridPane für strukturierte Inhalte
        GridPane grid = createSampleGrid();
        
        content.getChildren().addAll(contentTitle, animationPane, coloredShape, grid);
        return content;
    }
    
    /**
     * Erstellt ein Beispiel-GridPane
     */
    private GridPane createSampleGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);
        
        // Eingabefelder und Labels
        grid.add(new Label("Name:"), 0, 0);
        grid.add(new TextField("Beispieltext"), 1, 0);
        
        grid.add(new Label("Alter:"), 0, 1);
        grid.add(new TextField("25"), 1, 1);
        
        grid.add(new Label("Stadt:"), 0, 2);
        ComboBox<String> cityCombo = new ComboBox<>();
        cityCombo.getItems().addAll("Berlin", "München", "Hamburg", "Köln");
        cityCombo.setValue("Berlin");
        grid.add(cityCombo, 1, 2);
        
        return grid;
    }
    
    /**
     * Erstellt den Eigenschaften-/Einstellungsbereich
     */
    private VBox createPropertiesArea() {
        VBox properties = new VBox(15);
        properties.setPadding(new Insets(15));
        properties.setPrefWidth(200);
        properties.setStyle("-fx-background-color: #95a5a6;");
        
        Label propTitle = new Label("Eigenschaften");
        propTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        
        // CheckBoxes
        CheckBox option1 = new CheckBox("Animation aktiviert");
        CheckBox option2 = new CheckBox("Sound aktiviert");
        CheckBox option3 = new CheckBox("Vollbildmodus");
        option1.setSelected(true);
        
        // RadioButtons
        Label radioLabel = new Label("Darstellungsmodus:");
        ToggleGroup displayGroup = new ToggleGroup();
        RadioButton mode1 = new RadioButton("Normal");
        RadioButton mode2 = new RadioButton("Kompakt");
        RadioButton mode3 = new RadioButton("Erweitert");
        mode1.setToggleGroup(displayGroup);
        mode2.setToggleGroup(displayGroup);
        mode3.setToggleGroup(displayGroup);
        mode1.setSelected(true);
        
        // Slider
        Label sliderLabel = new Label("Geschwindigkeit:");
        Slider speedSlider = new Slider(1, 10, 5);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setMajorTickUnit(2);
        
        properties.getChildren().addAll(propTitle, option1, option2, option3,
                                       new Separator(), radioLabel, mode1, mode2, mode3,
                                       new Separator(), sliderLabel, speedSlider);
        return properties;
    }
    
    /**
     * Erstellt den Statusbereich
     */
    private HBox createStatusArea() {
        HBox status = new HBox(20);
        status.setPadding(new Insets(10));
        status.setAlignment(Pos.CENTER_LEFT);
        status.setStyle("-fx-background-color: #7f8c8d;");
        
        Label statusLabel = new Label("Status: Bereit");
        statusLabel.setTextFill(Color.WHITE);
        
        Label timeLabel = new Label("Zeit: " + java.time.LocalTime.now().toString().substring(0, 8));
        timeLabel.setTextFill(Color.WHITE);
        
        // Fortschrittsbalken
        ProgressBar progressBar = new ProgressBar(0.0);
        progressBar.setPrefWidth(200);
        
        status.getChildren().addAll(statusLabel, timeLabel, progressBar);
        return status;
    }
    
    /**
     * Startet die Animation
     */
    private void startAnimation() {
        animationTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0), e -> {
                // Text-Animation - pendelt hin und her
                double currentX = animatedText.getX();
                double leftLimit = 10;    // Linke Grenze
                double rightLimit = 160;  // Rechte Grenze
                
                // Neue Position berechnen
                double newX;
                if (movingRight) {
                    newX = currentX + 2;
                    // Richtung umkehren wenn rechte Grenze erreicht
                    if (newX >= rightLimit) {
                        movingRight = false;
                        newX = rightLimit; // An rechter Grenze stoppen
                    }
                } else {
                    newX = currentX - 2;
                    // Richtung umkehren wenn linke Grenze erreicht
                    if (newX <= leftLimit) {
                        movingRight = true;
                        newX = leftLimit; // An linker Grenze stoppen
                    }
                }
                animatedText.setX(newX);
                
                // Form-Animation (Farbwechsel)
                Color currentColor = (Color) coloredShape.getFill();
                if (currentColor.equals(Color.GREEN)) {
                    coloredShape.setFill(Color.ORANGE);
                } else if (currentColor.equals(Color.ORANGE)) {
                    coloredShape.setFill(Color.RED);
                } else {
                    coloredShape.setFill(Color.GREEN);
                }
            }),
            new KeyFrame(Duration.seconds(0.05))
        );
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
        animationTimeline.play();
    }
    
    /**
     * Pausiert/Startet die Animation
     */
    private void toggleAnimation() {
        if (animationTimeline.getStatus() == Timeline.Status.RUNNING) {
            animationTimeline.pause();
        } else {
            animationTimeline.play();
        }
    }
    
    /**
     * Setzt die Animation zurück
     */
    private void resetAnimation() {
        animationTimeline.stop();
        animatedText.setX(10); // Startposition zurücksetzen
        movingRight = true; // Richtung zurücksetzen
        coloredShape.setFill(Color.GREEN);
        animationTimeline.play();
    }

    
    /**
     * Die Hauptmethode startet die JavaFX-Anwendung
     */
    public static void main(String[] args) {
        launch(args); // Initialisiert die JavaFX-Laufzeitumgebung und ruft start() auf
    }
}
