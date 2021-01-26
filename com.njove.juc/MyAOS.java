import java.io.Serializable;

/**
 * @author yan.pb
 * @date 2021/01/26
 */
public class MyAOS implements Serializable {
    protected MyAOS() { }

    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread thread) {
        exclusiveOwnerThread = thread;
    }

    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }

}
