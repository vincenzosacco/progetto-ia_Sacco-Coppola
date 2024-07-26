package org.project.Logic.embAsp.graph.atoms;

import it.unical.mat.embasp.languages.Id;
import it.unical.mat.embasp.languages.Param;

import java.util.Objects;

@Id("value") // value(N)
public class value {
    @Param(0)
    private int N;

    public value() {
    }

    public value(int n) {
        N = n;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        value value = (value) o;
        return N == value.N;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(N);
    }

    @Override
    public String toString() {
        return "value(" +
                N +
                ")";
    }
}
