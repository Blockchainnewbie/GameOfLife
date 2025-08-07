package astar;

import java.util.ArrayList;
import java.util.List;

/**
 * Repräsentiert das Gittermodell für den A*-Algorithmus.
 * Diese Klasse verwaltet das Gitter, seine Dimensionen und den Zustand jeder Zelle.
 */
public class GitterModell {

    // Variablen
    private int spalten; // Anzahl der Spalten im Gitter
    private int zeilen; // Anzahl der Zeilen im Gitter
    private ZellenZustand[][] gitter; // 2D-Array, das das Gitter darstellt


    /**
     * Konstruktor für die GitterModell-Klasse.
     * Initialisiert das Gitter mit der angegebenen Anzahl von Zeilen und Spalten.
     * Alle Zellen werden zunächst auf den Zustand OFFEN gesetzt.
     *
     * @param zeilen  Die Anzahl der Zeilen.
     * @param spalten Die Anzahl der Spalten.
     */
    public GitterModell(int zeilen, int spalten) {
        this.spalten = spalten;
        this.zeilen = zeilen;

        this.gitter = new ZellenZustand[zeilen][spalten];
        // Initialisiert alle Zellen als OFFEN
        for (int i = 0; i < zeilen; i++) {
            for (int j = 0; j < spalten; j++) {
                gitter[i][j] = ZellenZustand.OFFEN;
            }
        }
    }

    /**
     * Ruft die vier direkten Nachbarn (oben, unten, links, rechts) einer bestimmten Position ab.
     *
     * @param pos Die Position, für die Nachbarn gefunden werden sollen.
     * @return Eine Liste benachbarter Gitterpositionen.
     */
    public List<GitterPosition> getNachbarn(GitterPosition pos) {
        List<GitterPosition> nachbarn = new ArrayList<>();

        int i = pos.getZeile();
        int j = pos.getSpalte();

        // Oberer Nachbar
        if (i > 0) {
            nachbarn.add(new GitterPosition(i - 1, j));
        }

        // Unterer Nachbar
        if (i + 1 < getZeilen()) {
            nachbarn.add(new GitterPosition(i + 1, j));
        }

        // Linker Nachbar
        if (j > 0) {
            nachbarn.add(new GitterPosition(i, j - 1));
        }

        // Rechter Nachbar
        if (j + 1 < getSpalten()) {
            nachbarn.add(new GitterPosition(i, j + 1));
        }

        return nachbarn;
    }

    /**
     * Ruft den Zustand einer Zelle an einer bestimmten Position ab.
     *
     * @param pos Die Position der Zelle.
     * @return Der Zustand der Zelle.
     */
    public ZellenZustand getZustand(GitterPosition pos) {
        int zeile = pos.getZeile();
        int spalte = pos.getSpalte();
        return gitter[zeile][spalte];
    }

    /**
     * Setzt den Zustand einer Zelle an einer bestimmten Position.
     *
     * @param pos     Die Position der Zelle.
     * @param zustand Der neue Zustand, der gesetzt werden soll.
     */
    public void setZustand(GitterPosition pos, ZellenZustand zustand) {
        int zeile = pos.getZeile();
        int spalte = pos.getSpalte();
        gitter[zeile][spalte] = zustand;
    }


    // Getter und Setter

    /**
     * Gibt die Anzahl der Zeilen im Gitter zurück.
     *
     * @return Die Anzahl der Zeilen.
     */
    public int getZeilen() {
        return zeilen;
    }

    /**
     * Gibt die Anzahl der Spalten im Gitter zurück.
     *
     * @return Die Anzahl der Spalten.
     */
    public int getSpalten() {
        return spalten;
    }

}