package thread;

import java.util.List;

/**
 * Created by thales on 08/04/17.
 */
public class ThreadMonitor {
    public static void waitAllThreads(List<Thread> threads) {

        boolean threadsExecuting = true;
        while(threadsExecuting) {
            try {
                threadsExecuting = false;
                for (int thread = 0; thread < threads.size(); thread++) {
                    if(threads.get(thread).isAlive()) {
                        threadsExecuting = true;
                        break;
                    }
                }
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
