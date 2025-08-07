package astar;
/**
 * Implementiert die Manhattan-Heuristik zur Berechnung der geschätzten Kosten
 * zwischen zwei Gitterpositionen. Diese Heuristik wird oft im A*-Algorithmus
 * verwendet, wenn Bewegungen nur horizontal und vertikal erlaubt sind (keine Diagonalen).
 */
public class ManhattanHeuristik implements Heuristik {

    /**
     * Berechnet die Manhattan-Distanz zwischen zwei Gitterpositionen.
     * Die Manhattan-Distanz ist die Summe der absoluten Differenzen ihrer Koordinaten.
     *
     * @param start Die Startposition.
     * @param ziel Die Zielposition.
     * @return Die berechnete Manhattan-Distanz als double.
     */
    @Override
    public double berechne(GitterPosition start, GitterPosition ziel) {
        // Berechnet die absolute Differenz der Spalten (x-Achse).
        int dx = Math.abs(start.getSpalte() - ziel.getSpalte());
        // Berechnet die absolute Differenz der Zeilen (y-Achse).
        int dy = Math.abs(start.getZeile()   - ziel.getZeile());
        // Die Manhattan-Distanz ist die Summe der absoluten Differenzen.
        return dx + dy;
    }
}