import java.io.Serializable;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 1:1复制ReentrantLock，为学习使用
 *
 * @author yan.pb
 * @date 2021/01/26
 */
public class MyReentrantLock implements Serializable {

    private final Sync sync;

    /**
     * 不传参默认构造为非公平实现
     */
    public MyReentrantLock() {
        sync = new NonfairSync();
    }

    /**
     * 传参时，true为公平实现，false为不公平实现
     */
    public MyReentrantLock(boolean fair) {
        sync = fair ? new FairSync() : new NonfairSync();
    }

    /**
     * ReentrantLock中的模板实现类，定义为abstract，分别定义了公平锁与非公平锁的内部实现类
     */
    abstract static class Sync extends AbstractQueuedSynchronizer {
        // 非公平锁实际获取，拿到锁返回true，否则false
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            // 如果没有锁，还是先尝试获取，此时是第二次尝试，第一次失败后，到这里会有可能别的线程已经释放锁了
            if (c == 0) {
                // 有可能并发，所以采用CAS竞争锁
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
                // 如果有锁，看看持有锁的线程是不是当前线程，此为"可重入"锁关键点。
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                // 此处肯定只能是当前线程进来，不存在并发，直接set
                setState(nextc);
                return true;
            }
            return false;
        }

        // 公平锁实际获取，拿到锁返回true，否则false
        final boolean fairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                // 如果没有锁，检查一下现在阻塞队列中有没有等待的线程，没有则直接获取锁，拿到之后将独占线程设置为当前线程
                // 有可能并发，所以采用CAS竞争锁
                if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) {
                    throw new Error("Maximum lock count exceeded");
                }
                setState(nextc);
                return true;
            }
            return false;
        }
    }

    /**
     * sync的非公平锁内部实现类
     */
    static final class NonfairSync extends Sync {
        // 非公平锁获取，第一次尝试获取锁，拿到之后将独占线程设置为当前线程
        final void lock() {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                // 未获取到，则与公平锁同一处理逻辑
                acquire(1);
            }
        }

        @Override
        protected boolean tryAcquire(int arg) {
            return nonfairTryAcquire(arg);
        }
    }

    /**
     * sync的公平锁内部实现类
     */
    static final class FairSync extends Sync {
        // 锁获取
        final void lock() {
            acquire(1);
        }

        @Override
        protected boolean tryAcquire(int arg) {
            return fairTryAcquire(arg);
        }
    }
}
