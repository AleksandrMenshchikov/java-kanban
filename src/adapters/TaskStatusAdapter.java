package adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import constants.TaskStatus;

import java.io.IOException;

public final class TaskStatusAdapter extends TypeAdapter<TaskStatus> {
    @Override
    public void write(JsonWriter jsonWriter, TaskStatus taskStatus) throws IOException {
        jsonWriter.value(taskStatus.toString());
    }

    @Override
    public TaskStatus read(JsonReader jsonReader) throws IOException {
        return TaskStatus.valueOf(jsonReader.nextString());
    }
}
