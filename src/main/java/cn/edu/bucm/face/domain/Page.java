package cn.edu.bucm.face.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 接收远程调用的数据
 *
 * @author js
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> content = new ArrayList<>();
    private boolean last;
    private int totalPages;
    private int totalElements;

    @Override
    public String toString() {
        return "Page{" +
                "content=" + content +
                ", last=" + last +
                ", totalPages=" + totalPages +
                ", totalElements=" + totalElements +
                ", numberOfElements=" + numberOfElements +
                ", size=" + size +
                ", number=" + number +
                '}';
    }

    private int numberOfElements;
    private int size;
    private int number;

    public Page() {
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}