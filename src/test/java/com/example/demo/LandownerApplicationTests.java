package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LandownerApplicationTests {

	@Test
	public void contextLoads() {
		Student student = new Student(1,"yi",1);
		Student student2 = new Student(2,"er",2);
		Student student3 = new Student(3,"san",3);
		List<Student> list = new ArrayList<Student>();
		list.add(student);
		list.add(student2);
		list.add(student3);
		Map<Integer, String> collect = list.stream().collect(Collectors.toMap(Student::getId, Student::getName));
		collect.forEach((index, o) -> System.out.println(index +"----->"+o));
	}
	
	public static void main(String[] args) {
		/*Student student = new Student(1,"yi",1);
		Student student2 = new Student(2,"er",2);
		Student student3 = new Student(3,"san",3);
		List<Student> list = new ArrayList<Student>();
		list.add(student);
		list.add(student2);
		list.add(student3);
		Map<Integer, String> collect = list.stream().collect(Collectors.toMap(Student::getId, Student::getName));
		collect.forEach((index, o) -> System.out.println(index +"----->"+o));*/
		
		System.out.println(new JSONObject());
		System.out.println("{}");
	}

}
