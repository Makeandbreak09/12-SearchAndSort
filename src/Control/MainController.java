package Control;

import Model.Ball;
import Model.List;
import View.DrawingPanel;

import java.util.Random;

/**
 * Idea by KNB,
 * Rework by AOS on 12.01.2017, 24.02.2018
 */
public class MainController {

    private Random rng;

    private int maxWidth;

    private long time;
    private long loops;
    private long switches;

    private Ball lastFound;
    private Ball[] originalArray;
    private Ball[] moddedArray;
    private List<Ball>[] hashArray; //Hier handelt es sich um ein Array, das Listen verwaltet.

    public MainController(){
        rng = new Random();
        rng.setSeed(System.currentTimeMillis());
    }

    /**
     * Erzeugt das Array originalArray aus zufällig gefüllten Kreisen mit sinnvollen Koordinaten. Dabei wird von jedem Ball-Objekt eine exakte Kopie erstellt und einem weiteren Array, dem moddedArray, hinzugefügt.
     * Die Methoden, die noch implementiert werden müssen, verändern das moddedArray, nicht das originalArray.
     * @param amount Anzahl der Bälle
     * @param originalPanel Panel zur Darstellung des Urpsrungsarrays.
     * @param moddedPanel Panel zur Darstellung des abgeändereten (sortierten?) Arrays.
     */
    public void generateArray(int amount, DrawingPanel originalPanel, DrawingPanel moddedPanel){
        originalArray = new Ball[amount];
        moddedArray = new Ball[amount];
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int x = 10;
        int y = 10;
        int maxRandom = Integer.max(100,amount);
        this.maxWidth = originalPanel.getWidth();
        for(int i = 0; i < amount; i++){
            Ball newBall = new Ball(x, y, rng.nextInt(maxRandom), alphabet.charAt(rng.nextInt(alphabet.length())));
            originalArray[i] = newBall;
            originalPanel.addObject(originalArray[i]);

            Ball copyBall = newBall.getCopy();
            moddedArray[i] = copyBall;
            moddedPanel.addObject(moddedArray[i]);

            x += 20;
            if ( x > maxWidth - 10 ){
                x = 10;
                y += 20;
            }
        }
    }

    /**
     * Erzeugt eine frische, unsortierte Kopie des Original-Arrays.
     */
    public void recopy(DrawingPanel moddedPanel){
        moddedPanel.removeAllObjects();
        for(int i = 0; i < originalArray.length; i++){
            Ball copyBall = originalArray[i].getCopy();
            moddedArray[i] = copyBall;
            moddedPanel.addObject(moddedArray[i]);
        }
    }

    /**
     * Setzt für alle Kugeln im sortierten Array neue Koordinaten gemäß der Reihenfolge im Array.
     * Muss nach dem Sortieren aufgerufen werden, damit die Sortierung sichtbar wird.
     */
    private void updateCoordinates(){
        int x = 10;
        int y = 10;
        for(int i = 0; i < moddedArray.length; i++){
            moddedArray[i].setX(x);
            moddedArray[i].setY(y);

            x += 20;
            if ( x > maxWidth - 10 ){
                x = 10;
                y += 20;
            }
        }
    }

    /**
     * Führt eine lineare Suche auf dem modded-Array durch.
     * @param key Die gesuchte Zahl.
     */
    public void linSearchArray(int key) {
        if(lastFound != null){
            lastFound.setMarked(false);
        }
        time = System.nanoTime();
        loops = 0;

        // Lineare Suche Start
        lastFound = null;
        int i = 0;
        while ( lastFound == null && i < moddedArray.length){
            loops++;
            if (moddedArray[i].getNumber() == key){
                lastFound = moddedArray[i];
            }
            i++;
        }
        // Lineare Suche Ende
        time = (System.nanoTime() - time)/1000;
        if(lastFound != null){
            lastFound.setMarked(true);
        }
    }

    /**
     * Führt eine binäre Suche auf dem modded-Array durch.
     * @param key Die gesuchte Zahl.
     */
    public void binSearchArray(int key) {
        if(lastFound != null){
            lastFound.setMarked(false);
        }
        time = System.nanoTime();
        loops = 0;
        // Binäre Suche Start
        /*
        int max = moddedArray.length-1;
        int min = 0;
        lastFound = null;

        while(lastFound == null && max != min){
            loops++;
            if(moddedArray[(max+min)/2].getNumber() < key){
                min = (max+min)/2+1;
            }else if(moddedArray[(max+min)/2].getNumber() > key) {
                max = (max + min)/2;
            }
            if(moddedArray[(max+min)/2].getNumber() == key){
                lastFound = moddedArray[(max+min)/2];
            }
        }
         */
        int[] a = new int[moddedArray.length];
        for(int i = 0; i<moddedArray.length; i++){
            a[i] = moddedArray[i].getNumber();
        }
        int found = whatAmIDoing(a, 0, moddedArray.length, key);
        if(found>=0){
            lastFound = moddedArray[whatAmIDoing(a, 0, moddedArray.length, key)];
        }else{
            lastFound = null;
        }
            //TODO 01: Orientiere dich für die Messung der Schleifendurchgänge an der Linearen Suche und implementiere die Binäre Suche iterativ.

        // Binäre Suche Ende
        time = (System.nanoTime() - time)/1000;
        if(lastFound != null){
            lastFound.setMarked(true);
        }
    }

    public int whatAmIDoing(int[] a, int left, int right, int key){
        loops++;
        if(left>right){
            return -1;
        }
        int middle=(left+right)/2;
        if(a[middle] == key){
            return middle;
        }else if(key < a[middle]){
            return whatAmIDoing(a, left, middle-1, key);
        }else {
            return whatAmIDoing(a, middle+1, right, key);
        }

    }

    /**
     * Sortiert das modded-Array gemäß dem Bubble-Sort-Algorithmus.
     */
    public void bubbleSortArray() {
        time = System.nanoTime();
        loops = 0;
        switches = 0;
        // Bubblesort Start
        for(int i = 0; i < moddedArray.length; i++){
            loops++;
            int a = 0;
            for (int j = 0; j < moddedArray.length-1-i; j++){
                loops++;
                if(moddedArray[j].getNumber() > moddedArray[j+1].getNumber()){
                    switchBalls(j, j+1);
                    a++;
                }
            }
            if(a==0) break;
        }
        // Bubble Sort Ende
        time = (System.nanoTime() - time)/1000;
        updateCoordinates();
    }

    /**
     * Sortiert das modded-Array gemäß dem Selection-Sort-Algorithmus.
     */
    public void selectionSortArray() {
        time = System.nanoTime();
        loops = 0;
        switches = 0;
        // Selectionsort Start
        for(int i = 0; i < moddedArray.length; i++){
            loops++;
            int min = i;
            for (int j = i; j < moddedArray.length; j++){
                loops++;
                if(moddedArray[min].getNumber() > moddedArray[j].getNumber()){
                    min = j;
                }
            }
            for(int j = min; j>i; j--){
                switchBalls(j, j-1);
            }
        }
            //TODO 02: Orientiere dich für die Messung der Schleifendurchgänge und der tatsächlichen Vertauschungen an Bubblesort und implementiere Selectionsort inplace.

        // Selection Sort Ende
        time = (System.nanoTime() - time)/1000;
        updateCoordinates();
    }

    /**
     * Sortiert das modded-Array gemäß dem Insertion-Sort-Algorithmus.
     */
    public void insertionSortArray() {
        time = System.nanoTime();
        loops = 0;
        switches = 0;
        // Insertionsort Start
        for(int i = 0; i < moddedArray.length; i++){
            loops++;
            for (int j = i; j > 0 && moddedArray[j].getNumber() < moddedArray[j-1].getNumber(); j--){
                loops++;
                switchBalls(j, j-1);
            }
        }

            //TODO 03: Orientiere dich für die Messung der Schleifendurchgänge und der tatsächlichen Vertauschungen an Bubblesort und implementiere Insertionssort inplace.

        // Insertion Sort Ende
        time = (System.nanoTime() - time)/1000;
        updateCoordinates();
    }

    /**
     * Sortiert das modded-Array gemäß dem Quick-Sort-Algorithmus.
     */
    public void quickSortArray() {
        time = System.nanoTime();
        loops = 0;
        switches = 0;
        // Quick Sort Start
        quicksortRecursive(0,moddedArray.length-1);
        // Quick Sort Ende
        time = (System.nanoTime() - time)/1000;
        updateCoordinates();
    }

    /**
     * Die eigentliche rekursive Quicksort-Methode.
     */
    private void quicksortRecursive(int start, int end){
        loops++;
        int i = start;
        int j = end;
        int middle =  (i + j) / 2;
        int pivot = moddedArray[middle].getNumber();

        //Beginn des Zaubers
            //TODO 05: Programmiere den rekursiven Quicksortalgorithmus. Halte dich an den hier vorgegeben Rahmen.
        //Ende des Zaubers
    }

    /**
     * Die Bälle werden gemäß der Hashfunktion in der Hashtabelle gepspeichert.
     * Dazu werden alle Bälle zunächst kopiert und dann in die passenden Listen von hashArray übertragen.
     * Anschließend müsst ihr noch für die zeichnerische Darstellung der Bälle die jeweilige x- und y-Koordinate aktualisieren.
     * @param hashPanel
     */
    public void hashIt(DrawingPanel hashPanel){
        hashPanel.removeAllObjects();
        hashArray = new List[1]; //Die Länge des Arrays wird durch die Anzahl prinzipiell möglicher Funktionswerte der Hash-Funktion festgelegt.
            //TODO 04b: Nach de Implementierung der Hashfunktion müssen die Ball-Objekte gemäß der Funktion ins hashArray übertragen werden. Beachte hierbei, dass du mit Ballkopien arbeiten musst, nicht mit den Originalen.

        int x = 10; //Start-Koordinate des ersten anzuzeigenen Balls
        int y = 10; //Start-Koordinate des ersten anzuzeigenen Balls
            //TODO 04c: Überarbeite die Koordinaten der Ball-Objekte im hashArray für die Darstellung in der View.
    }

    /**
     * Die Hashfunktion für die Methode hashIt(...)
     * @param argument Das übergebene Funktionsargument
     * @return Funktionswert
     */
    private int hashFunction(int argument){
            //TODO 4a: Implementiere eine vernünftige Hashfunktion.
        return 0;
    }

    /**
     * Führt eine Hash-Suche auf dem Hash-Arrays durch.
     * @param key
     */
    public void hashSearch(int key){
            //TODO 4d: Implementiere die Suche auf der Hashtabelle.
    }

    /**
     * Vertausch zwei Bälle innerhalb des Arrays, das verändert wird.
     * Bei jedem Aufruf dieser Methode wird das Attribut switches hochgezählt.
     * @param a Indexposition des einen Balls
     * @param b Indexposition des anderen Balls
     */
    private void switchBalls(int a, int b){
        Ball temp = moddedArray[a];
        moddedArray[a] = moddedArray[b];
        moddedArray[b] = temp;

        switches++;
    }

    public long getTime() {
        return time;
    }

    public long getLoops() {
        return loops;
    }

    public long getSwitches() {
        return switches;
    }
}
