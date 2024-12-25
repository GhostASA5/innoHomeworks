package org.project;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ListTask {

    private static final String readFromFile = "resources/numbersList.txt";

    private static final String writeToFile = "resources/outputList.txt";

    public static void main(String[] args) {

        Integer[] arr = readArrayFromFile();
        System.out.println("Исходный масив: " + Arrays.toString(arr));

        List<Integer> list = new ArrayList<>(List.of(arr));

        Collections.sort(list);
        System.out.println("Сортировка в натуральном порядке: " + list);
        writeToFile(list);

        list.sort(Collections.reverseOrder());
        System.out.println("Сортировка в обратном порядке: " + list);
        writeToFile(list);

        Collections.shuffle(list);
        System.out.println("Перемешанный список: " + list);
        writeToFile(list);

        Collections.rotate(list, 1);
        System.out.println("Сдвиг на 1 элемент: " + list);
        writeToFile(list);

        List<Integer> uniqueList = new HashSet<>(list).stream().toList();
        System.out.println("Только уникальные элементы: " + uniqueList);

        List<Integer> duplicates = findDuplicates(list);
        System.out.println("Только дублирующиеся элементы: " + duplicates);
        writeToFile(duplicates);

        int[] finalArr = list.stream().mapToInt(i -> i).toArray();
        System.out.println("Массив из списка: " + Arrays.toString(finalArr));
        writeToFile(Arrays.stream(finalArr).boxed().collect(Collectors.toList()));

    }

    private static List<Integer> findDuplicates(List<Integer> list) {
        Set<Integer> unique = new HashSet<>();
        Set<Integer> duplicates = new HashSet<>();
        for (Integer num : list) {
            if (unique.contains(num)) {
                duplicates.add(num);
            } else {
                unique.add(num);
            }
        }
        return new ArrayList<>(duplicates);
    }

    private static Integer[] readArrayFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader(ListTask.readFromFile))) {
            String line = br.readLine();
            String[] arr = line.split(";");
            return Arrays.stream(arr).map(Integer::parseInt).toArray(Integer[]::new);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void writeToFile(List<Integer> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writeToFile, true))) {
            for (Integer num : list) {
                bw.write(num + " ");
            }
            bw.write("\n");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
