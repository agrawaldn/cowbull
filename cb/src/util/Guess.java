/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package util;


public class Guess {
	private int[] guess;
	private int bulls;
	private int cows;
	
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
}
