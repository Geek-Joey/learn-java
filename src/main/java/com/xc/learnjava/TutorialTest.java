package com.xc.learnjava;

import org.junit.Test;

import java.util.Scanner;

/**
 * @author joey
 */
public class TutorialTest {

    public static void main(String[] args) {

        //1.String不可变(字符串内容不可变)
        String s = "hello";
        String t = s;
        s = "world";
        // t是"hello"还是"world"?
        System.out.println("s="+s+",t="+ t);

        //请将下面一组int值视为字符的Unicode码，把它们拼成一个字符串：
        int a = 72;
        int b = 105;
        int c = 65281;
        // FIXME:
        String str = "" + (char)a + (char)b + (char)c ;
        System.out.println(str);
        String str1 = String.valueOf((char)a);
        System.out.println(str1);
        System.out.println("***********************");

        //2.数组是引用类型，并且数组大小不可变. 可以通过索引访问数组元素，但索引超出范围将报错；
        //数组元素可以是值类型（如int）或引用类型（如String），但数组本身是引用类型；

        // 5位同学的成绩:
        int[] ns = new int[] { 68, 79, 91, 85, 62 };
        // 5
        System.out.println(ns.length);
        ns = new int[] { 1, 2, 3 };
        // 3
        System.out.println(ns.length);
        System.out.println("***********************");

        //3.格式化输出：System.out.printf()，通过使用占位符%?，printf()可以把后面的参数格式化成指定格式，常用的格式如下：
        //%d	格式化输出整数
        //%x	格式化输出十六进制整数
        //%f	格式化输出浮点数
        //%e	格式化输出科学计数法表示的浮点数
        //%s	格式化字符串
        //更多详细的格式化参数，可以参考：https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Formatter.html#syntax


        //example: 把一个整数格式化成十六进制，并用0补足8位：
        int n = 12345000;
        System.out.printf("n=%d, hex=%08x\n", n, n);
        System.out.println("***********************");

        //4.输入
        //创建Scanner对象并传入System.in。System.out代表标准输出流，而System.in代表标准输入流。
        Scanner input = new Scanner(System.in);
        System.out.println("Please input your name: ");
        String name = input.nextLine();
        System.out.println("Continue input your age: ");
        int age = input.nextInt();
        System.out.printf("Hi,%s,you are %d years old.\n",name,age);
        System.out.println("***********************");


        //5.if条件判断
        //5.1 判断浮点数运算
        double x = 1 - 9.0 / 10;
        System.out.println("x=" + x );
        //浮点数在计算机中常常无法精确表示，并且计算可能出现误差，因此，判断浮点数相等用==判断不靠谱
        if (x == 0.1) {
            System.out.println("x is 0.1");
        } else {
            System.out.println("x is NOT 0.1");
        }
        //Fix
        if (Math.abs(x - 0.1) < 0.00001) {
            System.out.println("x is 0.1");
        } else {
            System.out.println("x is NOT 0.1");
        }
        System.out.println("***********************");

        //5.2 判断引用类型的变量内容是否相等：使用equals()方法：
        String s1 = "hello";
        String s2 = "HELLO".toLowerCase();
        System.out.println("s1=" + s1 + ",s2=" + s2);
        if (s1.equals(s2)) {
            System.out.println("s1 equals s2");
        } else {
            System.out.println("s1 not equals s2");
        }
        System.out.println("***********************");


        /**
         * 6.switch 语句:
         *  根据switch (表达式)计算的结果，跳转到匹配的case结果，然后继续执行后续语句，直到遇到break结束执行。
         *  从Java 12开始，switch语句升级为使用类似模式匹配（Pattern Matching），保证只有一种路径会被执行，并且不需要break语句.
         *  Java 14正式发布switch表达式特性,可以使用return或yield返回一个值作为switch语句的返回值
         */
        int option = 2;
        switch (option) {
            case 1:
                System.out.println("Select 1");
                break;
            //没有遇到break,会继续执行
            case 2:
            case 3:
                System.out.println("Select 2,3");
                break;
            default:
                System.out.println("No Selected");
                break;
        }
        System.out.println("***********************");

        /**
         * 7.while 循环
         *  while循环先判断循环条件是否满足，再执行循环语句；
         *  while循环可能一次都不执行；
         *  编写循环时要注意循环条件，并避免死循环。
         */
        int sum = 0;
        int i = 1;
        while (i <= 100) {
            sum += i;
            i ++;
        }
        System.out.println("sum="+ sum);

        /**
         * 8.do-while循环
         * 先执行循环，再判断条件，条件满足时继续循环，条件不满足时退出;
         * do while循环会至少执行一次。
         */
        sum = 0;
        int j = 1;
        do {
            sum += j;
            j ++;
        } while(j<=100);
        System.out.println("sum="+ sum);

        //使用do while计算 x1 + ... + y1
        sum = 0;
        int x1 = 20;
        int y1 = 100;
        do {
            sum += x1;
            x1 ++;
        } while (x1<=y1) ;
        System.out.println("sum=" + sum);
        System.out.println("***********************");

        /**
         * 9.for循环
         * for循环通过计数器可以实现复杂循环；
         * for each循环可以直接遍历数组的每个元素；
         * 最佳实践：计数器变量定义在for循环内部，循环体内部不修改计数器；
         */

        //9.1 对一个整型数组的所有元素求和，可以用for循环实现
        int[] arr = { 1, 4, 9, 16, 25 };
        sum = 0 ;
        for (int f = 0; f < arr.length; f++) {
            System.out.println("f=" + f + ",arr[f]=" + arr[f]);
            sum += arr[f];
        }
        System.out.println("sum = " + sum );
        System.out.println("***********************");

        //9.2 for each
        for (int i1 : arr) {
            System.out.println(i1);
        }
        System.out.println("***********************");

        /**
         * 10.break与continue
         * break语句可以跳出当前循环；
         * break语句通常配合if，在满足条件时提前结束整个循环；
         * break语句总是跳出最近的一层循环；
         *
         * continue语句可以提前结束本次循环；
         * continue语句通常配合if，在满足条件时提前结束本次循环。
         */

        //10.1 break
        for (int k = 1; k <= 10; k++) {
            System.out.println("k = " + k);
            for (int l = 1; l <= 10 ; l++) {
                System.out.println("l = " + l);
                if (l >= k)
                    break;
            }
        }
        System.out.println("Broke");
        System.out.println("***********************");

        //10.2 continue
        int count = 0;
        for (int i3 = 1; i3 <= 10; i3++) {
            System.out.println("begin = " + i3);
            if (i3 %2 == 0){
                continue ;
            }
            count += i3;
            System.out.println("end = " + i3);
            System.out.println("tmp count = " + count);
        }
        System.out.println("count = " + count);
    }
}
