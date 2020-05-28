package JP.java_pro;

import edu.uci.ics.crawler4j.crawler.*;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.*;
import java.util.*;
import java.net.URL;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Controller extends Thread {
	public UI ui;
	public int mode = 0;
	public String keywords;
	public List < String > seedNext;
	public String crawlStorageFolder;
	public int numberOfCrawlers;
	public CrawlConfig config;
	public PageFetcher pageFetcher;
	public RobotstxtConfig robotstxtconfig;
	public RobotstxtServer robotstxtserver;
	public CrawlController controller;

	// 因為要在爬蟲時同時讓 loading icon 動，所以重新設計成多線程

	// Controller 傳 ui 讓他能爬完蟲後去呼叫 ui
	// mode 0 = search title, mode 1 = search author
	public Controller ( UI ui, String keywords, int mode ) throws Exception {
		this.ui = ui;
		this.keywords = keywords;
		this.init ( );
		this.mode = mode;
	}

	// some init and start run()

	private void init ( ) throws Exception {

		seedNext = new ArrayList < String > ( );
		crawlStorageFolder = "./data/crawl/root";
		numberOfCrawlers = 1;
		config = new CrawlConfig ( );
		config.setCrawlStorageFolder ( crawlStorageFolder );
		pageFetcher = new PageFetcher ( config );
		robotstxtconfig = new RobotstxtConfig ( );
		robotstxtserver = new RobotstxtServer ( robotstxtconfig, pageFetcher );
		controller = new CrawlController ( config, pageFetcher, robotstxtserver );
		this.start ( );
	}

	public static void saveImage ( String imageUrl ) throws IOException {
		URL url = new URL ( imageUrl );
		String fileName = url.getFile ( );

		String destName = "./figures" + fileName.substring ( fileName.lastIndexOf ( "/" ) );
		System.out.println ( destName );

		InputStream is = url.openStream ( );
		OutputStream os = new FileOutputStream ( destName );

		byte[] b = new byte[2048];
		int length;

		while ( ( length = is.read ( b ) ) != -1 ) {
			os.write ( b , 0 , length );
		}

		is.close ( );
		os.close ( );
	}

	// previos main, after Crawing call ui

	public void run ( ) {
		// search title
		if ( mode == 0 )
			controller.addSeed ( "https://www.ptt.cc/bbs/C_Chat/search?q=" + keywords );
		// search author 
		else if ( mode == 1 )
			controller.addSeed ( "https://www.ptt.cc/bbs/C_Chat/search?q=author:" + keywords );
			
		controller.start ( myCrawler.class , numberOfCrawlers );

		seedNext = myCrawler.seed;
		java.util.Iterator < String > iterator = seedNext.iterator ( );
		// Is /figures exist?
		Path check_figures = Paths.get ( "./figures" );
		if ( !Files.exists ( check_figures ) ) {
			System.out.println ( "./figures create" );
			try {
				Files.createDirectory ( check_figures );
			}
			catch ( IOException e ) {
				// TODO Auto-generated catch block
				e.printStackTrace ( );
			}
		}
		else {
			System.out.println ( "./figures exist" );
		}

		ui.finishCraw ( );

	}
}
