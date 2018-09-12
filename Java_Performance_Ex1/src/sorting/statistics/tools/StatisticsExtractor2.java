package sorting.statistics.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;

public class StatisticsExtractor2 {

	public static void main(String[] args) throws IOException {

		JFileChooser fc = new JFileChooser("C:\\Users\\Sallaberger\\Desktop\\extracted");
		fc.setMultiSelectionEnabled(true);
		fc.showOpenDialog(null);
		List<File> files = Arrays.asList(fc.getSelectedFiles());

		for (File file : files) {
			List<String> lines = Files.readAllLines(file.toPath());
			lines.forEach(StatisticsExtractor2::extractTimes);
		}

	}

	private static void extractTimes(String string) {
		final String regex = "[0-9]+,[0-9]+";
		final Pattern pattern = Pattern.compile(regex);
		final Matcher matcher = pattern.matcher(string);

		while (matcher.find()) {
			System.out.print(matcher.group(0).replace(",", ".") + ";");
		}
		System.out.println();
	}
}
