package main.java.com.hit.dm;

import java.util.Objects;

/**
 *
 * @param <T> type of content
 */
public class DataModel<T> implements java.io.Serializable{
   private Long id;
    private T content;

    public DataModel(Long id, T content) {
        this.id = id;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return id.equals(dataModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id: " + id +
                ", content: " + content +
                '}';
    }
}