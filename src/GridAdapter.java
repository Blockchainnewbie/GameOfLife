// Adapter: übersetzt das Spielfeld in eine Hindernis-Matrix für den A*.
// Idee: Lebende Zellen = Hindernisse (true), tote Zellen = frei (false).
// Vorteil: A* bleibt komplett unabhängig von Zelle/Spielfeld/JavaFX.
// Diese Utility-Klasse enthält nur statische Hilfsmethoden.

public final class GridAdapter {

    private GridAdapter() 
    {
        // Utility-Klasse: keine Instanzen nötig, besteht nur aus statischen Methoden
        // Verhindert das der Adapter wie ein Objekt behandelt wird
    }

    /**
     * Baut aus dem Spielfeld eine einfache Hindernis-Matrix.
     * @param f das aktuelle Spielfeld
     * @return boolean[hoehe][breite] – true = blockiert (lebendig), false = frei (tot)
     */
    public static boolean[][] zuMatrix(Spielfeld f) 
    {
        // Höhe und Breite aus dem Spielfeld lesen
        int hoehe = f.getHoehe();
        int breite = f.getBreite();


        // Matrix für den A*
    boolean[][] matrix = new boolean[hoehe][breite];

        // Alle Zellen durchgehen
        // Dann in Matrix eintragen ob lebendig oder tot
        for (int y = 0; y < hoehe; y++) 
        {
            for (int x = 0; x < breite; x++)
            {
                matrix[y][x] = f.istLebendig(x, y);
            }
        }


        return matrix;
    }
}

