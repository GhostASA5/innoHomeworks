package org.project;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTask {

    public static void main(String[] args) throws IOException {
        cycleGrayCode(17).limit(20).forEach(System.out::println);

        Path inputFile = Path.of("resources/input.txt");
        Path outputFile = Path.of("resources/output.txt");
        int n = 10;

        findMostFrequentWords(inputFile, outputFile, n);
    }

    public static Stream<Integer> cycleGrayCode(int n) {
        if (n < 1 || n > 16) {
            System.out.println("n должно быть от 1 до 16");
            return Stream.empty();
        }
        int size = 1 << n;
        return Stream.iterate(0, i -> (i + 1) % size)
                .map(i -> i ^ (i >> 1));
    }

    public static void findMostFrequentWords(Path inputFile, Path outputFile, int n) throws IOException {
        String text = Files.readString(inputFile);

        Map<String, Long> frequencyMap = Arrays.stream(text.toLowerCase().split("\\W+"))
                .filter(word -> !word.isBlank())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        List<String> mostFrequentWords = frequencyMap.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed()
                        .thenComparing(Map.Entry::getKey))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();

        mostFrequentWords.forEach(System.out::println);

        Files.write(outputFile, mostFrequentWords);
    }
}
