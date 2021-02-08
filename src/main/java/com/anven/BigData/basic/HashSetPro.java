package com.anven.BigData.basic;

import java.util.HashSet;

public class HashSetPro {
    public static void main(String[] args) {
        /**
         * 1. 存放不重复元素的集合，允许插入Null值
         * 2. 实现方法依赖HashMap，所存放的元素为HashMap的key，value则都是同一个对象PRESENT
         */
        HashSet hashSet = new HashSet();
        hashSet.add(null);
    }
}
