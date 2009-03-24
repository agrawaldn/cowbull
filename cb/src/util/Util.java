/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Util {
	public static final int NUMLENGTH = 4;
	int[] num = new int[NUMLENGTH];
	public static final int[] allDigits = {0,1,2,3,4,5,6,7,8,9};
	
	public int[] thinkNumber(){
		return thinkNumber(allDigits);
	}
	public int[] thinkNumber(int[] pickfrom){
		num[0] = randomFirstDigit(pickfrom);
		for(int i=1;i<NUMLENGTH;i++){
			int c = randomDigit(pickfrom);
			while(contains(num, c) >= 0){
				c = randomDigit(pickfrom);
			}
			num[i] = c;
		}
		return num;
	}
	private int randomDigit(int[] pickfrom){
		Random generator = new Random();
		int idx = generator.nextInt(pickfrom.length);
		int dig = pickfrom[idx];
		return dig;
	}

	private int randomFirstDigit(int[] pickfrom){
		int ran = randomDigit(pickfrom);
		while(ran == 0){
			ran = randomDigit(pickfrom);
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
	public int[] getExcludedPick(int[] exclude, int[] excludeFrom){
		//System.out.println("exclude.length "+exclude.length+" excludeFrom.length "+excludeFrom.length);
		int[] excludedPick = new int[excludeFrom.length-exclude.length];
		int idx = 0;
		for(int i=0;i<excludeFrom.length;i++){
			//System.out.println("Checking for "+excludeFrom[i]);
			boolean exists = false;
			for(int j=0;j<exclude.length;j++){
				if(excludeFrom[i] == exclude[j]){
					exists = true;
				}
				//System.out.println(""+excludeFrom[i]+" == "+exclude[j]+" "+exists);
			}
			if(!exists){
				//System.out.println("adding "+excludeFrom[i]+" to position "+idx);
				excludedPick[idx] = excludeFrom[i];
				idx++;
			}
		}
		return excludedPick;
	}
	public List<Digit> array2ListOfDigit(int[] intarr){
		List<Digit> digitList = new ArrayList<Digit>();
		for(int i=0;i<intarr.length;i++){
			Digit d = new Digit();
			d.setDigit(intarr[i]);
			digitList.add(d);
		}
		return digitList;
	}
	public Digit getMaxChanceDigit(List<Digit> digitList, List<Digit> exclude){
		int maxChance = 0;
		Digit maxChanceDigit = null;
		for(Digit digit : digitList){
			if(!exclude.contains(digit)){
				if (digit.getOverAllChance() > maxChance){
					maxChance = digit.getOverAllChance();
					maxChanceDigit = digit;
				}
			}
		}
		return maxChanceDigit;
	}
}
