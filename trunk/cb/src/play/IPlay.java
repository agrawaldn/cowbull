/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package play;

import java.util.ArrayList;
import java.util.List;

import util.Guess;
import util.Util;

public class IPlay {
	Util util = new Util();
	List<Guess> guesses = new ArrayList<Guess>();
	public void play(){
		int[] trial = util.thinkNumber();
		Guess guess = new Guess(trial);
		
		guesses.add(guess);
	}
	public void getResponse(int[] trial){
		for(int i=0;i<trial.length;i++){
			System.out.print(trial[i]);
		}
	}
	public static void main(String[] args) {
		IPlay player = new IPlay();
		player.play();
	}

}
