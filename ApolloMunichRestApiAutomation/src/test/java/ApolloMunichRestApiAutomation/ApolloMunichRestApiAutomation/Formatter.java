package ApolloMunichRestApiAutomation.ApolloMunichRestApiAutomation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Formatter {

	public static void write(File directory,String statements) {
		 try { 
			 	File file = new File(directory.getPath()+"/output.csv");
			  	BufferedWriter out = new BufferedWriter(new FileWriter(file, true)); 
			  	out.write(statements);
			  	out.close(); 
	        } 
	        catch (IOException e) { 
	            System.out.println("exception occoured" + e); 
	        } 
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("/home/hi.agrawal");
		write(file,"\n"+"himanshu ");
		write(file,"is my name");
	}

}
