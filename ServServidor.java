import java.io.*;
import java.net.*;

public class ServServidor {
    public static void main(String[] args) {
        int puerto = 12345;

        try (ServerSocket servidor = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones en el puerto " + puerto);

            try (Socket socket = servidor.accept();
                 BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader consola = new BufferedReader(new InputStreamReader(System.in))) {

                String cliente = socket.getInetAddress().getHostName();
                System.out.println("\nCliente conectado desde: " + cliente + "\n");

                // Leer mensajes del cliente
                Thread leerCliente = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String mensaje;
                            while ((mensaje = entrada.readLine()) != null) {
                                // Desencriptar el mensaje
                                String mensajeDesencriptado = Encriptacion.desencriptarCliente(mensaje, 3);
                                System.out.println("\n" + cliente + " dice (desencriptado): " + mensajeDesencriptado + "\n");
                            }
                        } catch (IOException e) {
                        }
                    }
                });
                leerCliente.start(); 

                // Enviar mensajes al cliente
                String mensaje;
                while (true) {
                    System.out.print("Servidor: ");
                    mensaje = consola.readLine();  // 
                    if (mensaje.equalsIgnoreCase("salir")) {
                        salida.println("Conexion cerrada.");
                        break;
                    }
                    // Encriptar el mensaje antes de enviarlo 
                    String mensajeEncriptado = Encriptacion.encriptarServidor(mensaje);
                    System.out.println("Mensaje encriptado enviado: " + mensajeEncriptado);  
                    salida.println(mensajeEncriptado);  
                }

                System.out.println("\nConexion cerrada con " + cliente + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
