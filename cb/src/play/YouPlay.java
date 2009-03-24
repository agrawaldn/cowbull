/**
 * Created by: dagrawal on Mar 23, 2009
 * Contact: dev.agrawal@gmail.com for help
 */
package play;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import util.Util;

public class YouPlay {
	private BufferedReader br= new BufferedReader(new InputStreamReader(System.in));
	private Util tester = new Util();
	private int[] number = tester.thinkNumber();
	
	private String readInput(){
		String input = "";
		System.out.print("Take your guess...");
		try {
			input = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public void play(){
		String input = "";
		int trial = 0;
		boolean quit = false;
		while(!quit){
			input = readInput();
			quit = input.equalsIgnoreCase("q");
			String output = "";
			try{
				if((Integer.parseInt(input) > 1000) && (Integer.parseInt(input) < 9999)){
					output = tester.cowbull(number,input );
					System.out.println("                  "+output);
					trial++;
				}else{
					throw new Exception();
				}
			}catch(Exception e){
				if(!quit){
					System.err.println("Invalid number. Enter 4 non repeating digits... ");	
				}
			}
			if(output.equals("4B0C")){
				System.out.println("Congratulations!!! You solved it in "+trial+" trials");
				break;
			}
		}
		if(quit){
			printAnswer();
		}

	}
	private void printAnswer(){
		System.out.print("The number is... ");
		for(int i=0;i<number.length;i++){
			System.out.print(number[i]);
		}
	}
	public static void main(String[] args) {
		YouPlay player = new YouPlay();
		//test.printAnswer();
		player.play();
	}

}
