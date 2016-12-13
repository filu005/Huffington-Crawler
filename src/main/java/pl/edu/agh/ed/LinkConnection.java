package pl.edu.agh.ed;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.edu.agh.ed.dao.CommentManipulate;
import pl.edu.agh.ed.dao.FactoryMaker;
import pl.edu.agh.ed.dao.PostManipulate;
import pl.edu.agh.ed.objects.Comment;
import pl.edu.agh.ed.objects.Post;

/**
 * Klasa bedaca pomocnikiem przy tworzeniu/obliczaniu danych potrzebnych do
 * stworzenia sieci linków
 * 
 * @author Kuba
 * 
 */
public class LinkConnection {
	/**
	 * Fabryka polaczen do tabeli postow
	 */
	private static PostManipulate PM = new PostManipulate(
			FactoryMaker.getSessionFactory(Post.class));

	/**
	 * Fabryka polaczen do tabeli komentarzy
	 */
	private static CommentManipulate CommentM = new CommentManipulate(
			FactoryMaker.getSessionFactory(Comment.class));

	/**
	 * Mapa odwzorowujaca link na ilosc linków, które byly trescia
	 * postu/komentarzy pod danym linkiem
	 */
	private static Map<String, Integer> linkers = new HashMap<String, Integer>();

	/**
	 * Mapa odwzorowujzaca link na ilosc linków, które na niego wskazywaly
	 */
	private static Map<String, Integer> linked = new HashMap<String, Integer>();

	/**
	 * Metoda generujaca plik CSV, który moze byc wykorzystany przez Gephi do
	 * stworzenia sieci
	 * 
	 * @param d1
	 *            data poczatkowa
	 * @param d2
	 *            data koncowa
	 * @param filename
	 *            nazwa pliku wyjsciowego
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void generateLinkConnectionsFile(Date d1, Date d2, String filename)
			throws FileNotFoundException, UnsupportedEncodingException {

		List<Post> posts = PM.getLinkByDate(Util.getBeginOfDay(d1),
				Util.getEndOfDay(d2));

		List<String> csv = new ArrayList<String>();

		for (Post p : posts) {
			getLinks(p.getContent(), csv, p.getLink());
			List<Comment> comments = CommentM.getCommentByPost(p);
			for (Comment comm : comments) {
				getLinks(comm.getContent(), csv, p.getLink());
			}
		}

		class LinkerObject {
			Integer count;
			String link;

			public Integer getCount() {
				return this.count;
			}

			public String getLink() {
				return this.link;
			}

			public LinkerObject(Integer i, String s) {
				this.count = i;
				this.link = s;
			}
		}

		List<LinkerObject> linkersList = new ArrayList<LinkerObject>();
		List<LinkerObject> linkedList = new ArrayList<LinkerObject>();

		for (String link : linkers.keySet()) {
			LinkerObject lo = new LinkerObject(linkers.get(link), link);
			linkersList.add(lo);
		}

		for (String link : linked.keySet()) {
			LinkerObject lo = new LinkerObject(linked.get(link), link);
			linkedList.add(lo);
		}

		Collections.sort(linkersList, new Comparator<LinkerObject>() {

			public int compare(LinkerObject o1, LinkerObject o2) {
				return o2.getCount().compareTo(o1.getCount());
			}

		});

		Collections.sort(linkedList, new Comparator<LinkerObject>() {

			public int compare(LinkerObject o1, LinkerObject o2) {
				return o2.getCount().compareTo(o1.getCount());
			}

		});

		System.out.println("=== ''Top 10 post with most links:'' === ");

		for (int i = 0; i <= 9; i++) {
			System.out.println("|| " + (i + 1) + ". || "
					+ linkersList.get(i).getLink() + " || "
					+ linkersList.get(i).getCount() + " ||");
		}

		System.out
				.println("\n=== ''Top 10 pages with forwarding link in post:'' ===");

		for (int i = 0; i <= 9; i++) {
			System.out.println("|| " + (i + 1) + ". || "
					+ linkedList.get(i).getLink() + " || "
					+ linkedList.get(i).getCount() + " ||");
		}
		PrintWriter writer = new PrintWriter("files\\" + filename + ".csv", "UTF-8");
		for (String line : csv) {
			writer.println(line.replaceAll(";", "").replaceAll("|", "")
					.replaceAll(",", ""));
		}
		writer.close();
	}

	/**
	 * Metoda zapisujaca do listy powiazania pomiedzy linkami
	 * 
	 * @param content
	 *            zawartosc posta/komentarza
	 * @param csv
	 *            lista powiazanych linkow
	 * @param link
	 *            link do posta
	 */
	private static void getLinks(String content, List<String> csv, String link) {

		String regex = "\\(?\\b(http://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}

			putToMaps(link, urlStr);

			if (csv.contains(urlStr + " " + link)) {
				csv.add(urlStr + " " + link);
			} else {
				csv.add(link + " " + urlStr);
			}
		}
	}

	/**
	 * Metoda wstawiajaca linki do odpowiednich map, czy dany link jest
	 * linkowany, czy linkujacy
	 * 
	 * @param link
	 *            link ktory jest linkujacy
	 * @param urlStr
	 *            link do którego linkowal <code>link</code>
	 */
	private static void putToMaps(String link, String urlStr) {
		if (linkers.get(link) != null) {
			Integer i = linkers.get(link);
			linkers.remove(link);
			linkers.put(link, ++i);
		} else {
			linkers.put(link, new Integer(1));
		}

		if (linked.get(urlStr) != null) {
			Integer i = linked.get(urlStr);
			linked.remove(link);
			linked.put(urlStr, ++i);
		} else {
			linked.put(urlStr, new Integer(1));
		}

	}
}
