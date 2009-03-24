/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package play;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import util.Digit;
import util.Guess;
import util.Util;

public class IPlay {
	private BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
	Util util = new Util();
	private List<Guess> guesses = new ArrayList<Guess>();
	private List<Digit> sureList = new ArrayList<Digit>();
	private List<Digit> notList;
	private List<Digit> maybeList;
	
	public void play(){
		int[] firstGuess = util.thinkNumber();
		Guess guess = new Guess(firstGuess);
		getResponse(guess);
		System.out.println("adding "+guess.getNumber()+" to guess list");
		guesses.add(guess);
		//Guess nextGuess = new Guess();
		while (guess != null){
			guess = nextGuess(guess);
			if(guess != null){
				getResponse(guess);
			}
			//guess = nextGuess;
		}
	}
	private Guess nextGuess(Guess guess){
		Guess nextGuess = null;
		//check if all the digits are discovered
		if(guess.getCowsAndBulls() == 4){
			if(guess.getBulls() < 4){
				//positionCows(guess.getGuess());
			}
		}else{
			//make second guess exclusive of first
			if(guesses.size()== 1){
				nextGuess = new Guess(util.thinkNumber(util.getExcludedPick(guess.getGuess(),Util.allDigits)));
				System.out.println("before adding second number "+guesses.get(0).getNumber());
				guesses.add(nextGuess);
				System.out.println("first number now is "+guesses.get(0).getNumber()+" second number now is "+guesses.get(1).getNumber());
			}else{
				nextGuess = intelligentGuess();
			}
			
		}
		return nextGuess;
	}
	private void positionCows(int[] allDigits){
		Guess lastGuess = new Guess(allDigits);
		while(lastGuess.getBulls() != 4){
			
		}
	}
	private Guess intelligentGuess(){
		//System.out.println("first number "+guesses.get(0).getNumber()+" second number "+guesses.get(1).getNumber());
		Guess guess = new Guess();
		if(guesses.size() == 2){
			//third chance
			int cowsAndBullsSoFar =  guesses.get(0).getCowsAndBulls()+guesses.get(1).getCowsAndBulls();
			if(cowsAndBullsSoFar == 4){
				//we have all the digits from 1st 2 guesses. eliminate the remaining 2 digits.
				
				int[] excluded0 = util.getExcludedPick(guesses.get(0).getGuess(), Util.allDigits);
				int[] excluded = util.getExcludedPick(guesses.get(1).getGuess(), excluded0);
				notList = util.array2ListOfDigit(excluded);
				List<Digit> notList1 = util.array2ListOfDigit(excluded);
				maybeList = guesses.get(0).getDigitList();
				maybeList.addAll(guesses.get(1).getDigitList());
				List<Digit> excludeList = new ArrayList<Digit>();
				Digit d1 = util.getMaxChanceDigit(maybeList, excludeList);
				excludeList.add(d1);
				Digit d2 = util.getMaxChanceDigit(maybeList, excludeList);
				int d1position = d1.getMaxChancePosition(-1);
				int d2position = d2.getMaxChancePosition(d1position);
				for(int i=0;i<guess.getGuess().length;i++){
					if(i==d1position){
						guess.insertDigit(d1position, d1.getDigit());
					}else if (i==d2position){
						guess.insertDigit(d2position, d2.getDigit());
					}else{
						guess.insertDigit(i, notList1.remove(0).getDigit());
					}
					
				}
			}
			else if(cowsAndBullsSoFar == 2){
				int[] sure = util.getExcludedPick(guesses.get(1).getGuess(), 
						util.getExcludedPick(guesses.get(0).getGuess(), Util.allDigits));
				sureList = util.array2ListOfDigit(sure);
			}
		}
		return guess;
	}

	public void getResponse(Guess guess){
		String response = "";
		boolean correctFormat = false;
		System.out.print("My guess... "+guess.getNumber());
		while(!correctFormat){
			System.out.print(" ...Enter response like xBxC ");
			try {
				response = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try{
				if(response.length()!=4){
					throw new Exception();
				}
				if(response.charAt(1)!= 'B' && response.charAt(3)!= 'C'){
					throw new Exception();
				}
				int bulls = Integer.parseInt(""+response.charAt(0));
				int cows = Integer.parseInt(""+response.charAt(2));
				guess.setBulls(bulls);
				guess.setCows(cows);
				correctFormat = true;
			}catch(Exception e){
				System.out.println("incorrect response format. ");
			}
		}
	}
	public static void main(String[] args) {
		IPlay player = new IPlay();
		player.play();
	}

}
