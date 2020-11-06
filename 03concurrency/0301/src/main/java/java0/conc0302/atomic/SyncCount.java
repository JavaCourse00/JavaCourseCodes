
package java0.conc0302.atomic;

public class SyncCount {

    private int num = 0;

    public synchronized int add() {
        return num++;
    }

    public int getNum() {
        return num;
    }
}
