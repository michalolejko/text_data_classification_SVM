package others;

public class MyStopwatch {
    long start;

    public MyStopwatch() {
        start();
    }

    public void start() {
        start = System.currentTimeMillis();
    }

    public void restart() {
        start();
    }

    @Override
    public String toString() {
        long m = System.currentTimeMillis() - start;
        return "Elapsed time [min:sec:milis] = " + String.format("%02d:%02d:%02d", m / 60_000, m / 1000, m / 100);
    }
}
