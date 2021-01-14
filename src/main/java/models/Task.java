package models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Task {
    @Getter@Setter
    private long id;
    @Getter
    private boolean isCompleted = false; // по дефолту задача невыполнена

    public Task(long id, boolean isCompleted) {
        this.id = id;
        this.isCompleted = isCompleted;
    }

    public Task() {

    }

    public void complete(boolean completed) {
        isCompleted = completed;
    }
}
