package com.landowner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.core.convert.converter.Converter;

public class Test {

	public static void main(String[] args) {
		test11();
	}
	private static void test1() {
		List<Persion> list = Arrays.asList(new Persion(1,"张三",1,16),new Persion(2,"李四",2,17),new Persion(3,"王五",2,18),new Persion(4,"周六",2,19));
		int asInt = list.stream().mapToInt(p -> p.getAge()).map(age -> age + 1).reduce((sum,age) -> sum * age).getAsInt();
		System.out.println(asInt);
	}
	private static void test2() {
		List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		features.stream().map(obj -> obj.toLowerCase()).forEach(System.out::println);
	}
	private static void test3() {
		List<String> list = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		List<String> collect = list.stream().filter(x -> x.length() > 10).collect(Collectors.toList());
		System.out.printf("Original List : %s, filtered list : %s", list, collect);
		System.out.println();
		System.out.printf("Original List : %s, filtered list : %s %n", list, collect);
	}
	private static void test4() {
		List<String> list = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
		List<Integer> list1 = Arrays.asList(5, 6, 7, 8);
		String collect = list.stream().map(str -> str.toUpperCase()).collect(Collectors.joining(","));
		double average = list1.stream().mapToInt(x -> x ).summaryStatistics().getAverage();
		System.out.println(average);
	}
	private static void test5() {
		
		List<Integer> list = Arrays.asList(5, 6, 7, 8);
		Integer i = 0;
		list.forEach(element -> {System.out.println(element*i);});
	}
	private static void test6() {
		LocalDateTime now = LocalDateTime.now();
		System.out.println(now);
		System.out.println(now.toLocalTime());
		System.out.println(now.withDayOfMonth(10).withYear(2012));
	}
	
	private static void test7() {
		Converter<String,Integer> con = Integer::valueOf;
		Integer converted = con.convert("123");
		System.out.println(converted);   // 123
	}
	
	private static void test8() {
		Comparator<Persion> p = (p1,p2) -> p1.getName().compareTo(p2.getName());
		Persion persion = new Persion(1,"张三",1,16);
		Persion persion2 = new Persion(2,"李四",2,17);
		int compare = p.compare(persion, persion2);
		int compare2 = p.reversed().compare(persion, persion2);
		System.out.println(compare);
		System.out.println(compare2);
	}
	
	private static void test9() {
		List<String> stringCollection = new ArrayList<>();
		stringCollection.add("ddd2");
		stringCollection.add("aaa2");
		stringCollection.add("bbb1");
		stringCollection.add("aaa1");
		stringCollection.add("bbb3");
		stringCollection.add("ccc");
		stringCollection.add("bbb2");
		stringCollection.add("ddd1");
		
		//stringCollection.stream().filter(s -> s.startsWith("a")).forEach(System.out::println);
//		stringCollection.stream().sorted().forEach(System.out::println);
		/*boolean anyStartsWithA = 
			    stringCollection
			        .stream()
			        .anyMatch((s) -> s.startsWith("a"));

			System.out.println(anyStartsWithA);*/    
		String str =
			    stringCollection
			        .stream()
			        .sorted()
			        .reduce((s1, s2) -> s1 + "#" + s2).get();
			System.out.println(str);
	}
	
	private static void test10() {
		int max = 1000000;
		List<String> values = new ArrayList<>(max);
		for (int i = 0; i < max; i++) {
		    UUID uuid = UUID.randomUUID();
		    values.add(uuid.toString());
		}
		long t0 = System.nanoTime();
		values.stream().sorted().count();
		long t1 = System.nanoTime();
		System.out.println("stream:\t\t"+(t1-t0));
		
		long t2 = System.nanoTime();
		values.parallelStream().sorted().count();
		long t3 = System.nanoTime();
		System.out.println("parallelStream:\t"+(t3-t2));

	}
	
	private static void test11() {
		Consumer out = System.out::println;
		
		Map<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 10; i++) {
		    map.putIfAbsent(i, "val" + i);
		}
		
		map.computeIfPresent(3, (num, val) -> val + num);
		out.accept(map.get(3));             // val33 
		String computeIfPresent = map.computeIfPresent(9, (num, val) -> "");
		out.accept(computeIfPresent); 
		out.accept(map.containsKey(9)); 
		out.accept(map.get(9));
		
	}
	
	public static void filter(List<String> names, Predicate<String> condition) {
	    names.stream().filter((name) -> (condition.test(name))).forEach((name) -> {
	        System.out.println(name + " ");
	    });
	}
}
