package astar;
/**
 * Definiert die möglichen Zustände einer Zelle im Gitter für den A*-Algorithmus.
 */
public enum ZellenZustand {
    /**
     * Die Zelle ist offen und kann betreten werden. Sie wurde noch nicht besucht oder ist Teil der offenen Liste.
     */
    OFFEN,
    /**
     * Die Zelle wurde bereits besucht und ausgewertet. Sie ist Teil der geschlossenen Liste.
     */
    GESCHLOSSEN,
    /**
     * Die Zelle ist ein Hindernis und kann nicht betreten werden.
     */
    HINDERNIS,
    /**
     * Die Zelle repräsentiert den Startpunkt des Pfades.
     */
    START,
    /**
     * Die Zelle repräsentiert den Zielpunkt des Pfades.
     */
    ZIEL,
    /**
     * Die Zelle ist Teil des gefundenen kürzesten Pfades.
     */
    WEG;
}