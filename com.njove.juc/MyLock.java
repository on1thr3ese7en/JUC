/**
 * @author yan.pb
 * @date 2021/01/30
 */
public interface MyLock {

    void lock();

    boolean tryLock();

    void unlock();

}
