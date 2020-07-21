package com.ywh.olrn.jvm.rtda;

import lombok.Getter;
import lombok.Setter;

import java.lang.ref.Reference;

/**
 * 槽
 *
 * @author ywh
 * @since 21/07/2020
 */
public class Slot {

    private String num;

    /**
     * >0 为true  <0 为 false
     */
    @Getter
    @Setter
    private boolean flag;

    @Getter
    @Setter
    private Reference ref;

    @Override
    public String toString() {
        return getNumInt() + "";
    }

    public int getNumInt() {
        return Integer.parseInt(num, 2);
    }

    public String getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = Integer.toBinaryString(num);
    }

    public void setNum(String num) {
        this.num = num;
    }

}
