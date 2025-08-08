public class Zelle 
{   
    // Eine Zelle im Spiel des Lebens
    // Sie kann lebendig oder tot sein
    // Sie kann Nachbarn haben, die ebenfalls Zellen sind
    // Sie stirbt wenn sie weniger als 2 oder mehr als 3 lebendige Nachbarn hat
    // Sie wird lebendig wenn sie genau 3 lebendige Nachbarn hat
    // Sie bleibt lebendig wenn sie 2 oder 3 lebendige Nachbarn hat
    
    // 1. Eigenschaft
    private boolean istLebendig = false; // Standardmäßig ist die Zelle tot
    
    // 2. Konstruktor
    /**
     * Konstruktor der Zelle, der den Anfangszustand (lebendig oder tot) setzt.
     * @param istLebendig true, wenn die Zelle lebendig sein soll, sonst false.
     */
    public Zelle (boolean istLebendig) 
    {
        this.istLebendig = istLebendig;
    }

    // 3. Methoden
    /**
     * Getter-Methode, um den Zustand der Zelle abzufragen.
     * @return true, wenn die Zelle lebendig ist, sonst false.
     */
    public boolean getIstLebendig()
    {
        return this.istLebendig;
    }

    /*
     * Setter-Methode, um den Zustand der Zelle zu setzen.
     * @param istLebendig Der neue Zustand der Zelle (true für lebendig, false für tot).
     */
    public void setIstLebendig(boolean istLebendig)
    {
        this.istLebendig = istLebendig;
    }
}