package pl.edu.agh.ed;

import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Crawler.crawl("files\\all_link.txt", "files\\aaa.txt", "files\\www.huffingtonpost.com_8th_Dec_2016.txt");
		
		System.out.println("END.\n");
	}
}
