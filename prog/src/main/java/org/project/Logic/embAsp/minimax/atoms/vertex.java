package org.project.Logic.embAsp.minimax.atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("vertex") //vertex(ID,Type,Value)
public class vertex {
    @Param(0)
    private int id;
    @Param(1)
    private String type;
    @Param(2)
    private int value;

    public vertex() {
    }

    public vertex(int id, String type, int value) {
        this.id = id;
        this.type = type;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        vertex vertex = (vertex) o;
        return id == vertex.id && value == vertex.value && Objects.equals(type, vertex.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, value);
    }

    @Override
    public String toString() {
        return " vertex(" +
                id +
                "," + type +
                "," + value +
                ") ";
    }
}
