import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yan.pb
 * @date 2021/01/25
 */
public class Rtl_02 {
    public static void main(String[] args) {
        MyLock lock = new MyReentrantLock();
        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("A");
//            }
            lock.lock();
            for (int i = 0; i < 10; i++) {

                try {
                    System.out.println("A");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            lock.unlock();
        }).start();

        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("B");
//            }
            lock.lock();
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("B");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            lock.unlock();
        }).start();
        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("C");
//            }
            lock.lock();
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("C");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }).start();
    }
}
