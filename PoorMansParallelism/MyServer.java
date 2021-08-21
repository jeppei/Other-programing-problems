package Jobs.PoorMansParallelism;

import java.net.*;
import java.io.*;

public class MyServer {
    public static void main( String[] args) {
	try {
            int portNumber = Integer.parseInt(args[0]);
	    ServerSocket sock = new ServerSocket(portNumber, 100);
            System.out.println("Server with portnumber " + portNumber + " started");
	    while (true) { new MyThread(sock.accept()).start(); }
	}
	catch(IOException e)  {System.err.println(e); }
    }
} 

class MyThread extends Thread {
    static int noThreads=0;
    BufferedReader inToServer;
    PrintWriter outToClient;
    public MyThread(Socket socket){
	try {
	    inToServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    outToClient = new PrintWriter(socket.getOutputStream());
	}
	catch(IOException e) {System.err.println(e);}
    }
    
    @Override
    public void run() {
        
	try {
                
            // Receiving and storing output
	    String[] input=inToServer.readLine().split(" ");
            
            // The boundaries of the real axis and imaginary axis
            double minReC = Double.parseDouble(input[0]); 
            double maxReC = Double.parseDouble(input[1]); 
            double minImC = Double.parseDouble(input[2]); 
            double maxImC = Double.parseDouble(input[3]); 
            
            // Maximum number of iterations
            int maxNoIterations = Integer.parseInt(input[4]);   
            
            // Width, height and number of parts to divide the output image into
            int width = Integer.parseInt(input[5]);   
            int height = Integer.parseInt(input[6]);   
            
            // Number of current server and the total number of servers
            int server = Integer.parseInt(input[7]);   
            int noServers = Integer.parseInt(input[8]);   
            
            // width of current servers image
            int widthPerServer = width/noServers;
            
            // Printing values.
	    System.out.println("Number of threads: " + (++noThreads));
            System.out.println("\n\tINPUT VALUES");
            System.out.println("Real part:      " + minReC + " <= Re(z) <= " + maxReC);
            System.out.println("Imaginary part: " + minImC + " <= Im(z) <= " + maxImC);
            System.out.println("Size of output image: " + width + "x" + height);
            System.out.println("Maximum number of iterations:" + maxNoIterations);
            System.out.println("This is server number: " + server + "/" + noServers);
            
            // Number of steps to take along the real-axis (x-axis) and 
            // the imaginary-axis (y-axis) axis if only one server is used.
            int noXIncrement = width-1;
            int noYIncrement = height-1;

            // Steps along the real and the imaginary axis if only one server 
            // is used.
            double dx = (maxReC-minReC)/(noXIncrement);
            double dy = (maxImC-minImC)/(noYIncrement);
            
            // 2D array to store all pixel values
            int[][] pixelColors = new int[widthPerServer][height];
            
            // Start value of the real and imaginary part of z and how much to
            // increase the x-value
            double imStartValue = minImC;
            double reStartValue = minReC + server*dx;
            double dxServer = dx*noServers;
            
            // Start value of c
            ComplexNumber c = new ComplexNumber(reStartValue, imStartValue);

            // Calculate all pixel values
            System.out.println("Calculating pixel colors... ");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < widthPerServer; x++) {
                    outToClient.println(mandelbrot(c, maxNoIterations)%256);
                    outToClient.flush();
                    c.add(dxServer, 0);         // Increase the real part
                }
                c.add(0, dy);           // Increase the imaginary part
                c.setRe(reStartValue);  // Reset the real part
                
                // Print the progress
                if (y%(height*0.1)==0) { System.out.println(y + "/" + height); }
            }
            System.out.println(" ...finished!");
            
	    noThreads--;
	}
        catch(IOException | NumberFormatException e) { System.err.println(e); }
    }
    
    private int mandelbrot(ComplexNumber c, int noIterations) { 
        // Returns the number of iterations until z > 2 in the equation 
        // z_n+1= (z_n)^2 + c
        int iteration;
        ComplexNumber z = new ComplexNumber(0,0);       // z = 0
        
        for (iteration = 1; iteration <= noIterations; iteration++) {
            z.squareMeAndAdd(c);
            if (z.abs()>2) { break; }
        }
        return iteration;
    }
}
