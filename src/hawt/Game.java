package hawt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
	// Dictionary and score filepaths
	final static String DICT_PATH = "./src/hawt/customdictionary.txt";
	final static File DICT_FILE = new File(DICT_PATH);
	final static String SCORE_PATH = "./src/hawt/scores.txt";
	
	// Initialize variables
	public ArrayList<Character> guessed_letters = new ArrayList<>();
	public int number_of_misses = 0;
	public int game_state = 0;
	public int won = 0;
	public int lost = 0;
	private String correct = new String();
	private String hidden = new String();
	Scanner input = new Scanner(System.in);
	
	// Constructor
	Game() throws FileNotFoundException{
		this.correct = get_word(DICT_FILE);
		this.hidden = hide_word(this.correct);
		toggle_state();
	}
	
	// Turn game on (1) or off (0)
	private void toggle_state() {
		if (this.game_state == 0) {
			this.game_state = 1;
		} else {
			this.game_state = 0;
		}
		if(this.game_state == 0) {
			if (this.number_of_misses == 6) {
				this.lost = 1;
			} else {
				this.won = 1;
			}
		}
	}
		
	// Choose a word from the dictionary
	private String get_word(File file) throws FileNotFoundException {
		ArrayList<String> dict = new ArrayList<String>();
		
		Scanner sc = new Scanner(file);
		while(sc.hasNext()) {
			dict.add(sc.nextLine());
		}
		sc.close();
		int dict_size = dict.size();
		final int RANDOM_INT = ThreadLocalRandom.current().nextInt(dict_size);
		String word = dict.get(RANDOM_INT);
		return word;
		}
	
	// Turn word into blanks
	private String hide_word(String word) {
		char[] char_list = word.toCharArray();
		int char_list_length = char_list.length;
		for(int i=0; i<char_list_length; i++) {
			char_list[i] = '_';
		};
		String hidden = new String(char_list);
		return hidden;
	}
	
	// Update blanks upon guessed letter
	private void update_hidden(char guess) {		
		char[] hidden_list = this.hidden.toCharArray();
		char[] correct_list = this.correct.toCharArray();
		
		int i = 0;
		for(char c: correct_list){
			if(guess == c) {
				hidden_list[i] = c;
			}
			i++;
		}
		String new_hidden = new String(hidden_list);
		if(new_hidden.equals(this.hidden)) {
			this.number_of_misses++;
			if (this.number_of_misses == 6) {
				toggle_state();
			}
		}
		if(new_hidden.equals(this.correct)) {
			toggle_state();
		}
		this.hidden = new_hidden;
	}
	
	// Get user input guess
	public void get_guess(char[] raw_guess) 
	{
		char guess;
		
		if (raw_guess.length == 1 && Character.isLetter(raw_guess[0])) 
		{
			guess = Character.toLowerCase(raw_guess[0]);
			this.guessed_letters.add(guess);
			update_hidden(guess);
		}
	}
	
	// Hidden word getter
	public String return_hidden() {
		return this.hidden;
	}
	
	// Correct answer getter
	public String return_correct() {
		return this.correct;
	}
	
	// Append score to scores.txt
	public void record_score() {
		try(FileWriter fw = new FileWriter(SCORE_PATH, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw))
		{
			Date today = new Date();
			DateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
			String str_today = date_format.format(today);
			String score = String.format("%s %s - %s", str_today, this.correct, this.number_of_misses);
			out.println(score);
			} catch (IOException e) {
				System.out.println("Error!");
			}
	}

}
