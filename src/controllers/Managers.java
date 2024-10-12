package controllers;

import exceptions.CrossTaskException;
import models.FileBackedTaskManager;
import models.InMemoryHistoryManager;
import models.InMemoryTaskManager;

import java.io.File;

public final class Managers {

    public FileBackedTaskManager getDefaultFile(File file) throws CrossTaskException {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
