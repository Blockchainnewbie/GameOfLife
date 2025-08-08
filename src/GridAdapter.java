// Adapter: übersetzt das Spielfeld in eine Hindernis-Matrix für den A*.
// Idee: Lebende Zellen = Hindernisse (true), tote Zellen = frei (false).
// Vorteil: A* bleibt komplett unabhängig von Zelle/Spielfeld/JavaFX.

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
    public static boolean[][] toMatrix(Spielfeld f) 
    {
        // Höhe und Breite aus dem Spielfeld lesen
        int h = f.getHoehe();
        int w = f.getBreite();

        System.out.println("Spielfeld Höhe (Zeilen): " + f.getHoehe());
        System.out.println("Spielfeld Breite (Spalten): " + f.getBreite());


        // Matrix für den A*
        boolean[][] matrix = new boolean[h][w];

        // Alle Zellen durchgehen
        // Dann in Matrix eintragen ob lebendig oder tot
        for (int y = 0; y < h; y++) 
        {
            for (int x = 0; x < w; x++)
            {
                matrix[y][x] = f.isAlive(x, y);
            }
        }


        return matrix;
    }
}

