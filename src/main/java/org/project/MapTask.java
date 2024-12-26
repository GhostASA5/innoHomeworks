package org.project;

import java.io.*;
import java.util.*;

public class MapTask {

    private static final String readFromFile = "resources/numbersMap.txt";

    private static final String writeToFile = "resources/outputMap.txt";

    public static void main(String[] args) {

        Map<String, String> hashMap = readMapFromFile();
        System.out.println("Исходная HashMap: " + hashMap);
        writeToFile(hashMap);

        hashMap.put("Key11", "Value1");
        hashMap.put("Key12", "Value2");
        hashMap.put("Key13", "Value1");
        hashMap.put("Key14", "Value3");
        hashMap.put("Key15", "Value2");
        System.out.println("HashMap после добавления элементов: " + hashMap);
        writeToFile(hashMap);

        System.out.println("Перебор HashMap:");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            System.out.println("Ключ: " + entry.getKey() + ", Значение: " + entry.getValue());
        }

        hashMap.put("key1", "NewValue1");
        System.out.println("HashMap после обновления значения ключа 'key1': " + hashMap);
        writeToFile(hashMap);

        List<String> keys = new ArrayList<>(hashMap.keySet());
        System.out.println("Список ключей: " + keys);

        Set<String> uniqueValues = new HashSet<>(hashMap.values());
        System.out.println("Уникальные значения: " + uniqueValues);
        System.out.println("Количество уникальных значений: " + uniqueValues.size());

        System.out.println("Содержит ли ключ 'key2': " + hashMap.containsKey("key2"));

        System.out.println("Содержит ли значение 'value3': " + hashMap.containsValue("value3"));

        System.out.println("Количество элементов в HashMap: " + hashMap.size());

        hashMap.remove("key4");
        System.out.println("HashMap после удаления по ключу 'key4': " + hashMap);
        writeToFile(hashMap);

        hashMap.values().remove("value2");
        System.out.println("HashMap после удаления всех элементов со значением 'value2': " + hashMap);
        writeToFile(hashMap);

    }

    private static Map<String, String> readMapFromFile() {
        Map<String, String> map = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(readFromFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    map.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;
    }

    private static void writeToFile(Map<String, String> map) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(writeToFile, true))) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue() + "\n");
            }
            bw.write("\n");
            bw.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
