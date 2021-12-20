package com.xc.learnjava;

import org.junit.Test;

import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.*;

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
     * 2.通过 Supplier 来创建 Stream,可以是无限序列；
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
        final Pattern pattern = Pattern.compile("\\s+");
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
     * Stream.map()是 Stream 最常用的一个转换方法，它把一个Stream转换为另一个Stream。
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
     * filter 除了应用于数值外，可以应用于任何Java对象
     */
    public void filterStream() {
        //只保留奇数
        IntStream.of(1,2,3,4,5,6,7)
                .filter(x -> x%2 != 0)
                .forEachOrdered(System.out::println);
        System.out.println("***********************");

        //从一组LocalDate中，过滤掉工作日，保留休息日
        Stream.generate(new LocalDateSupplier())
                .limit(31)
                .filter(ldt -> ldt.getDayOfWeek() == DayOfWeek.SATURDAY || ldt.getDayOfWeek() ==DayOfWeek.SUNDAY)
                .forEach(System.out::println);

    }

    /**
     * 不断生成日期的流
     */
    class LocalDateSupplier implements Supplier<LocalDate> {
        LocalDate start = LocalDate.of(2021, 1, 1);
        int n = -1;
        @Override
        public LocalDate get() {
            n++;
            return start.plusDays(n);
        }
    }



    @Test
    /**
     * 四、聚合Stream
     * Stream.reduce()是Stream的一个聚合方法，可以把一个Stream的所有元素按照聚合函数聚合成一个结果
     * reduce()方法传入的对象是BinaryOperator接口，它定义了一个apply()方法，负责把上次累加的结果和本次的元素 进行运算，并返回累加的结果
     *
     * T reduce(T identity, BinaryOperator<T> accumulator);
     *等价于：
     * T result = identity;
     * for (T element : this stream)
     * result = accumulator.apply(result, element)
     * return result;
     *
     * reduce()方法将一个Stream的每个元素依次作用于BinaryOperator，并将结果合并
     */
    public void reduceStream() {

        //求和
        Integer sum = Stream.of(1, 2, 3, 4, 5)
                .reduce(0, (acc, n) -> acc + n);
        System.out.println("sum=" + sum);
        System.out.println("***********************");

        //求积
        Integer quadrature = Stream.of(1, 2, 3, 4, 5)
                .reduce(1, (acc, n) -> acc * n);
        System.out.println("quadrature=" + quadrature);
        System.out.println("***********************");

        //除了可以对数值进行累积计算外，灵活运用reduce()也可以对Java对象进行操作。
        // 下面的代码演示了如何将配置文件的每一行配置通过map()和reduce()操作聚合成一个Map<String, String>
        Stream<String>  stream = Stream.of("profile=native", "debug=true", "logging=warn", "interval=500");

        //将K=V 转换为 Map[K]=V
        Stream<Map<String, String>> mapStream = stream.map(
                kv -> {
                    String[] split = kv.split("\\=", 2);
                    return Collections.singletonMap(split[0], split[1]);
                });

        //将所有的Map聚合到一个Map中
        Map<String, String> reduceMap = mapStream.reduce(new HashMap<String, String>(), (m, kv) -> {
            m.putAll(kv);
            return m;
        });

        //打印结果
        reduceMap.forEach((k,v) -> {
            System.out.println(k + " = " + v);
        });

    }

    @Test
    /**
     * 输出集合
     * 1.输出为List: 把Stream变为List不是一个转换操作，而是一个聚合操作，它会强制Stream输出每个元素
     * 2.输出为数组
     * 3.输出为Map
     * 4.分组输出
     *
     */
    public void outputStream() {
        //1.输出List
        Stream<String> stream = Stream.of("Apple", "", null, "Pear", "  ", "Orange");
        //Java11 引入 s.isBlank，来判断字符串是否为空格
        List<String> list = stream.filter(s -> s != null && !s.trim().isEmpty())
                .collect(Collectors.toList());
        System.out.println(list);
        System.out.println("***********************");

        //2.输出为Map
        String[] array = Stream.of("Apple", "Banana", "Orange").toArray(String[]::new);
        for (String s : array) {
            System.out.print(s + " ");
        }
        System.out.println("\n***********************");

        //3.输出为Map
        Stream<String> stream3 = Stream.of("APPL:Apple", "MSFT:Microsoft");
        Map<String, String> map = stream3.collect(Collectors.toMap(
                //把元素s映射为key
                s -> s.substring(0, s.indexOf(':')),
                //将元素s映射为value
                s -> s.substring(s.indexOf(':') + 1)
        ));

        System.out.println(map);
        System.out.println("***********************");

        //4.分组输出
        Map<String, List<String>> groupList = Stream.of("Apple", "Banana", "Blackberry", "Coconut", "Avocado", "Cherry", "Apricots")
                .collect(
                        Collectors.groupingBy(s -> s.substring(0, 1), Collectors.toList())
                );
        System.out.println(groupList);
    }

    @Test
    /**
     * Stream 除了提供转换和聚合操作外，还提供了一些其他操作：
     * 转换操作：map()，filter()，sorted()，distinct()；
     * 聚合操作：reduce()，collect()，count()，max()，min()，sum()，average()；
     * 合并操作：concat()，flatMap()；
     * 并行处理：parallel()；
     * 其他操作：allMatch(), anyMatch(), forEach()。
     */
    public void OtherOppStream() {

        //1.排序,sorted()只是一个转换操作，它会返回一个新的Stream
        List<String> sortedList = Stream.of("Orange", "apple", "Banana")
                //忽略大小写排序
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        System.out.println(sortedList);

        //2.排序，对一个Stream的元素进行去重，没必要先转换为Set，可以直接用distinct()：
        List<String> distinctList = Stream.of("A", "B", "A", "C", "B", "D")
                .distinct()
                .collect(Collectors.toList());
        System.out.println(distinctList);

        //3.截取
        //截取操作常用于把一个无限的Stream转换成有限的Stream，skip()用于跳过当前Stream的前N个元素，limit()用于截取当前Stream最多前N个元素：
        List<String> limitList = Stream.of("A", "B", "C", "D", "E", "F")
                //跳过A、B
                .skip(2)
                //跳过C、D
                .limit(3)
                .collect(Collectors.toList());
        System.out.println(limitList);

        //4.合并
        //将两个Stream合并为一个Stream可以使用Stream的静态方法concat()
        Stream<String> s1 = Stream.of("A", "B", "C");
        Stream<String> s2 = Stream.of("D", "E");
        Stream<String> concat = Stream.concat(s1, s2);
        System.out.println(concat.collect(Collectors.toList()));

        //5.flatMap
        // 是指把Stream的每个元素（这里是List）映射为Stream，然后合并成一个新的Stream
        Stream<List<Integer>> s = Stream.of(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9));
        Stream<Integer> flatMapStream = s.flatMap(list -> list.stream());
        System.out.println(flatMapStream.collect(Collectors.toList()));

        //6.并行
        //把一个普通Stream转换为可以并行处理的Stream非常简单，只需要用parallel()进行转换
//        Stream<String> s = ...
//        String[] result = s.parallel() // 变成一个可以并行处理的Stream
//                .sorted() // 可以进行并行排序
//                .toArray(String[]::new);

    }

}
