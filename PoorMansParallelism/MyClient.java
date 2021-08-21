
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

// @author Jesper Lundin

public class MyClient {

    public static void main(String[] args) throws InterruptedException { 
        new MyClient(args); 
    }
    
    MyClient(String[] input) throws InterruptedException {
        try {
            // Input values
            // The boundaries of the real axis and imaginary axis
            double minReC       = Double.parseDouble(input[0]); 
            double maxReC       = Double.parseDouble(input[1]); 
            double minImC       = Double.parseDouble(input[2]); 
            double maxImC       = Double.parseDouble(input[3]); 
            
            // Maximum number of iterations
            int maxNoIterations = Integer.parseInt(input[4]); 
            
            // Width, height and number of parts to divide the output image into
            int width           = Integer.parseInt(input[5]);
            int height          = Integer.parseInt(input[6]);
            int noXPictures     = Integer.parseInt(input[7]);
            int noYPictures     = Integer.parseInt(input[8]);
            
            // Number of servers, adress and portnumbers to each service
            int noServers = Integer.parseInt(input[9]);
            String[] servers = new String[noServers];
            int[]    portNumbers = new int[noServers];
            
            for (int server = 0; server < noServers; server++) {
                System.out.println(input[10+server]);
                servers[server] = input[10+server].split(":")[0];
                portNumbers[server] = Integer.parseInt(input[10+server].split(":")[1]);
            }
                             
            // Print input on screen
            System.out.println("\tINPUT VALUES\n" +
                "Real part:      " + minReC + " <= Re(z) <= " + maxReC + "\n" +
                "Imaginary part: " + minImC + " <= Im(z) <= " + maxImC + "\n" +
                "Maximum number of iterations: " + maxNoIterations + "\n" +
                "Image size: " + width + "x" + height + "\n" +
                "Image divided into: " + noXPictures + "x" + noYPictures + "\n"+
                "Servers:"
            );
            
            for (int server = 0; server < noServers; server++) {
                System.out.println(
                    "\t" + servers[server] + ":" + portNumbers[server]
                );
            }
            
            // Creating a connection to the server
            System.out.print("\nConnecting to servers and sending input...");
            Socket[]         sockets      = new Socket[noServers];
            BufferedReader[] inFromServer = new BufferedReader[noServers];
            PrintWriter[]    outToServer  = new PrintWriter[noServers];
            InputStreamReader[] inputStreams = new InputStreamReader[noServers];
            
            for (int server = 0; server < noServers; server++) {
                sockets[server] = new Socket(servers[server],portNumbers[server]);
                inputStreams[server] = new InputStreamReader(
                        sockets[server].getInputStream());
                inFromServer[server] = new BufferedReader(inputStreams[server]);
                outToServer[server]  = new PrintWriter(
                        sockets[server].getOutputStream());
                
                // Sending values to server
                outToServer[server].println(
                          minReC + " " + maxReC + " " + 
                          minImC + " " + maxImC + " " + 
                          maxNoIterations + " " + 
                          width + " " + height + " " + 
                          server + " " + noServers);
                outToServer[server].flush();
            }
            System.out.println( " ...finnished!");
            
            // 2D array to store all pixel values
            String[][] colors = new String[width][height];
            
            // Reading pixelvalues from server
            System.out.println("Reading input from server");
            System.out.println("Progress: 0% |        | 100%");
            System.out.print("             ");
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x+=noServers) {
                    for (int server = 0; server < noServers; server++) {
                        colors[x+server][y] = inFromServer[server].readLine();
                    }
                }
                // Print the progress
                if (y%(height*0.1)==0) { System.out.print("|"); }
            }
            System.out.println("\nAlla data recieved!");
            
            
            // Creating the image files
            int subWidth  = width/noXPictures;    // Width of imagefile
            int subHeight = height/noYPictures;   // Height of imagefile
            BufferedWriter outToFile;
            FileWriter fstream;
            for (int x = 0; x < noXPictures; x++) {
                for (int y = 0; y < noYPictures; y++) {
                    
                    // Creating and naming the file
                    fstream = new FileWriter("mandelbrot" + y + x + ".pgm");  
                    
                    // Sending input stream to buffered writer
                    outToFile = new BufferedWriter(fstream);  
                    
                    // Writing to file
                    // Magic number, comment/header, width, height, maxvalue-1
                    outToFile.write("P2\n"
                                  + "# Mandelbrot \n"
                                  + subWidth + " " + subHeight    
                                  + "\n255\n");
                    
                    // Writing each pixel value to the file
                    for(int j = y*subHeight ; j < (y+1)*subHeight;j++) {
                        for(int i = x*subWidth ; i < (x+1)*subWidth;i++) {
                            outToFile.write(colors[i][j]+" ");                  
                        }
                        outToFile.write("\n");
                    }
                    outToFile.close();    // Closing the BufferedWritter
                }  
            }
            System.out.println("All image(s) succesfully created!");
        }     
        // Catching different exceptions
        catch (IOException ex) { 
            Logger.getLogger(MyClient.class.getName()).log(Level.SEVERE, null, ex); }
    }
}
