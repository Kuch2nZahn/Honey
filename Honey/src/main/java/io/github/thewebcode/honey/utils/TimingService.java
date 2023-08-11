package io.github.thewebcode.honey.utils;

import io.github.thewebcode.honey.Honey;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

public class TimingService {
    private final ArrayList<BukkitTask> tasks;

    public TimingService() {
        this.tasks = new ArrayList<>();

        BukkitTask threeSecondsRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                //TODO: Add Event
                Honey.getInstance().getMessagingService().tick();
            }
        }.runTaskTimer(Honey.getInstance(), 0, 20 * 3);

        tasks.add(threeSecondsRunnable);
    }

    public void cancelAllTasks() {
        tasks.forEach(BukkitTask::cancel);
    }
}
