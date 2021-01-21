import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yan.pb
 * @date 2021/01/22
 */
public class Cdl_01 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        CountDownLatch cdl = new CountDownLatch(3);
        cdl.countDown();
        executorService.execute(() -> {
            System.out.println(1);
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cdl.countDown();
        });
        executorService.execute(() -> {
            System.out.println(2);
            try {
                Thread.sleep(10000000000000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cdl.countDown();
        });
        try {
            cdl.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("======================");
        executorService.shutdown();
    }
}
