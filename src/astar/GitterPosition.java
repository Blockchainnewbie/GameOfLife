package astar;

/**
 * Repräsentiert eine unveränderliche Position im Gitter mit Zeilen- und Spaltenindex.
 * Dient als Schlüssel in Maps/Sets und ist daher mit equals/hashCode implementiert.
 */
public class GitterPosition {
    private final int zeile;
    private final int spalte;

    public GitterPosition(int zeile, int spalte) {
        this.zeile = zeile;
        this.spalte = spalte;
    }

    public int getZeile() {
        return zeile;
    }

        public int getSpalte() {
        return spalte;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GitterPosition that = (GitterPosition) o;
        return zeile == that.zeile && spalte == that.spalte;
    }

    @Override
    public int hashCode() {
        return 31 * zeile + spalte;
    }
}