package com.ctransaction.base.utils;

import org.springframework.util.CollectionUtils;

import java.util.Comparator;
import java.util.List;

/**
 * Created by chpy on 19/5/19.
 */
public class SortUtils {

    /**
     * 插入排序
     *
     * @param list
     * @param comparator
     * @param <T>
     * @return
     */
    public static <T> List<T> insertionSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() == 1)
            return list;
        for (int i = 1; i < list.size(); i++) {
            boolean flag = true;
            int index = i;
            while (index >= 1 && flag) {
                T preData = list.get(index - 1);
                //当前位置小于前一个节点的话 交换位置
                if (comparator.compare(list.get(index), preData) < 0) {
                    list.set(index - 1, list.get(index));
                    list.set(index, preData);
                } else if (index != i) {
                    flag = false;
                }
                index--;
            }
        }
        return list;
    }
}
