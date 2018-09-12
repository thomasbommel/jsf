package sorting.statistics.tools;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFileChooser;

public class StatisticsExtractor {

	public static void main(String[] args) throws IOException {
		JFileChooser fc = new JFileChooser("C:\\Users\\Sallaberger\\Desktop\\statistics");
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(null);

		List<File> files = Arrays.asList(fc.getSelectedFiles());

		new File("extracted").mkdir();

		for (File file : files) {
			List<String> lines = Files.readAllLines(file.toPath());
			extractFile(file, lines, "sorting.Benchmark.benchmarkQuickSort");
			extractFile(file, lines, "sorting.Benchmark.benchmarkCubeSort");
			extractFile(file, lines, "sorting.Benchmark.benchmarkMergeSort");
		}
	}

	private static void extractFile(File file, List<String> lines, String sort) throws IOException {
		Stream<String> lineStream = lines.stream().filter(line -> isSortingLine(line, sort));
		Path newFilePath = Paths.get("extracted", file.getName() + "_" + sort + "_extracted.txt");
		Files.write(newFilePath, lineStream.collect(Collectors.toList()), Charset.defaultCharset());
	}

	private static boolean isSortingLine(String line, String sort) {
		return line.startsWith(sort) && line.contains("<---") && line.contains("sort([");
	}

}
