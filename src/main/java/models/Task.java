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

    public void complete(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
