package pl.edu.agh.ed;

import java.io.IOException;

public class Main
{
	public static void main(String[] args) throws IOException
	{
		Crawler.crawl("files\\avoid_links.txt", "files\\defined_links.txt", "files\\new_links_2015.txt");
		
		System.out.println("END.\n");
	}
}
