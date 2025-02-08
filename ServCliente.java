import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServCliente {
    public static void main(String[] args) {
        String servidor = "localhost";
        int puerto = 12345;

        try (Socket socket = new Socket(servidor, puerto);
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("\nConectado al servidor. Escribe un mensaje ('salir' para cerrar conexión):\n");

            // Leer mensajes del servidor
            Thread leerServidor = new Thread(() -> {
                try {
                    String respuesta;
                    while ((respuesta = entrada.readLine()) != null) {
                        // Desencriptar el mensaje 
                        String respuestaDesencriptada = Encriptacion.desencriptarServidor(respuesta);
                        System.out.println("\nServidor: " + respuestaDesencriptada + "\n");
                    }
                } catch (IOException e) {
                }
            });
            leerServidor.start(); 

            // Enviar mensajes al servidor
            String mensaje;
            while (true) {
                System.out.print("Cliente: ");
                mensaje = scanner.nextLine();  // 
                if (mensaje.equalsIgnoreCase("salir")) {
                    salida.println("Conexion cerrada.");
                    break;
                }
                // Encriptar el mensaje 
                String mensajeEncriptado = Encriptacion.encriptarCliente(mensaje, 3);
                System.out.println("Mensaje encriptado enviado: " + mensajeEncriptado);  
                salida.println(mensajeEncriptado);  
            }

            System.out.println("\nConexión cerrada con el servidor.\n");
        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
