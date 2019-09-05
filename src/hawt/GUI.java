package hawt;

import java.awt.BorderLayout;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EtchedBorder;

public class GUI 
{
	Game game;
	JTextField InpTxt;
	JLabel InpInfo; JLabel WrdLbl; JLabel GLbl; JLabel LstLbl;
	char[] input_text;
	
	GUI() throws FileNotFoundException
	{
		// Create window
		JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Hangman");
		guiFrame.setSize(200, 250);
		guiFrame.setLocationRelativeTo(null);
		guiFrame.setLayout(new BorderLayout());
		guiFrame.addWindowListener(new WindowAdapter()
				{
			public void windowClosing(WindowEvent we) 
			{
				guiFrame.dispose();
			}
				});
		
		// Upper panel showing hidden word
		JPanel WrdPnl = new JPanel(new BorderLayout());
		JLabel WrdLbl = new JLabel("WELCOME TO HANGMAN");
		WrdPnl.add(WrdLbl, BorderLayout.CENTER);
		
		// Middle panel showing hanging man image and list of guessed letters
		JPanel GPnl = new JPanel(new BorderLayout());
		JTextArea GLbl = new JTextArea(Gallows.current_gallow(0));
		GLbl.setEditable(false);
		JLabel LstLbl = new JLabel("[]");
		GPnl.add(GLbl, BorderLayout.CENTER);
		GPnl.add(LstLbl, BorderLayout.PAGE_END);
		
		// Lower panel containing user input field
		JPanel InpPnl = new JPanel(new BorderLayout());
		JLabel InpInfo = new JLabel("Select File > New Game to Start!");
		JTextField InpTxt = new JTextField ("Input Here");
		InpTxt.setVisible(false);
		InpPnl.add(InpInfo, BorderLayout.LINE_START); InpPnl.add(InpTxt, BorderLayout.CENTER);
		//--When enter is pressed
		InpTxt.addActionListener(new ActionListener()
				{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				input_text = InpTxt.getText().toCharArray();
				game.get_guess(input_text);
				WrdLbl.setText(game.return_hidden());
				GLbl.setText(Gallows.current_gallow(game.number_of_misses));
				LstLbl.setText(String.format("%s", game.guessed_letters));
				InpTxt.setText("");
				
				if (game.won == 1) 
				{
					InpTxt.setVisible(false);
					InpInfo.setText("YOU WIN!");
					game.record_score();
				} else if (game.lost == 1)
				{
					InpTxt.setVisible(false);
					InpInfo.setText("YOU LOST! THE WORD IS " + game.return_correct());
					game.record_score();
				}
				
			}
				});
				
		// Create menubar
		MenuBar menubar = new MenuBar();
		Menu menu = new Menu("File");
		MenuItem newGame = new MenuItem("New Game");
		//--New Game initializes Game
		newGame.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent event)
			{
				try {
					game = new Game();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				InpTxt.setVisible(true);
				InpInfo.setText("Type guess here:");
				WrdLbl.setText(game.return_hidden());
				GLbl.setText(Gallows.current_gallow(game.number_of_misses));
				LstLbl.setText(String.format("%s", game.guessed_letters));
			}
		});
		//--Scores opens new window with scores from scores.txt
		MenuItem viewScores = new MenuItem("Scores");
		viewScores.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent event)
			{
				File scoreFile = new File(game.SCORE_PATH);
				ArrayList<String> scoreLst = new ArrayList<String>();
				
				Scanner sc;
				try 
				{
					sc = new Scanner(scoreFile);
					while(sc.hasNext()) 
					{
						scoreLst.add(sc.nextLine());
					} 
					sc.close();
				}
				catch(FileNotFoundException e) 
				{
						e.printStackTrace();
				}
				
				JFrame scoreWindow = new JFrame();
				scoreWindow.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				scoreWindow.setTitle("Scores");
				scoreWindow.setSize(300, 300);
				scoreWindow.setLocationRelativeTo(null);
				
				JPanel scorePnl = new JPanel();
				JTextArea scoreTxt = new JTextArea(String.join("\n", scoreLst));
				scoreTxt.setEditable(false);
				JScrollPane scoreScroll = new JScrollPane(scoreTxt);
				scoreScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
				
				scorePnl.setBorder(new EtchedBorder());
				scorePnl.add(scoreScroll);
				scoreWindow.add(scorePnl);
				scoreWindow.pack();
				scoreWindow.setVisible(true);		
			}
		});
		//--Exit destroys window
		MenuItem exit = new MenuItem("Exit");
		exit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed (ActionEvent event)
			{
				guiFrame.dispose();
			}
		});
		//--Add menubar to window
		menu.add(newGame);
		menu.add(viewScores);
		menu.add(exit);
		menubar.add(menu);
		
		// Add panels to window
		guiFrame.add(InpPnl, BorderLayout.PAGE_END); 
		guiFrame.add(GPnl, BorderLayout.CENTER);
		guiFrame.add(WrdPnl, BorderLayout.PAGE_START);
		guiFrame.setMenuBar(menubar);
		guiFrame.setVisible(true);
	}

}
