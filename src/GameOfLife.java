import java.util.Scanner;

public class GameOfLife {

    /**
     * Die main-Methode, die die Konsolenversion des Spiels startet.
     * @param args Kommandozeilenargumente (werden hier nicht verwendet).
     */
    public static void main(String[] args) 
    {
        // Erstellt ein Scanner-Objekt, um Benutzereingaben von der Konsole zu lesen
        Scanner scanner = new Scanner(System.in);

        // Fragt den Benutzer nach der gewünschten Breite des Spielfelds
        System.out.print("Wie breit soll das Spielfeld sein? ");
        int breite = scanner.nextInt();

        // Fragt den Benutzer nach der gewünschten Höhe des Spielfelds
        System.out.print("Wie hoch soll das Spielfeld sein? ");
        int hoehe = scanner.nextInt();

        System.out.println("Wähle ein Startmuster:");
        System.out.println("1 = Alle Zellen zufällig beleben");
        System.out.println("2 = Nur wenige zufällige Zellen (z. B. 10)");
        
        System.out.print("Deine Wahl: ");
        int wahl = scanner.nextInt();

        int anzahl = 10; // Standardwert für begrenzte zufällige Zellen

        if (wahl == 2) {
            System.out.print("Wie viele Zellen sollen zufällig belebt werden? ");
            anzahl = scanner.nextInt();
        }

        // Schließt den Scanner, da er nicht mehr benötigt wird
        scanner.close();

        // Ruft den Konstruktur der Spielfeld Klasse auf und erzeugt ein  neues Objekt namens spiel
        // das Objekt spiel ruft dann die Methode zufaelligBeleben der Klasse Spielfeld auf und erzeugt mit der Math.random Funktion 
        // ein zufälliges Muster mit lebenden Zellen
        // Zeigt dann das Spielfeld an

        Spielfeld spiel = new Spielfeld(breite, hoehe);  

        if (wahl == 1) {
            spiel.zufaelligBelebenAlle();
        } else if (wahl == 2) {
            spiel.zufaelligBelebenBegrenzt(anzahl);
        } 
        else {
            System.out.println("Ungültige Eingabe – Standard: Zufällig Alle");
            spiel.zufaelligBelebenAlle();
        }

        for (int runde = 0; runde < 50; runde++)
        {
            /* Konsole immer leeren, bevor das neue Spielfeld ausgegeben wird
             *    
             * Entweder 20 x println um es aus dem Bild zu schieben oder per ANSI Zeichen
             * for (int i = 0; i < 20; i++) {
             * System.out.println();
             * }
            */
            System.out.print("\033[H\033[2J");
            System.out.flush();

            
                
                spiel.anzeigen();


            // Verzögerung um 500 Milisekunden
            // Thread benötigt immer eine Fehlerbehandlung mit einem try catch Block, für den  Fall das Thread unterbrochen wird
            // Interrupted Exception ist eine Fehlerklasse 
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                // Falls Fehler wird er in der Konsole ausgegeben
                e.printStackTrace();
            }

            // Nächste Generation:
            spiel.berechneNaechsteGeneration();

        }
    }
}