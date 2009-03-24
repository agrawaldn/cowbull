/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package util;

import java.util.ArrayList;
import java.util.List;


public class Guess {
	private int[] guess;
	private int bulls;
	private int cows;
	
	public Guess(){
		guess = new int[Util.NUMLENGTH];
	}
	public Guess(int[] guess){
		this.guess = guess;
	}
	/**
	 * @param guess the guess to set
	 */
	public void setGuess(int[] guess) {
		this.guess = guess;
	}
	/**
	 * @return the guess
	 */
	public int[] getGuess() {
		return guess;
	}
	public int getNumber(){
		return guess[0]*1000+guess[1]*100+guess[2]*10+guess[3];
	}
	/**
	 * @param bulls the bulls to set
	 */
	public void setBulls(int bulls) {
		this.bulls = bulls;
	}
	/**
	 * @return the bulls
	 */
	public int getBulls() {
		return bulls;
	}
	/**
	 * @param cows the cows to set
	 */
	public void setCows(int cows) {
		this.cows = cows;
	}
	/**
	 * @return the cows
	 */
	public int getCows() {
		return cows;
	}
	public int getCowsAndBulls(){
		return getCows()+getBulls();
	}
	public int getPosition(int number){
		int position = -1;
		for(int i=0;i<guess.length;i++){
			if(guess[i]==number){
				position = i;
			}
		}
		return position;
	}
	public List<Digit> getDigitList(){
		List<Digit> digitList = new ArrayList<Digit>();
		for(int i=0;i<guess.length;i++){
			Digit d = new Digit();
			d.setDigit(guess[i]);
			int overAllChance = getCowsAndBulls()*25;
			d.setOverAllChance(overAllChance);
			int bulls = getBulls();
			if(bulls == 0){
				d.setChance(i, 0);
			}else{
				d.setChance(i, (int)overAllChance*bulls/getCowsAndBulls());
			}
			digitList.add(d);
		}
		return digitList;
	}
	public void insertDigit(int position, int digit){
		guess[position] = digit;
	}
	public void insertDigit(int digit){
		
	}
}
