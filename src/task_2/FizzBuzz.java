package task_2;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FizzBuzz {
    private final Object pauseLock = new Object();

    private int number;

    private int currentNumber = 1;

    private final Queue<String> resultQueue = new ArrayDeque<>();
    private String tempLastResult;

    private boolean running = true;

    private AtomicInteger processedCount = new AtomicInteger(0);

    public FizzBuzz(int number) {
        this.number = number;
    }

    private void fizz() {
        while (running) {
            if (currentNumber % 3 == 0 && currentNumber % 5 != 0) {
                writeString("fizz");
            }
            afterThreadWork();
        }
    }

    private void buzz() {
        while (running) {
            if (currentNumber % 5 == 0 && currentNumber % 3 != 0) {
                writeString("buzz");
            }
            afterThreadWork();
        }
    }

    private void fizzbuzz() {
        while (running) {
            if (currentNumber % 3 == 0 && currentNumber % 5 == 0) {
                writeString("fizzbuzz");
            }

            afterThreadWork();
        }
    }

    private void writeString(String str) {
        synchronized (resultQueue) {
            resultQueue.add(str);
            tempLastResult = str;
        }
    }

    private void writeNumberIfNeeded() {
        if (tempLastResult == null) {
            synchronized (resultQueue) {
                resultQueue.add(String.valueOf(currentNumber));
            }
        } else {
            tempLastResult = null;
        }
    }

    private void afterThreadWork() {
        if (processedCount.addAndGet(1) == 3) {
            synchronized (this) {
                processedCount.set(0);

                writeNumberIfNeeded();

                if (++currentNumber > number) {
                    running = false;
                }

                synchronized (pauseLock) {
                    pauseLock.notifyAll();
                }
            }
        } else {
            synchronized (pauseLock) {
                try {
                    pauseLock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void number() {
        int nextWriteIndex = 1;
        while (running) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (!resultQueue.isEmpty()) {
                synchronized (resultQueue) {
                    for (String str : resultQueue) {
                        System.out.print(str);
                        System.out.print(nextWriteIndex != number ? ", " : "\n");
                        nextWriteIndex++;
                    }
                }
            }
        }
    }

    public void run() {
        Thread a = new Thread(this::fizz, "A");
        Thread b = new Thread(this::buzz, "B");
        Thread c = new Thread(this::fizzbuzz, "C");
        Thread d = new Thread(this::number, "D");

        a.start();
        b.start();
        c.start();
        d.start();

        try {
            a.join();
            b.join();
            c.join();
            d.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FizzBuzz fizzBuzz = new FizzBuzz(15);

        fizzBuzz.run();

        FizzBuzz fizzBuzz2 = new FizzBuzz(30);

        fizzBuzz2.run();
    }
}
