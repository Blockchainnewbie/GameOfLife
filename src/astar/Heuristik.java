package astar;
/**
 * Diese Schnittstelle definiert das Verhalten einer Heuristik-Funktion,
 * die im A*-Algorithmus zur Schätzung der Kosten vom aktuellen Knoten zum Zielknoten verwendet wird.
 * Eine Heuristik muss eine "optimistische" (zulässige) Schätzung liefern,
 * d.h. sie darf die tatsächlichen Kosten niemals überschätzen, um die Optimalität des A*-Algorithmus zu gewährleisten.
 */
public interface Heuristik {
    /**
     * Berechnet die heuristischen Kosten (geschätzte Entfernung) zwischen zwei Gitterpositionen.
     *
     * @param start Die Startposition für die Heuristikberechnung.
     * @param ziel Die Zielposition für die Heuristikberechnung.
     * @return Die geschätzten Kosten als double.
     */
    double berechne(GitterPosition start, GitterPosition ziel);
}