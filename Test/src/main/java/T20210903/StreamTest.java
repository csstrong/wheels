package T20210903;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
/**
 *
 * @author chensi
 * @date 2021/9/3
 */
public class StreamTest {

    public static void main(String[] args) throws IOException {
        StreamTest streamTest = new StreamTest();
        streamTest.test10();
    }

    public void test1(){
        //1.Individual values
        Stream stream=Stream.of("a","b","c");

        //2.Arrays
        String[] strArray=new String[]{"a","b","c"};
        stream=Stream.of(strArray);
        stream= Arrays.stream(strArray);

        //3.Collections
        List<String> list=Arrays.asList(strArray);
        stream=list.stream();
        
    }

    public void test2(){
        IntStream.of(new int[]{1,2,3}).forEach(System.out::println);
        IntStream.range(1,3).forEach(System.out::println);
        IntStream.rangeClosed(1,3).forEach(System.out::println);
    }

    public void test3(){
        //1.Array
        Stream stream=Stream.of("a","b","c");
        String[] strArray1= (String[]) stream.toArray(String[]::new);

        //2.Collection
        List<String> list1= (List<String>) stream.collect(Collectors.toList());
        List<String> list2= (List<String>) stream.collect(Collectors.toCollection(ArrayList::new));
        Set set1= (Set) stream.collect(Collectors.toSet());
        Stack stack1= (Stack) stream.collect(Collectors.toCollection(Stack::new));

        //3.String
        String str=stream.collect(Collectors.joining()).toString();
    }

    public void test4(){
        List<Integer> nums=Arrays.asList(1,2,3,4);
        List<Integer> squareNums=nums.stream().map(n->n*n)
                .collect(Collectors.toList());

        Stream<List<Integer>> inputStream=Stream.of(
                Arrays.asList(1),
                Arrays.asList(2,3),
                Arrays.asList(4,5,6)
        );
        Stream<Integer> outputStream=inputStream.
                flatMap((childList)->childList.stream());
    }

    public void test5(){
        //filter 留下偶数
        Integer[] sixNums={1,2,3,4,5,6};
        Integer[] events=Stream.of(sixNums).filter(n->n%2==0).toArray(Integer[]::new);
        for (int i = 0; i < events.length; i++) {
            System.out.println(events[i]);
        }
    }

    public void test6(){
        //reduce
        /**
         * 主要作用是把Stream元素组合起来。它提供一个起始值，然后依照运算规则，和前面Stream的第一个、第二个、
         * 第n个元素组合。从这个意义上说，字符串拼接，数值的sum/min/max/average都是特殊的reduce。
         */
        //字符串拼接，concat="ABCD"
        String concat=Stream.of("A","B","C","D").reduce("",String::concat);

        //求最小值 minValue=-3.0
        double minValue=Stream.of(-1.5,1.0,-3.0,-2.0).reduce(Double.MAX_VALUE,Double::min);

        //求和 sumValue=10 有起始值
        int sumValue=Stream.of(1,2,3,4).reduce(0,Integer::sum);

        //求和 sumValue=10 无起始值
        sumValue=Stream.of(1,2,3,4).reduce(Integer::sum).get();

        //过滤 字符串连接 concat="ace"
        concat=Stream.of("a","B","c","D","e","F").
                filter(x->x.compareTo("Z")>0).
                reduce("",String::concat);
    }

    public void test7(){
        List<Person> persons = new ArrayList<>();
        for (int i = 1; i < 10000; i++) {
            Person person = new Person(i, "name" + i);
            persons.add(person);
        }
        List<String> personList2= (List<String>) persons.stream().
                map(Person::getName).limit(10).skip(3).collect(Collectors.toList());
        System.out.println(personList2);

    }

    public void test8(){
        //sorted 可以首先对Stream进行各类map/filter/limit/skip甚至distinct来减少元素数量后再排序
        List<Person> persons=new ArrayList<>();
        for (int i = 0; i <=5; i++) {
            Person person=new Person(i,"name"+i);
            persons.add(person);
        }
        List<Person> personList2=persons.stream().limit(2).sorted(
                (p1,p2)->p1.getName().compareTo(p2.getName())).
                collect(Collectors.toList());
        System.out.println(personList2);
    }

    public void test9() throws IOException {
        //min/max/distinct 找出最长一行的长度
        BufferedReader br=new BufferedReader(new FileReader("e:\\1.txt"));
        int longest=br.lines().mapToInt(String::length).max().getAsInt();
        br.close();
        System.out.println(longest);
    }

    public void test10() throws IOException {
        //通过distinct来找出不重复的单词
        //找出全文的单词，转小写，并排序
        BufferedReader br=new BufferedReader(new FileReader("E:\\tmp\\1.txt"));
        List<String> words=br.lines().flatMap(line->Stream.of(line.split(" "))).
                filter(word->word.length()>0).map(String::toLowerCase).distinct().sorted()
                .collect(Collectors.toList());
        br.close();
        System.out.println(words);
    }

    public void test11(){
        /**
         * Stream有三个match方法，从语义上说：
         * 1）allMatch:Stream中全部元素符合传入的predicate，返回true
         * 2)anyMatch:Stream中只要有一个元素符合传入的predicate，返回true
         * 3）noneMatch:Stream 中没有一个元素符合传入的 predicate，返回 true
         */
        // 使用 Match
        List<Person> persons = new ArrayList();
//        persons.add(new Person(1, "name" + 1, 10));
//        persons.add(new Person(2, "name" + 2, 21));
//        persons.add(new Person(3, "name" + 3, 34));
//        persons.add(new Person(4, "name" + 4, 6));
//        persons.add(new Person(5, "name" + 5, 55));

//        boolean isAllAdult = persons.stream().allMatch(p -> p.getAge() > 18);
//        System.out.println("All are adult? " + isAllAdult);
//        boolean isThereAnyChild = persons.stream().anyMatch(p -> p.getAge() < 12);
//        System.out.println("Any child? " + isThereAnyChild);
    }

}
class Person{
    public int no;
    private String name;
    public Person(int no,String name){
        this.no=no;
        this.name=name;
    }
    public String getName(){
        System.out.println(name);
        return name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "no=" + no +
                ", name='" + name + '\'' +
                '}';
    }
}
