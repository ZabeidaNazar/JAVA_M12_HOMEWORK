package task_1;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Time {
    public static void printTime() {
        long startTime = System.currentTimeMillis();


        ScheduledExecutorService scheduler1 = new ScheduledThreadPoolExecutor(1);

        scheduler1.scheduleAtFixedRate(() -> {
            long ownTime = (System.currentTimeMillis() - startTime);
            System.out.printf("Від моменту запуску програми минуло: %02d:%02d:%02d\n",
                    TimeUnit.MILLISECONDS.toHours(ownTime),
                    TimeUnit.MILLISECONDS.toMinutes(ownTime) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(ownTime) % TimeUnit.MINUTES.toSeconds(1));
        }, 0, 1, TimeUnit.SECONDS);

        ScheduledExecutorService scheduler2 = new ScheduledThreadPoolExecutor(1);
        scheduler2.scheduleAtFixedRate(() -> {
            System.out.println("Минуло 5 секунд");
        }, 5, 5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        printTime();
    }
}
