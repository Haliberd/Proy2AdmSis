/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package empresacombustible;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Maximo Hernandez
 * Inspirado en el codigo perteneciente a https://cs.lmu.edu/~ray/notes/javanetexamples/. 
 */
public class Distribuidor {
    public static void main(String[] args) throws Exception {
        try (ServerSocket listener = new ServerSocket(59898)) {
            System.out.println("El servidor de servicio esta corriendo...");
            ExecutorService pool = Executors.newFixedThreadPool(6);
            ExecutorService pool2 = Executors.newFixedThreadPool(1);
            while (true) {
                pool.execute(new Capitalizer(listener.accept()));
                pool2.execute(new Interfaz());
            }
        }
    }

    private static class Capitalizer implements Runnable {
        private Socket socket;

        Capitalizer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            //System.out.println("Connected: " + socket);
            String bandera = "9";
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                while (bandera.compareTo("0") != 0) {
                    bandera = in.readUTF();
                    System.out.println(bandera);
                    out.writeUTF("yes");
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
                System.out.println(e.getMessage());
            } finally {
                try { socket.close();
            } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }
    }
    
    private static class Interfaz implements Runnable{

        private Socket socket;
        private Scanner scanner;
        
        Interfaz() throws IOException{
            this.scanner = new Scanner(System.in);
        }
        
        @Override
        public void run() {
            String bandera2 = "9";
            while (bandera2.compareTo("0") != 0){
                System.out.println("Menu de ");
                bandera2 = scanner.nextLine();
            }
        }
        
    }
}
