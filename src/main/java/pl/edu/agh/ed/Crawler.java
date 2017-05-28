package pl.edu.agh.ed;

import java.util.stream.IntStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.edu.agh.ed.Consts;
import pl.edu.agh.ed.dao.AuthorManipulate;
import pl.edu.agh.ed.dao.CategoryManipulate;
import pl.edu.agh.ed.dao.CommentManipulate;
import pl.edu.agh.ed.dao.CommentTopicManipulate;
import pl.edu.agh.ed.dao.FactoryMaker;
import pl.edu.agh.ed.dao.PostManipulate;
import pl.edu.agh.ed.dao.PostTagManipulate;
import pl.edu.agh.ed.dao.PostTopicManipulate;
import pl.edu.agh.ed.dao.TagManipulate;
import pl.edu.agh.ed.dao.TopicManipulate;
import pl.edu.agh.ed.objects.Author;
import pl.edu.agh.ed.objects.Category;
import pl.edu.agh.ed.objects.Comment;
import pl.edu.agh.ed.objects.CommentTopic;
import pl.edu.agh.ed.objects.Post;
import pl.edu.agh.ed.objects.PostTag;
import pl.edu.agh.ed.objects.PostTopic;
import pl.edu.agh.ed.objects.Tag;
import pl.edu.agh.ed.objects.Topic;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class Crawler {

	/**
	 * Fabryka polaczen do tabeli autorow
	 */
	private static AuthorManipulate AM = new AuthorManipulate(
			FactoryMaker.getSessionFactory(Author.class));

	/**
	 * Fabryka polaczen do tabeli kategorii
	 */
	private static CategoryManipulate CM = new CategoryManipulate(
			FactoryMaker.getSessionFactory(Category.class));

	/**
	 * Fabryka polaczen do tabeli komentarzy
	 */
	private static CommentManipulate CommentM = new CommentManipulate(
			FactoryMaker.getSessionFactory(Comment.class));

	/**
	 * Fabryka polaczen do tabeli lacznikowej komentarz a topic
	 */
//	private static CommentTopicManipulate CTopicM = new CommentTopicManipulate(
//			FactoryMaker.getSessionFactory(CommentTopic.class));

	/**
	 * Fabryka polaczen do tabeli postow
	 */
	private static PostManipulate PM = new PostManipulate(
			FactoryMaker.getSessionFactory(Post.class));
	/**
	 * Set postow
	 */
	private static Set<String> postSet = new HashSet<String>();

	/**
	 * Fabryka polaczen do tabeli lacznikowej post a tag
	 */
//	private static PostTagManipulate PTM = new PostTagManipulate(
//			FactoryMaker.getSessionFactory(PostTag.class));
	/**
	 * Fabryka polaczen do tabeli lacznikowej post a topic
	 */
//	private static PostTopicManipulate PTopicM = new PostTopicManipulate(
//			FactoryMaker.getSessionFactory(PostTopic.class));
	/**
	 * Fabryka polaczen do tabeli tagow
	 */
	private static TagManipulate TM = new TagManipulate(
			FactoryMaker.getSessionFactory(Tag.class));
	/**
	 * Fabryka polaczen do tabeli topikow
	 */
	private static TopicManipulate TopicM = new TopicManipulate(
			FactoryMaker.getSessionFactory(Topic.class));
	
	/**
	 * Tablica linkow juz dodanych przy rekurencyjnym wyluskiwaniu linkow
	 */
	private static ArrayList<String> links_numbers = new ArrayList<String>();

	
	// --------------------------------------------------------------- Helper Functions
	/**
	 * Laczy liste stringow w jeden string
	 * 
	 * @param words
	 *            lista stringow
	 * @return polaczony string
	 */
	private static String connectStrings(Map<String, Integer> words)
	{
		ValueComparator bvc = new ValueComparator(words);
		TreeMap<String, Integer> sorted_map = new TreeMap<String, Integer>(bvc);
		sorted_map.putAll(words);
		String s = "";
		if (words.size() <= 10) {
			s = StringUtils.join(sorted_map.keySet(), " ");
		} else {
			int i = 0;
			Iterator<String> it = sorted_map.keySet().iterator();
			while (i < 10 && it.hasNext()) {
				s += (it.next() + " ");
				i++;
			}
		}
		return s;
	}

	/**
	 * Znajduje najbardziej wykorzystywane slowa w tekscie
	 * 
	 * @param text
	 *            tekst
	 * @return slowa
	 */
	private static String countWords(String text)
	{
		text = text.toLowerCase();
		// remove any "\n" characters that may occur
		String temp = text.replaceAll("[\\n]", " ");

		// replace any grammatical characters and split the String into an array
		String[] splitter = temp.replaceAll("[.,?!:;/]", "").split(" ");

		// intialize an int array to hold count of each word
		List<String> splitterList = new ArrayList<String>(
				Arrays.asList(splitter));

		int[] counter = new int[splitterList.size()];

		// loop through the sentence
		for (int i = 0; i < splitterList.size(); i++) {

			// hold current word in the sentence in temp variable
			temp = splitterList.get(i);

			// inner loop to compare current word with those in the sentence
			// incrementing the counter of the adjacent int array for each match
			for (int k = 0; k < splitterList.size(); k++) {

				if (temp.equalsIgnoreCase(splitterList.get(k))) {
					counter[k]++;
				}
			}
		}
		Map<String, Integer> words = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < splitterList.size(); i++) {
			words.put(splitterList.get(i), counter[i]);
		}

		for (String avoid : Consts.avoids)
		{
			words.remove(avoid);
		}

		return connectStrings(words);
	}

	/**
	 * Uzupelnia dane o autorze
	 * 
	 * @param author
	 *            autor
	 * @param doc
	 *            dokument
	 */
	private static String getAuthor(Author author, Document doc)
	{
		for (Element el : doc.getElementsByAttributeValue("class", "bloger-name"))
		{
			author.setName(el.html());//<span class="author-card__details__name">Sam Stein</span>
			author.setLink(doc.getElementsByAttributeValue("class", "bloger-name").attr("abs:href"));
		}
//		if (author.getName() == null)
//		{
//			//<div class="wire-byline">
//			//<span>Roberta Rampton and Steve Holland</span>
//			//</div>
//			for (Element el : doc.getElementsByAttributeValue("class", "wire-byline"))
//			{
//				author.setName(el.text());
//				author.setLink(doc.getElementsByAttributeValue("class", "author-card__details__link").attr("abs:href"));
//			}
//		}
		return author.getName();
	}

	/**
	 * Uzupelnia dane o kategorii
	 * 
	 * @param category
	 *            kategoria
	 * @param doc
	 *            dokument
	 */
	private static void getCategory(Category category, Document doc)
	{
		for (Element el : doc.getElementsByAttributeValue("class", "breadcrumb"))
		{
			Matcher m = Pattern.compile(Consts.PATTERN_GET_CATEGORY_S24).matcher(el.text());
			if (m.find())
			{
				System.out.println(m.group(1));
				category.setCategoryName(m.group(1));
			}
//			System.out.println(el.attr("content"));
//			category.setCategoryName(el.text());//attr("content")
		}
	}

	/**
	 * Uzupelnia tresc posta
	 * 
	 * @param post
	 *            post
	 * @param doc
	 *            dokument
	 */
	private static void getContent(Post post, Document doc)
	{
		Elements els = doc.getElementsByAttributeValue("class", Consts.CONTENT_CLASS);
		String content = "";
		for (Element el : els)
		{
			content += el.select("p").text();
		}
		post.setContent(content);
	}
	
	private static void getTags(Post post, Document doc)
	{
		Elements els = doc.getElementsByAttributeValue("class", Consts.TAGS_CLASS);
		for (Element el : els)
		{
			if(el.text() == "More")
			{
				continue;
			}
			
			Tag tag = new Tag();
			tag.setName(el.text());
			Tag found_tag = TM.getTagByName(tag);
			if (found_tag != null)
			{
				tag = found_tag;
			}
			else
			{
				TM.addTag(tag);
				if (tag.getId() == 0) {
					System.err.println("Tag nie zapisal sie!!!");
					return;
				}
			}
			PostTag postTag = new PostTag();
			postTag.setPost_id(post);
			postTag.setTag_id(tag);
//			PTM.addPostTag(postTag);
		}
	}

	private static void getDate(Post post, Document doc) throws ParseException
	{
		for (Element el : doc.getElementsByClass("created"))
		{
			DateFormat df = new SimpleDateFormat("dd MMMMM yyyy 'r.'");//yyyy-MM-dd'T'HH:mm:ssXXX	| MM/dd/yyyy HH:mm
			post.setDate(df.parse(el.text()));
			System.out.println(post.getDate());
		}
	}

	/**
	 * Metoda wyszukujaca nowych linkow na podstawie juz znanej listy linkow
	 * kilka poziomow wglab
	 * 
	 * @param sites
	 *            lista zdefiniowanych linków z serwisu
	 * @param level
	 *            poziom zaglebienia poszukiwan
	 * @param fileName
	 *            nazwa pliku do którego maj¹ zostaæ zapisane nowe pliki
	 * @throws IOException
	 */
	private static void getNewLinks(List<String> sites, int level, String fileName) throws IOException
	{
		if (level > Consts.LEVEL_DEPTH)
		{
			return;
		}
		int numberLink = 1;
		int u = 1;
		List<String> nextLinks = new ArrayList<String>();
		for (String link : sites)
		{
			System.out.println(u + " / " + sites.size());
			try
			{
				Document doc = Jsoup.connect(link).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(0).get();
				Elements links = doc.select("a[href]");

				for (Element url : links)
				{
					String urlLink = null;
					urlLink = url.attr("abs:href");

					if(urlLink.startsWith("http://www.huffingtonpost.com/entry/"))
					{
						Matcher m = Pattern.compile(Consts.PATTERN_GET_SITE_ID).matcher(urlLink);
						if(m.find() && !links_numbers.contains(m.group(1)))
						{
							System.out.println(" > group > " + m.group(1));

							
							System.out.println(" > " + urlLink);
							if (postSet.add(urlLink))
							{
								links_numbers.add(m.group(1));
								nextLinks.add(urlLink);
								PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
								out.println(urlLink);
								out.close();
								System.out.println(urlLink + " level " + level
										+ " HREF" + "( " + numberLink + "/" + sites.size() + ")");
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				System.out.println(e.toString());
				continue;
			}
			numberLink++;
			u++;
		}

		level++;

		getNewLinks(nextLinks, level, fileName);
	}
	
	private static void getNewLinks_from_s24_najnowszeNotki(String fileName) throws IOException
	{
		List<String> nextLinks = new ArrayList<String>();
		if(nextLinks.isEmpty())
		{
			for(int najNotki_currPage = Consts.S24_NAJNOWSZENOTKI_START_PAGEID; najNotki_currPage > 0; --najNotki_currPage)
			{
				String starting_link = Consts.S24_NAJNOWSZENOTKI_PAGE + najNotki_currPage;
				System.out.println(starting_link);
				int numberLink = 1;
				try
				{
					Document doc = Jsoup.connect(starting_link).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(0).get();
					Elements archive_class_els = doc.getElementsByAttributeValue("class", Consts.S24_NAJNOWSZENOTKI_CLASS);
					Elements links = archive_class_els.select("a[href]");
					
					for (Element url : links)
					{
						String urlLink = null;
						urlLink = url.attr("abs:href");
						System.out.println(urlLink);

						if(urlLink.contains(".salon24.pl"))
						{
							if (postSet.add(urlLink))
							{
								nextLinks.add(urlLink);
								PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
								out.println(urlLink);
								out.close();
								System.out.println(" > " + numberLink + ": " + urlLink);
							}
						}
						numberLink++;
					}
				}
				catch (Exception e)
				{
					System.out.println(e.toString());
				}
			}
			
		}
	}
	
	private static void getNewLinks_from_archive(String fileName) throws IOException
	{
		List<String> nextLinks = new ArrayList<String>();
		if(nextLinks.isEmpty())
		{
			Integer[] years = {2016};
			Integer[] months_30 = {2, 4, 6, 9, 11};
			for(int year : years)
			{
				IntStream.range(1, 13).forEach( month ->
				{
					int no_days = 32;
					if(Arrays.asList(months_30).contains(month))
						no_days = 31;
					if(month == 2)
						no_days = 30;
					
					IntStream.range(1, no_days).forEach( day ->
					{
						String starting_link = Consts.HP_ARCHIVE_PAGE + year + "-" + month + "-" + day;
						System.out.println(starting_link);
						int numberLink = 1;
						try
						{
							Document doc = Jsoup.connect(starting_link).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(0).get();
							Elements archive_class_els = doc.getElementsByAttributeValue("class", Consts.HP_ARCHIVE_CLASS);
							Elements links = archive_class_els.select("li").select("a[href]");
							
							for (Element url : links)
							{
								String urlLink = null;
								urlLink = url.attr("abs:href");

								if(urlLink.startsWith("http://www.huffingtonpost.com/" + year + "/"))
								{
									if (postSet.add(urlLink))
									{
										nextLinks.add(urlLink);
										PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName,true)));
										out.println(urlLink);
										out.close();
										System.out.println(" > " + numberLink + ": " + urlLink);
									}
								}
								numberLink++;
							}
						}
						catch (Exception e)
						{
							System.out.println(e.toString());
						}
					});
					
				});
				
			}
		}
	}

	/**
	 * Uzupelnia numer posta
	 * 
	 * @param site
	 *            link
	 * @param post
	 *            post
	 */
	private static void getSiteNum(String site, Post post)
	{
		Matcher m = Pattern.compile(Consts.PATTERN_GET_SITE_ID_S24).matcher(site);
		while (m.find())
		{
			System.out.println(m.group(1));
			post.setSite(m.group(1));
		}
	}

	/**
	 * Uzupelnia tytul posta
	 * 
	 * @param post
	 *            post
	 * @param doc
	 *            dokument
	 */
	private static void getTitle(Post post, Document doc)
	{
		Elements els = doc.getElementsByClass(Consts.TITLE_CLASS).select("header > h1");
		for (Element el : els)
		{
			System.out.println(el.text());
			post.setTitle(el.text());
		}
	}
	
	private static void get_comment_from_JSONObject(JSONObject comment_object, Post parent_post, Comment parent_comment) throws JSONException, ParseException, IOException
	{
		Comment comment = new Comment();
		
		comment.setFBid(comment_object.getString("id"));
		comment.setPost(parent_post);
		
		JSONObject author_object = comment_object.getJSONObject("from");
		
		Author author = new Author();
		author.setName(author_object.getString("name"));
		author.setFBid(author_object.getString("id"));
//		author.setLink(elAuthor.attr("href").equals("#") ? author.getName() : elAuthor.attr("href"));
		
		Author found_author = AM.getAuthorByName(author.getName());
		if (found_author != null)
		{
			author = found_author;
		}
		else
		{
			if (author.getLink() == null && author.getName() == null)
			{
				System.err.println("Autor jest pusty!!!");
				return;
			}
			AM.addAuthor(author);
			if (author.getId() == 0)
			{
				System.err.println("Autor nie zapisal sie!!!");
				return;
			}
		}
		comment.setAuthor(author);
		
		comment.setContent(comment_object.getString("message"));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");//, Locale.US
		comment.setDate(dateFormat.parse(comment_object.getString("created_time")));
				
		if(parent_comment != null)
		{
			comment.setComment(parent_comment);
		}
		
		CommentM.addComment(comment);
		
		
		JSONArray comments_data_array = get_JSONArray_form_comment_block_id(comment_object.getString("id"));
		for(int i = 0; i < comments_data_array.length(); ++i) // comments_data_array.length() moze byc 0
		{
			get_comment_from_JSONObject(comments_data_array.getJSONObject(i), parent_post, comment);
		}
		
		
		Topic topic = new Topic();
		topic.setKeywords(countWords(comment.getContent()));
		Topic found_topic = TopicM.getTopicByName(topic.getKeywords());
		if (found_topic != null)
		{
			topic = found_topic;
		}
		else
		{
			if (topic.getKeywords() == null)
			{
				System.err.println("Topic jest pusty!!!");
				return;
			}
			TopicM.addTopic(topic);
			if (topic.getId() == 0)
			{
				System.err.println("Topic nie zapisal sie!!!");
				return;
			}
		}
		CommentTopic commentTopic = new CommentTopic();
		commentTopic.setComment_id(comment);
		commentTopic.setTopic_id(topic);
//		CTopicM.addCommentTopic(commentTopic);
	}
	
	private static JSONArray get_JSONArray_form_comment_block_id(String comment_block_id) throws JSONException, IOException
	{
		String comments_url = Consts.FB_COMMENTS_START + comment_block_id + Consts.FB_COMMENTS_END + Consts.FB_ACCESS_TOKEN;
		JSONObject comments_json = readJsonFromUrl(comments_url);
		JSONArray comments_data_array = comments_json.getJSONArray("data");
		return comments_data_array;
				
	}
	
	private static void manage_comments(String site, Post post) throws IOException, JSONException, ParseException
	{
		String comment_block_url = Consts.FB_COMMENTS_GET_ID + site + Consts.FB_ACCESS_TOKEN;
		JSONObject json = readJsonFromUrl(comment_block_url);
		JSONObject og_object = json.getJSONObject("og_object");
		String comment_block_id = og_object.getString("id");
		
		JSONArray comments_data_array = get_JSONArray_form_comment_block_id(comment_block_id);
		for(int i = 0; i < comments_data_array.length(); ++i)
		{
			get_comment_from_JSONObject(comments_data_array.getJSONObject(i), post, null);
		}
	}
	
	// ---------------------------------------------------------------

	/**
	 * G³ówna metoda crawluj¹ca
	 * 
	 * @throws IOException
	 */
	public static void crawl(String avoidLinks, String definedLinks, String savedLinks) throws IOException
	{
		/**
		 * Lista linków ktore chcemy pominac zaczytywane z pliku
		 */
		List<String> old = Files.readLines(new File(avoidLinks), Charsets.UTF_8);
		for (String a : old)
		{
			postSet.add(a);
		}

		/**
		 * Zdefiniowane linki z których chcemy pozyskac nowe, które zostaj¹
		 * zapisane w pliku
		 */
//		List<String> sites = Files.readLines(new File(definedLinks),
//				Charsets.UTF_8);
//		getNewLinks(sites, 1, savedLinks);
		
		/**
		 * Pobiera linki z archiwum HuffingtonPosta
		 */
//		getNewLinks_from_archive("files//archive_links_2016.txt");
		
//		getNewLinks_from_s24_najnowszeNotki("files//najnowszenotki.txt");
		
		/**
		 * Parsuje linki HuffingtonPosta ze starego do nowego ('_us_xnox') formatu
		 */
//		parse_old_formated_links("files//archive_links_2016.txt", "files//new_links_2016.txt");

		/**
		 * Zaczytywanie plików które chcemy dodaæ do bazy danych
		 */
		List<String> postLinks = Files.readLines(new File(savedLinks), Charsets.UTF_8);

		for (int i = 0; i < postLinks.size(); i += 1)
		{
			parsePageAndAddToDB(postLinks.get(i));
			System.out.println("DODANO postów " + (i+1) + " z " + postLinks.size());
			sleep(2000);
		}
	}
	
	protected static void sleep(int max_milliseconds)
	{
		try
		{
			Thread.sleep((int)(Math.random() * max_milliseconds));
		}
		catch (Exception ignored) { }
	}
	
	// <meta content="http://www.huffingtonpost.com/entry/x_us_yyy" property="og:url">
	private static void parse_old_formated_links(String archive_links_file, String us_links_file) throws IOException
	{
		List<String> links = Files.readLines(new File(archive_links_file), Charsets.UTF_8);
		
		int no_link = 1;
		for(String link : links)
		{
			try
			{
				Document doc = Jsoup.connect(link).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(10000).get();
				
				Elements meta_content = doc.getElementsByAttributeValue("property", "og:url");
				String us_link = meta_content.attr("content");
				
				if(!us_link.isEmpty() && is_politics(doc) && getAuthor(new Author(), doc) != null)
				{
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(us_links_file,true)));
					out.println(us_link);
					out.close();
				}
				
				System.out.println(" > " + no_link + "/" + links.size() + " : " + us_link);
			}
			catch(org.jsoup.HttpStatusException e)
			{
				if(e.getStatusCode() == 503)
				{
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("503_2016_error.txt",true)));
					out.println(e.getUrl());
					out.close();
				}
				else
				{
					PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("000_2016_error.txt",true)));
					out.println(e.toString());
					out.close();
				}
			}
			catch(Exception e)
			{
				System.out.println(e);
			}
			++no_link;
			sleep(2);
		}
	}

	// <meta content="Politics" property="article:section">
	private static boolean is_politics(Document doc)
	{
		Elements meta_content = doc.getElementsByAttributeValue("property", "article:section");
		String article_content_keyword = meta_content.attr("content");
		System.out.println(article_content_keyword);
		if(article_content_keyword.equals("Politics"))
			return true;
		else
			return false;
	}
	/**
	 * Parsowanie strony oraz dodanie jej ze wszystkim do bazy danych
	 * 
	 * @param site
	 * @throws IOException 
	 */
	public static void parsePageAndAddToDB(String site) throws IOException
	{
		try
		{
			if (PM.checkIfExistsLink(site))
			{
				System.err.println("Post juz istnieje!!!");
				return;
			}
			
			Document doc = Jsoup.connect(site).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").timeout(0).get();
			
			// 1
			Author author = new Author();
			getAuthor(author, doc);
			Author found_author = AM.getAuthorByName(author.getName());
			if (found_author != null)
			{
				author = found_author;
			}
			else
			{
				if (author.getLink() == null && author.getName() == null)
				{
					System.err.println("Autor jest pusty!!!");
					return;
				}
				AM.addAuthor(author);
				if (author.getId() == 0)
				{
					System.err.println("Autor nie zapisal sie!!!");
					return;
				}
			}
			
			// 2
			Category category = new Category();
			getCategory(category, doc);

			Category found_category = CM.getCategoryByName(category
					.getCategoryName());
			if (found_category != null)
			{
				category = found_category;
			}
			else
			{
				if (category.getCategoryName() == null)
				{
					System.err.println("Kategoria jest pusta!!!");
					return;
				}
				CM.addCategory(category);
				if (category.getId() == 0)
				{
					System.err.println("Kategoria nie zapisala sie!!!");
					return;
				}
			}
			
			// 3
			Post post = new Post();
			post.setLink(site);
			getSiteNum(site, post);
			getDate(post, doc);
			getTitle(post, doc);
			getContent(post, doc);
			post.setZrobiona(false);
			post.setAuthor(author);
			post.setCategory(category);
			PM.addPost(post);
			if (post.getId() == 0)
			{
				System.err.println("Post sie nie zapisal!!!");
				return;
			}

			// 4 + 5
//			getTags(post, doc);
			
			// 6 + 7
//			Topic topic = new Topic();
//			topic.setKeywords(countWords(post.getContent()));
//			Topic findedTopic = TopicM.getTopicByName(topic.getKeywords());
//			if (findedTopic != null)
//			{
//				topic = findedTopic;
//			}
//			else
//			{
//				if (topic.getKeywords() == null)
//				{
//					System.err.println("Topic jest pusty!!!");
//				}
//				TopicM.addTopic(topic);
//				if (topic.getId() == 0) {
//					System.err.println("Topic nie zapisal sie!!!");
//					return;
//				}
//			}

//			PostTopic postTopic = new PostTopic();
//			postTopic.setPost_id(post);
//			postTopic.setTopic_id(topic);
//			PTopicM.addPostTopic(postTopic);
			
			// Komentarze FB
			// 8+9
			
			manage_comments(site, post);
		}
		catch(org.jsoup.HttpStatusException e)
		{
			if(e.getStatusCode() == 503)
			{
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("503_2016_error.txt",true)));
				out.println(e.getUrl());
				out.close();
			}
			else
			{
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("000_2016_error.txt",true)));
				out.println(e.toString());
				out.close();
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Wczytywanie do string'aa z reader'a
	 * 
	 * @param rd
	 *            reader
	 * @return string
	 * @throws IOException
	 *             exception
	 */
	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	/**
	 * Wczytywanie json z url'a
	 * 
	 * @param url
	 *            url
	 * @return json
	 * @throws IOException
	 *             exception
	 * @throws JSONException
	 *             exception
	 */
	public static JSONObject readJsonFromUrl(String url) throws IOException,
			JSONException {
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}

	/**
	 * Usuwanie html-tagow z komentarzy
	 */
	private static void removeTags() {
		CommentManipulate CM = new CommentManipulate(
				FactoryMaker.getSessionFactory(Comment.class));
		CM.deleteTags();
		System.out.println("KONIEC");

	}

	/**
	 * Porownuje wartosci w topikach
	 */
	private static class ValueComparator implements Comparator<String>
	{
		/**
		 * Baza
		 */
		Map<String, Integer> base;

		/**
		 * Konstruktor
		 * 
		 * @param base
		 *            baza
		 */
		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			}
		}
	}

}
