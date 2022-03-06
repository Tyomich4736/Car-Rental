package by.nosevich.carrental.utils;

import lombok.Data;

@Data
public class Page {
    private final Integer size;
    private final Integer num;

    public Page(int size) {
        this.size = size;
        num = 1;
    }

    public Page(int size, int num) {
        this.size = size;
        this.num = num;
    }

    public Integer getOffset() {
        return size * (num - 1);
    }

    public Integer getSize() {
        return size;
    }
}
