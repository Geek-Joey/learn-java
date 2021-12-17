package com.xc.learnjava;

import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 *
 */
public class LambdaTest {

    @Test
    /**
     * 使用Lambda表达式实现忽略大小写排序
     */
    public void test_01(){
        String[] array = {"apple","Orange","banana","Lemon"};
        //1.通过实例化接口的传统实现
//        Arrays.sort(array, new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                //o1.compareTo(o2); //Lemon,Orange,apple,banana
//                return o1.compareToIgnoreCase(o2); //apple,banana,Lemon,Orange
//
//            }
//        });

        //2. Lambda实现
//        Arrays.sort(array,(s1,s2) -> s1.compareToIgnoreCase(s2));
        //方法引用
        Arrays.sort(array,String::compareToIgnoreCase);
        System.out.println(String.join(",",array));
    }
}
