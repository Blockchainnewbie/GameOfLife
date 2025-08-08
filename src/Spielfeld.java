public class Spielfeld 
{
    // Das Spielfeld als Konsolenausgabe im Spiel des Lebens
    // Es besteht aus 50 x 50 Zellen
    // deklariert ein 2d-Array des Typs Zelle und Namen raster
    Zelle[][] raster;
    private int hoehe; // hoehe des Spielfelds
    private int breite; // breite des Spielfelds
   
    /**
     * Konstruktor für das Spielfeld.
     * @param breite Die Breite des Spielfelds in Zellen.
     * @param hoehe Die Höhe des Spielfelds in Zellen.
     */
    public Spielfeld(int breite, int hoehe)
    {
        this.breite = breite;
        this.hoehe = hoehe;
       
        raster = new Zelle [hoehe][breite]; // Initialisiert das 2D-Array mit der Breite und Höhe

        for (int i = 0; i < hoehe; i++)
        {
            for (int j = 0;  j < breite; j++)
            {
                raster[i][j] = new Zelle (false);
            }
        }

    }

    /**
     * Methode zum Ausgeben des Spielfelds auf der Konsole.
     * Jede Zelle wird als '#' (lebendig) oder ' ' (tot) dargestellt.
     */
    public void anzeigen()
    {
        // Gehe jede Zeile des Spielfelds durch
        for (int i = 0; i < hoehe; i++)
        {
            // Gehe jede Spalte der aktuellen Zeile durch
            for (int j = 0; j < breite; j++)
            {
                // Prüfe, ob die aktuelle Zelle lebendig ist
                if (raster[i][j].getIstLebendig())
                {
                    // Lebendige Zelle als '#' ausgeben
                    System.out.print('#');
                }
                else
                {
                    // Tote Zelle als Leerzeichen ausgeben
                    System.out.print(' ');
                }
            }
            // Nach jeder Zeile einen Zeilenumbruch ausgeben
            System.out.println();
        }
    }

    /**
     * Setzt alle Zellen auf dem Spielfeld auf "tot" zurück.
     */
    public void leeren() {
        for (int i = 0; i < hoehe; i++) {
            for (int j = 0; j < breite; j++) {
                raster[i][j].setIstLebendig(false);
            }
        }
    }

    /**
     * Belebt das gesamte Spielfeld zufällig mit etwa 50 % lebenden Zellen.
     * Gut für Tests oder chaotische Simulationen.
     */
    public void zufaelligBelebenAlle()
    {
        // Die maximale Anzahl an Durchläufen (Zellen) ist bekannt deshalb hier die for-Schleife da jede Zelle genau einmal besucht werden muss, dann wird entschieden ob lebendig oder tot 
        for ( int i = 0; i < hoehe; i++)
        {
            for ( int j = 0; j < breite; j++)
            {
                // Math.random ist eine Java Funktion zum erzeugen einer Zufallszahl zwischen 0.0 - 1.0
                // Wir benötigen diese Funktion um eine zufällige Anzahl von Zellen lebendig zu machen
                double zufall = Math.random();
                // Ein Wert kleiner oder gleich 0.5 erzeugt eine lebendige Zelle, ein Wert über 0.5 erzeugt eine tote
                // Nutz den Setter der Zelle Klasse um den Zustand zusetzen
                if (zufall <= 0.5)
                {
                    raster[i][j].setIstLebendig(true);
                }
                else
                {
                    raster[i][j].setIstLebendig(false);
                }
            }
        }
    }

    /**
     * Methode zum zufälligen Beleben einer begrenzten Anzahl von Zellen.
     * Belebt genau 'anzahl' Zellen zufällig auf dem Spielfeld.
     * @param anzahl Die genaue Anzahl der Zellen, die belebt werden sollen.
     */
    public void zufaelligBelebenBegrenzt(int anzahl)
    {
        leeren(); // WICHTIG: Das Spielfeld zuerst komplett leeren.
        int count = 0; // Zähler für lebendig gemachte Zellen
        // Da wir nicht wissen wie oft der Zufalls Generator die gleiche Zelle trifft, nehmen wir die  while und erhöhen den Zähler nur, wenn tatsächlich eine neue Zelle belebt wurde.
        while (count < anzahl)
        {
            // Zufällige Position im Spielfeld wählen
            int i = (int)(Math.random() * hoehe);
            int j = (int)(Math.random() * breite);

            // Prüfen, ob die gewählte Zelle noch tot ist
            /*
             * Ohne diese Prüfung könnte der Zufallsgenerator mehrmals dieselbe Zelle auswählen. 
             * Wenn wir einfach eine for-Schleife hätten, die 10-mal läuft und zufällige Zellen belebt, 
             * könnten wir am Ende vielleicht nur 8 oder 9 lebende Zellen haben, weil eine Zelle doppelt "getroffen" wurde.
             */
            if (!raster[i][j].getIstLebendig()) // Hier wird geprüft ob die Zelle nicht lebt
            {
                // Zelle lebendig machen
                raster[i][j].setIstLebendig(true);
                count++; // Zähler erhöhen
            }
        }
    }

    /**
     * Methode zum Erzeugen der nächsten Generation nach den Regeln von Conway.
     */
        /**
     * Führt einen Simulationsschritt nach den Regeln von Conway's Game of Life aus
     * und gibt zurück, welche Zellen sich im Zustand geändert haben.
     *
     * @return Eine 2D-boolean-Matrix mit denselben Dimensionen wie das Spielfeld.
     *         true = Zustand der Zelle hat sich geändert, false = Zustand unverändert.
     */
    public boolean[][] stepAndReportChanged() {
        // Neues temporäres Raster für die nächste Generation
        Zelle[][] neuesRaster = new Zelle[hoehe][breite];

        // Matrix zur Erfassung aller Änderungen
        boolean[][] changed = new boolean[hoehe][breite];

        // Spielfeld Zelle für Zelle durchgehen
        for (int i = 0; i < hoehe; i++) {
            for (int j = 0; j < breite; j++) {
                int lebendeNachbarn = 0; // Zähler für lebende Nachbarn

                // Alle 8 Nachbarn überprüfen
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        // Die aktuelle Zelle selbst überspringen
                        if (x == 0 && y == 0) continue;

                        int nachbarVonI = i + x;
                        int nachbarVonJ = j + y;

                        // Prüfen, ob Nachbar innerhalb der Spielfeldgrenzen liegt
                        if (nachbarVonI >= 0 && nachbarVonI < hoehe &&
                            nachbarVonJ >= 0 && nachbarVonJ < breite) {
                            if (raster[nachbarVonI][nachbarVonJ].getIstLebendig()) {
                                lebendeNachbarn++;
                            }
                        }
                    }
                }

                // Neue Zelle erzeugen (standardmäßig tot)
                neuesRaster[i][j] = new Zelle(false);

                boolean vorherLebendig = raster[i][j].getIstLebendig();
                boolean nachherLebendig = vorherLebendig;

                // Regeln von Conway anwenden
                if (vorherLebendig) {
                    // Unterbevölkerung oder Überbevölkerung → stirbt
                    if (lebendeNachbarn < 2 || lebendeNachbarn > 3) {
                        nachherLebendig = false;
                    }
                } else {
                    // Wiederbelebung bei genau 3 lebenden Nachbarn
                    if (lebendeNachbarn == 3) {
                        nachherLebendig = true;
                    }
                }

                // Neuen Zustand setzen
                neuesRaster[i][j].setIstLebendig(nachherLebendig);

                // Änderung merken
                if (vorherLebendig != nachherLebendig) {
                    changed[i][j] = true;
                }
            }
        }

        // Spielfeld aktualisieren
        raster = neuesRaster;

        // Änderungsmatrix zurückgeben
        return changed;
    }

    /**
     * Alte API für einen Simulationsschritt – behält Kompatibilität bei.
     * Diese Methode delegiert intern an {@link #stepAndReportChanged()}, 
     * ignoriert aber den Rückgabewert.
     */
    public void berechneNaechsteGeneration() {
        // Rückgabewert wird hier nicht benötigt
        stepAndReportChanged();
    }


    /**
     * Gibt die Höhe des Spielfelds zurück.
     * @return Die Höhe in Zellen.
     */
    public int getHoehe()
    {
        return this.hoehe;
    }

    /**
     * Gibt die Breite des Spielfelds zurück.
     * @return Die Breite in Zellen.
     */
    public int getBreite()
    {
        return this.breite;
    }

    // Wir brauchen hier einen Getter, als eine Art Abstraktionsscichtfür die Abfrage ob die Zelle lebt
    // Zugriff auf Zellen nur über Spielfeld-Methoden (z. B. isAlive(x, y)).
    // So bleibt die interne Datenstruktur (raster) gekapselt und kann später
    // leicht geändert werden, ohne dass anderer Code angepasst werden muss.
    // Erhöht Flexibilität, Lesbarkeit und vereinfacht z. B. A*-Integration.

    public boolean isAlive(int x, int y) 
    {
        return raster[y][x].getIstLebendig();
    }

}
