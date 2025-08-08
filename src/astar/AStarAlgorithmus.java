package astar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet; // Neu für den TreeSet

/**
 * Implementiert den A*-Suchalgorithmus zur Wegfindung in einem Gitter.
 * Diese Klasse nutzt eine Heuristik, um den kürzesten Weg von einem Start- zu einem Zielpunkt zu finden.
 */
public class AStarAlgorithmus {
    private final GitterModell modell; // Das Gitter, in dem der Weg gefunden wird.
    private final Heuristik heuristik; // Die Heuristik zur Schätzung der Kosten bis zum Ziel.

    /**
     * Konstruktor für den A*-Algorithmus.
     * @param modell Das Gittermodell.
     * @param heuristik Die zu verwendende Heuristik.
     */
    public AStarAlgorithmus(GitterModell modell, Heuristik heuristik) {
        this.modell = modell;
        this.heuristik = heuristik;
    }

    /**
     * Findet den kürzesten Weg von einer Start- zu einer Zielposition.
     * Implementiert den klassischen A*-Suchablauf mit einer offenen Liste (TreeSet)
     * und einer geschlossenen Menge, unter Nutzung einer (zulässigen) Heuristik.
     *
     * Hinweise zur Datenstrukturwahl:
     * - Die offene Liste ist ein TreeSet, sortiert nach f = g + h.
     * - Bei gleichen f-Kosten erfolgt ein Tie-Break via Positions-Hash, damit keine Elemente "verschwinden".
     *
     * @param start Die Startposition.
     * @param ziel  Die Zielposition.
     * @return Eine Liste von GitterPositionen, die den Pfad repräsentieren,
     *         oder eine leere Liste, wenn kein Pfad gefunden wurde.
     */
    public List<GitterPosition> findeWeg(GitterPosition start, GitterPosition ziel) {
        // Die Menge der bereits besuchten und ausgewerteten Knoten.
        Set<GitterPosition> geschlosseneMenge = new HashSet<>();
        

        // Eine Prioritätswarteschlange für die zu besuchenden Knoten, sortiert nach den f-Kosten (g + h).
        /*  PriorityQueue<KnotenEintrag> offeneListe = new PriorityQueue<>(
            (e1, e2) -> Double.compare(e1.getGKosten() + e1.getHKosten(),
                                      e2.getGKosten() + e2.getHKosten())
        ); */

        /* Wechsel von PriorityQueue zu TreeSet nach lesen von 
        https://www.happycoders.eu/de/algorithmen/a-stern-algorithmus-java/ */
        
    // Ein TreeSet für die zu besuchenden Knoten, sortiert nach den f-Kosten (g + h).
        TreeSet<KnotenEintrag> offeneListe = new TreeSet<>((e1, e2) -> {
            // Vergleich der f-Kosten
            int vergleich = Double.compare(e1.getGKosten() + e1.getHKosten(),
                                          e2.getGKosten() + e2.getHKosten());
            
            // Bei gleichen f-Kosten: Unterscheide nach Position, damit keine Elemente verloren gehen
            if (vergleich == 0) {
                // Verwende hashCode als eindeutigen Identifier
                return Integer.compare(e1.getPosition().hashCode(), e2.getPosition().hashCode());
            }
            
            return vergleich;
        });


        // Eine Map, die jeder Position den besten bisher gefundenen KnotenEintrag zuordnet.
        Map<GitterPosition, KnotenEintrag> positionZuEintrag = new HashMap<>();

        // Initialisierung des Startknotens.
        double startHeuristik = heuristik.berechne(start, ziel);
        KnotenEintrag startEintrag = new KnotenEintrag(start, 0, startHeuristik, null);
        offeneListe.add(startEintrag);
        positionZuEintrag.put(start, startEintrag);

        // Hauptschleife des Algorithmus.
        while (!offeneListe.isEmpty()) {
            // Wähle den Knoten mit den niedrigsten f-Kosten.
            // TreeSet: kleinsten Eintrag (niedrigste f-Kosten) entnehmen
            KnotenEintrag aktuellerEintrag = offeneListe.pollFirst();

            GitterPosition aktuellePosition = aktuellerEintrag.getPosition();

            // Wenn das Ziel erreicht ist, den Pfad rekonstruieren und zurückgeben.
            if (aktuellePosition.equals(ziel)) {
                return bauePfad(aktuellerEintrag);
            }

            // Füge die aktuelle Position zur geschlossenen Menge hinzu.
            geschlosseneMenge.add(aktuellePosition);

            // Untersuche alle Nachbarn der aktuellen Position (4-Richtungen gemäß GitterModell).
            /*
             FÜR JEDEN nachbar In getNachbarn(aktuellePosition) TUE
             WENN nachbar IN geschlosseneMenge ODER getZustand(nachbar) = HINDERNIS 
             DANN SETZE FORT MIT NÄCHSTEM NACHBARN
             ENDE WENN
             */
            for (GitterPosition nachbar : modell.getNachbarn(aktuellePosition)) {
                // Überspringe Hindernisse und bereits besuchte Knoten.
                if (geschlosseneMenge.contains(nachbar)
                    || modell.getZustand(nachbar) == ZellenZustand.HINDERNIS) {
                    continue;
                }

                // Berechne die neuen g-Kosten für den Nachbarn.
                double gNeu = aktuellerEintrag.getGKosten() + 1; // Kantenkosten sind 1.
                KnotenEintrag vorhandenerEintrag = positionZuEintrag.get(nachbar); // Prüft ob Position schon besucht wurde, ob es einen Eintrag gibt und wie gut der bisher besuchte Weg war

                // Wenn der neue Weg zum Nachbarn kürzer ist oder der Nachbar noch nicht besucht wurde:
                if (vorhandenerEintrag == null || gNeu < vorhandenerEintrag.getGKosten()) {
                    double hNeu = heuristik.berechne(nachbar, ziel);
                    KnotenEintrag neuerEintrag = new KnotenEintrag(nachbar, gNeu, hNeu, aktuellerEintrag);

                    // Aktualisiere den Eintrag für den Nachbarn in der offenen Liste und der Map.
                    positionZuEintrag.put(nachbar, neuerEintrag);

                    // Falls es bereits einen Eintrag gab: aus TreeSet entfernen und mit besseren Werten neu einfügen
                    if (vorhandenerEintrag != null) {
                        offeneListe.remove(vorhandenerEintrag);
                    }

                    offeneListe.add(neuerEintrag);
                }
            }
        }

        // Wenn die offene Liste leer ist und das Ziel nicht erreicht wurde, gibt es keinen Pfad.
        return Collections.emptyList();
    }

    /**
     * Rekonstruiert den Pfad vom Zielknoten zurück zum Startknoten.
     * @param zielEintrag Der KnotenEintrag des Ziels.
     * @return Eine Liste von GitterPositionen, die den Pfad vom Start zum Ziel darstellt.
     */
    private List<GitterPosition> bauePfad(KnotenEintrag zielEintrag) {
        List<GitterPosition> pfad = new ArrayList<>();
        KnotenEintrag cursor = zielEintrag;
        while (cursor != null) {
            pfad.add(cursor.getPosition());
            cursor = cursor.getVorgaenger();
        }
        Collections.reverse(pfad); // Umkehren, um den Pfad vom Start zum Ziel zu erhalten.
        return pfad;
    }

    /**
     * Eine private Hilfsklasse, die einen Knoten im Suchgraphen darstellt.
     * Sie speichert die Position, die g- und h-Kosten sowie den Vorgängerknoten.
     * Bietet eine saubere objektorientierte Lösung an.
     * 1. bündelte zusammengehörige Daten
     * 2. macht den Code lesbarer
     * 3. die Algo-Logik wird klarer dargestellt
     * 4. die Pfadrückverfolgung wird elegant ermöglicht
     * Alternativen wären hier: separate Datenstrukturen, die erweiterte GitterPosition-Klasse
     * oder Mehrdimensionale Arrays/Maps
     */
    private static class KnotenEintrag {
        private final GitterPosition position; // Die Position des Knotens im Gitter.
        private final double gKosten; // Die Kosten vom Startknoten bis zu diesem Knoten.
        private final double hKosten; // Die geschätzten Kosten von diesem Knoten bis zum Ziel (Heuristik).
        private final KnotenEintrag vorgaenger; // Der Vorgängerknoten auf dem bisher besten Pfad.

        KnotenEintrag(GitterPosition position,
                     double gKosten,
                     double hKosten,
                     KnotenEintrag vorgaenger) {
            this.position = position;
            this.gKosten = gKosten;
            this.hKosten = hKosten;
            this.vorgaenger = vorgaenger;
        }

        public GitterPosition getPosition() {
            return position;
        }

        public double getGKosten() {
            return gKosten;
        }

        public double getHKosten() {
            return hKosten;
        }

        public KnotenEintrag getVorgaenger() {
            return vorgaenger;
        }
    }
}