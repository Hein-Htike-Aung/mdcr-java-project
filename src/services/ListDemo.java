package services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ListDemo {

	public static void main(String[] args) {

		List<String> nameList = new ArrayList<>();

		nameList.add("John");
		nameList.add("Merry");
		nameList.add("John");

		List<String> filteredNames = nameList.stream()
				.filter(name -> name.toLowerCase().startsWith("j".toLowerCase()))
				.collect(Collectors.toList());
		
		
		filteredNames.forEach(fn -> System.out.println(fn));
		

	}
}
