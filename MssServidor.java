import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MssServidor extends JFrame {
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;
    
    // Componentes de red
    private ServerSocket serverSocket;
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    
    public MssServidor(int port) {
        setTitle("Chat - Servidor");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Área de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setBackground(new Color(240, 242, 245));
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(chatArea);
        
        // Campo de texto y botón enviar
        messageField = new JTextField();
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        sendButton = new JButton("Enviar");
        sendButton.setBackground(new Color(37, 211, 102));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        
        // Iniciar el servidor 
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(port);
                SwingUtilities.invokeLater(() -> chatArea.append("Esperando conexión en el puerto " + port + "...\n"));
                socket = serverSocket.accept();
                SwingUtilities.invokeLater(() -> chatArea.append("Cliente conectado desde: " + socket.getInetAddress() + "\n"));
                
                salida = new PrintWriter(socket.getOutputStream(), true);
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                // Hilo para leer mensajes del cliente
                new Thread(() -> {
                    try {
                        String mensaje;
                        while ((mensaje = entrada.readLine()) != null) {
                           
                            String mensajeDesencriptado = Encriptacion.desencriptarCliente(mensaje, 3);
                            SwingUtilities.invokeLater(() -> chatArea.append("Cliente (desencriptado): " + mensajeDesencriptado + "\n"));
                        }
                    } catch (IOException e) {
                        SwingUtilities.invokeLater(() -> chatArea.append("Error al leer mensaje del cliente: " + e.getMessage() + "\n"));
                    }
                }).start();
                   
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> chatArea.append("Error en el servidor: " + e.getMessage() + "\n"));
            }
        }).start();
        
      
        sendButton.addActionListener((ActionEvent e) -> Enviar());
        messageField.addActionListener((ActionEvent e) -> Enviar());
    }
    
    private void Enviar() {
        String mensaje = messageField.getText().trim();
        if (!mensaje.isEmpty()) {

            String mensajeEncriptado = Encriptacion.encriptarServidor(mensaje);
           
            chatArea.append("Servidor: " + mensaje + "\n");
            chatArea.append("Servidor(encriptado): " + mensajeEncriptado + "\n");
            salida.println(mensajeEncriptado);
            messageField.setText("");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MssServidor servidor = new MssServidor(12345);
            servidor.setVisible(true);
        });
    }
}
