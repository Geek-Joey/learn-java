package com.xc.learnjava;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Stream API
 * 1.流式API,使用java.util.stream 包
 * 2.定义：顺序输出的任意Java对象实例，用作内存计算／业务逻辑
 * 3.特点：提供了一套新的流式处理的抽象序列；支持函数式编程和链式操作；惰性计算
 * 4.基本用法：创建一个Stream，然后做若干次转换，最后调用一个求值方法获取真正计算的结果
 *
 * @author joey
 */
public class StreamTest {

    @Test
    /**
     * 一、创建 Stream
     * 1.通过指定元素、数组、Collection来创建；
     * 2.通过 Sipplier来创建 Stream,可以是无限序列；
     * 3.通过其它类的相关方法来创建
     * 4.基本类型如何处理？：IntStream、LongStream和DoubleStream
     *
     */
    public void createStream() throws Exception{
        //1.Stream.of
        Stream<String> stream = Stream.of("A", "B", "C", "D");
        //void forEach(Consumer<? super T> action); 可以传入符合Consumer接口的方法引用
        stream.forEach(System.out::println);
        System.out.println("***********************");

        //2.数组
        Stream<String> stream1 = Arrays.stream(new String[]{"A", "B", "C"});
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);list.add(2);list.add(3);
        Stream<Integer> stream2 = list.stream();
        stream1.forEach(System.out::println);
        stream2.forEach(System.out::println);
        System.out.println("***********************");

        //3.Supplier
        /**
         * 通过Stream.generate()方法，需要传入一个Supplier对象
         * 基于Supplier创建的Stream会不断调用Supplier.get()方法来不断产生下一个元素，这种Stream保存的不是元素，而是算法，它可以用来表示无限序列
         * example: 编写一个能不断生成自然数的Supplier
         */
        Stream<Integer> natual = Stream.generate(new NatualSupplier());
        //Tips:无限序列必须先变成有限序列，才能打印
        natual.limit(20).forEach(System.out::println);
        System.out.println("***********************");

        //4.其它方法：通过一些API提供的接口，可以获得stream
        //example: 正则表达式的Pattern对象有一个splitAsStream()方法，可以直接把一个长字符串分割成Stream序列而不是数组
        Pattern pattern = Pattern.compile("\\s+");
        Stream<String> stringStream = pattern.splitAsStream("The quick brown fox jumps over the lazy dog");
        stringStream.forEach(System.out::println);
        System.out.println("***********************");

        //5.基本类型在Stream中的处理
        /**
         * Java的泛型不支持基本类型，所以无法使用Stream<int>，会发生编译错误；
         * 为了保存int，只能使用Stream<Integer>，但是会产生频繁的装箱、拆箱操作；
         * 为了提高效率，Java标准库提供了IntStram、LongStream、DoubleStream这三种基本类型的Stream
         */
        IntStream is = Arrays.stream(new int[]{1, 3, 5});
        is.forEach(System.out::println);
        List<String> list1 = new ArrayList<>();
        list1.add("1");list1.add("2");list1.add("3");
        LongStream ls = list1.stream().mapToLong(Long::parseLong);
        ls.forEach(System.out::println);
        System.out.println("***********************");

        //LongStream：打印斐波拉契数列（Fibonacci）
        LongStream fib = LongStream.generate(new FibSupplier());
        fib.limit(10).forEach(System.out::println);

    }

    /**
     * 一个能不断生成自然数的Supplier
     */
    class NatualSupplier implements Supplier<Integer> {
        int n = 0;
        @Override
        public Integer get() {
            n++;
            return n;
        }
    }

    /**
     * 编写一个能输出斐波拉契数列（Fibonacci）的LongStream：
     * 1, 1, 2, 3, 5, 8, 13, 21, 34, ...
     */
    class FibSupplier implements LongSupplier {
        long first = 0;
        long second = 1;

        @Override
        public long getAsLong() {
            long tmp = second;
            second += first;
            first = tmp;
            return first;
        }
    }


    @Test
    /**
     * 二、map(转换) Stream
     * Stream.map()是Stream最常用的一个转换方法，它把一个Stream转换为另一个Stream。
     * 查看源码可以发现，map()方法接收的对象是Function接口对象，它定义了一个apply()方法，负责把一个T类型转换成R类型：
     */
    public void mapStream() {

        //对x计算它的平方
        Stream<Integer> is = Stream.of(1, 2, 3, 4, 5);
        Stream<Integer> mapStream = is.map(x -> x * x);
        mapStream.forEach(System.out::println);
        System.out.println("***********************");

        //操作字符串
        List<String> list = Arrays.asList("Apple", "pear ", "ORANGE", "BaNaNa ");
        list.stream()
                 //去除空格
                .map(String::trim)
                //转换为小写
                .map(String::toLowerCase)
                //排序
                .sorted()
                //打印
                .forEach(System.out::println);
        System.out.println("***********************");

        //练习：使用map()把一组String转换为LocalDate并打印
        String[] array = new String[] { " 2019-12-31 ", "2020 - 01-09 ", "2020- 05 - 01 ", "2022 - 02 - 01", " 2025-01 -01" };
        // 使用map把String[]转换为LocalDate并打印:
        Stream<LocalDate> localDateStream = Arrays.stream(array)
                //去除所有空格
                .map(x -> x.replace(" ",""))
                .map(x -> LocalDate.parse(x, DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        localDateStream.forEach(System.out::println);

    }


    @Test
    /**
     * 三、Filter(过滤) Stream
     * filter()操作，就是对一个Stream的所有元素一一进行测试，不满足条件的就被“滤掉”了，剩下的满足条件的元素就构成了一个新的Stream。
     */
    public void filterStream() {
    }


}
