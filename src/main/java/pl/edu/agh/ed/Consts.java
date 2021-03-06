package pl.edu.agh.ed;

/**
 * Stale zmienne
 */
public class Consts
{
	/**
	 * Slowa, ktorych nie nalezy liczyc do czesto sie powtarzajacych
	 */
	public static final String[] avoids = new String[] { "i", "a", "about",
			"an", "and", "are", "as", "at", "be", "by", "com", "de", "en",
			"for", "from", "how", "in", "is", "it", "la", "of", "on", "or",
			"that", "the", "this", "to", "was", "what", "when", "where", "who",
			"will", "with", "und", "the", "www", "their", "have", "had",
			"would", "us" };

	/**
	 * ID html-taga do tresci posta
	 */
	public static final String CONTENT_CLASS = "content-list-component text";
	
	public static final String TAGS_CLASS = "tag";

	/**
	 * Nazwa html-klasy do tytulu
	 */
	public static final String TITLE_CLASS = "headline__title";

	/**
	 * Koniec linka do pobierania komentarzy
	 */
	public static final String FB_COMMENTS_START = "https://graph.facebook.com/";
	// + comment_block_id
	public static final String FB_COMMENTS_END = "/comments?summary=1&limit=10000";
	
	public static final String FB_COMMENTS_GET_ID = "https://graph.facebook.com/v2.8/?fields=og_object{id},share&id=";
	public static final String FB_ACCESS_TOKEN = "&access_token=1787348511525222|y--c1m0BFDATTAhxksqrmtuHod8";
	
	public static final String HP_ARCHIVE_PAGE = "http://www.huffingtonpost.com/archive/";
	public static final String HP_ARCHIVE_CLASS = "archive";
	
	/**
	 * Poziom zaglebienia w wyszukiwaniu linkow
	 */
	public static final int LEVEL_DEPTH = 20;

	/**
	 * Szablon do usuwania html-tagow z tresci
	 */
	public static final String PATTERN_DELETE_TAGS = "(?i)<(?!(/?(a)))[^>]*>";

	/**
	 * Szablon do wyciagniecia numeru posta
	 */
	public static final String PATTERN_GET_SITE_ID = "\\_us\\_([0-9a-z]+)";
}
