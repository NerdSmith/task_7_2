package ru.vsu.cs;

public class Pair<K,V> {
    private K v1;

    public K getV1() {
        return v1;
    }

    private V v2;


    public V getV2() {
        return v2;
    }

    public Pair(K v1, V v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }
}
