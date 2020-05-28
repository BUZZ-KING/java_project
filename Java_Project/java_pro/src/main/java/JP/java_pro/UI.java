package JP.java_pro;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import javax.swing.event.*;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;

import org.w3c.dom.events.MouseEvent;

public class UI extends JFrame implements Ibase {

    // constructor, title, size
    public UI ( String title, int width, int height ) {
        // call parent ctor 
        super ( title );

        // set frame size 
        this.setSize ( width , height );
        this.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        this.setLayout ( null );

        // init search button and radio button 
        Ibase.initBtn ( this.searchBtn , 100 , 30 , width - 150 , 100 );
        Ibase.initRBtn ( this.searchByKeywords , 100 , 20 , 10 , 80 );
        Ibase.initRBtn ( this.searchByAuthor , 100 , 20 , 10 , 120 );
        this.searchByKeywords.setSelected ( true );

        this.radioBtnGrp.add ( this.searchByKeywords );
        this.radioBtnGrp.add ( this.searchByAuthor );

        // loading text
        this.loadIcon.setImage ( loadIcon.getImage ( ).getScaledInstance ( width , height , Image.SCALE_DEFAULT ) );
        this.loadingJLabel = new JLabel ( loadIcon );
        this.loadingJLabel.setLocation ( -20 , -20 );
        this.loadingJLabel.setSize ( width , height );
        this.loadingJLabel.setVisible ( false );

        // init text field 
        this.inputBox.setLocation ( this.searchBtn.getX ( ) - 230 , this.searchBtn.getY ( ) );
        this.inputBox.setSize ( this.inputBox.getColumns ( ) , 30 );

        //init Picture button
        Ibase.initBtn ( this.nextButton , 150 , 30 , width - 200 , height - 100 );
        Ibase.initBtn ( this.saveButton , 150 , 30 , width - 200 , height - 150 );
        Ibase.initBtn ( this.preButton , 150 , 30 , width - 350 , height - 100 );
        Ibase.initBtn ( this.backButton , 150 , 30 , width - 500 , height - 100 );
        this.nextButton.setVisible ( false );
        this.saveButton.setVisible ( false );
        this.preButton.setVisible ( false );
        this.backButton.setVisible ( false );

        //init show picture label
        this.showPictureJLabel.setLocation ( 0 , 0 );
        this.showPictureJLabel.setSize ( width - 100 , height - 100 );
        this.showPictureJLabel.setVisible ( false );

        // set events
        this.searchBtn.addActionListener ( act -> {
            inputBox.setVisible ( false );
            searchBtn.setVisible ( false );
            searchByKeywords.setVisible ( false );
            searchByAuthor.setVisible ( false );
            loadingJLabel.setVisible ( true );
        } );
        this.searchBtn.addActionListener ( act -> {
            try {
                this.search ( this.inputBox.getText ( ) );
            }
            catch ( Exception e1 ) {
                e1.printStackTrace ( );
            }
        } );
        this.inputBox.addFocusListener ( new FocusListener ( ) {
            @Override
            public void focusLost ( FocusEvent e ) {
            }

            @Override
            public void focusGained ( FocusEvent e ) {
                inputBox.setText ( "" );
            }
        } );
        //儲存按紐
        this.saveButton.addActionListener ( act -> {
            try {
                Controller.saveImage ( myCrawler.seed.get ( pic_index ) );
            }
            catch ( IOException e1 ) {
                e1.printStackTrace ( );
            }
        } );
        //next picture
        this.nextButton.addActionListener ( act -> {
            if ( pic_index < myCrawler.seed.size ( ) - 1 ) {
                pic_index++;
                try {
                    this.picture = pictureNext;
                    this.picture.setImage ( picture.getImage ( ).getScaledInstance ( showPictureJLabel.getWidth ( ) , showPictureJLabel.getHeight ( ) , Image.SCALE_DEFAULT ) );
                    this.showPictureJLabel.setIcon ( picture );
                    if(pic_index-1>=0) {
                    	this.picturePre = returnIcon ( new URL ( myCrawler.seed.get ( pic_index-1 ) ) );
                    }
                    if(pic_index+1 < myCrawler.seed.size ( ) - 1) {
                    	this.pictureNext = returnIcon ( new URL ( myCrawler.seed.get ( pic_index+1 ) ) );
                    }
                    
                }
                catch ( MalformedURLException e1 ) {
                    e1.printStackTrace ( );
                } 
                catch ( IOException e1 ) {
					e1.printStackTrace ( );
				}
            }
        } );
        //back to main MENU
        this.backButton.addActionListener ( act -> {
            this.inputBox.setVisible ( true );
            this.searchBtn.setVisible ( true );
            this.searchByKeywords.setVisible ( true );
            this.searchByAuthor.setVisible ( true );
            this.preButton.setVisible ( false );
            this.saveButton.setVisible ( false );
            this.nextButton.setVisible ( false );
            this.backButton.setVisible ( false );
            this.showPictureJLabel.setVisible ( false );
            this.pic_index = 0;
            myCrawler.seed.clear ( );
        } );
        //previous picture
        this.preButton.addActionListener ( act -> {
            if ( pic_index > 0 ) {
                pic_index--;
                try {
                    this.picture = picturePre;
                    this.picture.setImage ( picture.getImage ( ).getScaledInstance ( showPictureJLabel.getWidth ( ) , showPictureJLabel.getHeight ( ) , Image.SCALE_DEFAULT ) );
                    this.showPictureJLabel.setIcon ( picture );
                    if(pic_index-1>=0) {
                    	this.picturePre = returnIcon( new URL ( myCrawler.seed.get ( pic_index - 1 ) ) );
                    }
                    if(pic_index+1 < myCrawler.seed.size ( ) - 1) {
                    	this.pictureNext = returnIcon( new URL ( myCrawler.seed.get ( pic_index + 1 ) ) );
                    }
                }
                catch ( MalformedURLException e1 ) {
                    e1.printStackTrace ( );
                } 
                catch ( IOException e1 ) {
					e1.printStackTrace( );
				}
            }
        } );

        // add components 
        this.add ( searchBtn );
        this.add ( inputBox );
        this.add ( loadingJLabel );

        this.add ( searchByKeywords );
        this.add ( searchByAuthor );

        this.add ( nextButton );
        this.add ( saveButton );
        this.add ( preButton );
        this.add ( backButton );
        this.add ( showPictureJLabel );

        // show this frame 
        this.setVisible ( true );
    }

    private void search ( String keywords ) throws Exception {
        System.out.println ( "SEARCH : " + keywords );

        int mode = 0;
        if ( this.searchByKeywords.isSelected ( ) ) {
            mode = SearchKeywords;
        }
        else if ( this.searchByAuthor.isSelected ( ) ) {
            mode = SearchAuthor;
        }
        main_controller = new Controller ( this, keywords, mode );
    }

    //finishCraw, show button for control picture(next, pre, backToMENU, save)
    public void finishCraw ( ) {
        this.loadingJLabel.setVisible ( false );
        this.nextButton.setVisible ( true );
        this.saveButton.setVisible ( true );
        this.preButton.setVisible ( true );
        this.backButton.setVisible ( true );
        this.showPictureJLabel.setVisible ( true );
        if ( myCrawler.seed.size ( ) != 0 ) {
            try {
            	url = new URL ( myCrawler.seed.get ( pic_index ) );
            	
                this.picture = returnIcon(url);
                this.picture.setImage ( picture.getImage ( ).getScaledInstance ( showPictureJLabel.getWidth ( ) , showPictureJLabel.getHeight ( ) , Image.SCALE_DEFAULT ) );
                this.showPictureJLabel.setIcon ( picture );
                if(pic_index+1 < myCrawler.seed.size ( ) - 1) {
                	this.pictureNext = new ImageIcon ( new URL ( myCrawler.seed.get ( pic_index +1) ) );
                	//this.pictureNext.setImage ( picture.getImage ( ).getScaledInstance ( showPictureJLabel.getWidth ( ) , showPictureJLabel.getHeight ( ) , Image.SCALE_DEFAULT ) );
                	//this.showPictureJLabel.setIcon ( picture );
                }
            }
            catch ( Exception e ) {
                e.printStackTrace ( );
            }
        }
        else {
            System.out.println ( "no pic" );
        }
    }
    
    //for some fxxking 404, we need something
    
    public ImageIcon returnIcon( URL url ) throws IOException {
    	URLConnection connection = url.openConnection();
    	connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
    	InputStream in = connection.getInputStream();
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	int c;
        while ((c = in.read()) != -1) {
        	out.write(c);
        }
    	
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(out.toByteArray()));
        return icon;
    }

    private Controller main_controller = null;
    private int pic_index = 0;
    private JLabel showPictureJLabel = new JLabel ( );
    private ImageIcon loadIcon = new ImageIcon ( "./icon/icon.gif" );
    private ImageIcon picture = new ImageIcon ( );
    private ImageIcon picturePre = new ImageIcon ( );
    private ImageIcon pictureNext = new ImageIcon ( );
    private JButton searchBtn = new JButton ( "Search" );
    private JLabel loadingJLabel;
    private JTextField inputBox = new JTextField ( "Enter Search Keywords Here !", 200 );
    private JButton nextButton = new JButton ( "Next Picture" );
    private JButton saveButton = new JButton ( "Save Picture" );
    private JButton preButton = new JButton ( "Previous Picture" );
    private JButton backButton = new JButton ( "Back to MENU" );
    
    private URL url;

    private ButtonGroup radioBtnGrp = new ButtonGroup ( );
    private JRadioButton searchByKeywords = new JRadioButton ( "Keywords" );
    private JRadioButton searchByAuthor = new JRadioButton ( "Author" );

    public static final int SearchKeywords = 0;
    public static final int SearchAuthor = 1;
}