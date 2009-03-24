/**
 * Created by: dagrawal on Mar 24, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package util;


public class Digit {
	private int digit;
	private int[] chance = new int[4];
	private int overAllChance;
	/**
	 * @param digit the digit to set
	 */
	public void setDigit(int digit) {
		this.digit = digit;
	}
	/**
	 * @return the digit
	 */
	public int getDigit() {
		return digit;
	}
	/**
	 * @param chance0 the chance0 to set
	 */
	public void setChance(int position, int chance) {
		this.chance[position] = chance;
	}
	/**
	 * @return the chance0
	 */
	public int getChance(int position) {
		return chance[position];
	}
	public int getMaxChancePosition(int exclude){
		int maxChance = 0;
		int maxChancePosition = 0;
		for(int i=0;i<chance.length;i++){
			if(i != exclude){
				if(chance[i]>= maxChance){
					maxChance = chance[i];
					maxChancePosition = i;
				}
			}
		}
		return maxChancePosition;
	}
	/**
	 * @param overAllChance the overAllChance to set
	 */
	public void setOverAllChance(int overAllChance) {
		this.overAllChance = overAllChance;
	}
	/**
	 * @return the overAllChance
	 */
	public int getOverAllChance() {
		return overAllChance;
	}
	public boolean equals(Object obj){
		try{
			if(((Digit)obj).digit == this.getDigit()){
				return true;
			}
		}catch(Exception e){
			return false;
		}
		return false;
	}
}
