import statistical.DataManager;
import statistical.MainManagement;

public class Main {
    static final int  maxLinesToBeProcessed = 10_000;

    public static void main(String[] args) {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        //dla algorytmu przetwarzania zbioru AMAZONA przy wiekszych ilosciach drastycznie spada wydajnosc
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        DataManager[] dataManagerAcllmbds = MainManagement.calculateStatisticalDataAcllmbd(maxLinesToBeProcessed, false, true);
        DataManager[] dataManagerAmazon = MainManagement.calculateStatisticalDataAmazon(maxLinesToBeProcessed,false, true);
    }
}
