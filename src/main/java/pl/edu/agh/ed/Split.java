package pl.edu.agh.ed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

/**
 * Dzieli plik z linkami na porcje
 */
public class Split {
	public static void main(String[] args) throws IOException {
		List<String> links = Files.readLines(new File("files\\copy_links.txt"),
				Charsets.UTF_8);
		for (int i = 0; i < links.size(); i += 500) {
			for (int j = 0; j < 500; j++) {
				PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter("files\\new_link_" + i + ".txt", true)));
				out.println(links.get(i + j));
				out.close();
			}
		}

	}
}
