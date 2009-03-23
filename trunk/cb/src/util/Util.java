/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package util;

import java.util.Random;


public class Util {
	public static final int NUMLENGTH = 4;
	int[] num = new int[NUMLENGTH];
	
	public int[] thinkNumber(){
		num[0] = randomFirstDigit();
		for(int i=1;i<NUMLENGTH;i++){
			int c = randomDigit();
			while(contains(num, c) >= 0){
				c = randomDigit();
			}
			num[i] = c;
		}
		return num;
	}
	private int randomDigit(){
		Random generator = new Random();
		int dig = generator.nextInt(10);
		return dig;
	}
	private int randomFirstDigit(){
		int ran = randomDigit();
		while(ran == 0){
			ran = randomDigit();
		}
		return ran;
	}
	public int contains(int[] num, int digit){
		int position = -1;
		for(int i=0;i<num.length;i++){
			if(num[i]== digit){
				position = i;
			}
		}
		return position;
	}
	public String cowbull(int[] num, String guess ){
		int bull = 0;
		int cow = 0;
		for(int i=0;i<guess.length();i++){
			int dig = Integer.parseInt(""+guess.charAt(i));
			int position = contains(num,dig);
			if(position == i){
				bull++;
			}else if(position >=0){
				cow++;
			}
		}
		return ""+bull+"B"+cow+"C";
	}
}
