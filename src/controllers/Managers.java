package controllers;

import models.FileBackedTaskManager;
import models.InMemoryHistoryManager;
import models.InMemoryTaskManager;

import java.io.File;

public class Managers {

    public FileBackedTaskManager getDefaultFile(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
