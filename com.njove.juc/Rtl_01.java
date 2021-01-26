import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yan.pb
 * @date 2021/01/25
 */
public class Rtl_01 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition conditionA = lock.newCondition();
        Condition conditionB = lock.newCondition();
        Condition conditionC = lock.newCondition();
        new Thread(() -> {
//            for (int i = 0; i < 10; i++) {
//                System.out.println("A");
//            }
            lock.lock();
            for (int i = 0; i < 10; i++) {

                try {
                    System.out.println("A");
                    conditionB.signal();
                    conditionA.await();
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
                    conditionC.signal();
                    conditionB.await();
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
                    conditionA.signal();
                    conditionC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }).start();
    }
}
