import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;

public class MssCliente extends JFrame {
    private final JTextArea chatArea;
    private final JTextField messageField;
    private final JButton sendButton;
    
    // Componentes de red
    private Socket socket;
    private PrintWriter salida;
    private BufferedReader entrada;
    
    public MssCliente(String host, int port) {
        setTitle("Chat - Cliente ");
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
        
        // Conectar al servidor
        try {
            socket = new Socket(host, port);
            salida = new PrintWriter(socket.getOutputStream(), true);
            entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            chatArea.append("Conectado al servidor: " + host + " en el puerto " + port + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error conectando al servidor: " + ex.getMessage());
            System.exit(1);
        }
        
        // Hilo para leer mensajes del servidor
        new Thread(() -> {
            try {
                String respuesta;
                while ((respuesta = entrada.readLine()) != null) {
                    
                    String respuestaDesencriptada = Encriptacion.desencriptarServidor(respuesta);
                    SwingUtilities.invokeLater(() -> chatArea.append("Servidor (desencriptado): " + respuestaDesencriptada + "\n"));
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> chatArea.append("Error al leer mensajes del servidor: " + e.getMessage() + "\n"));
            }
        }).start();
        
        // Eventos para enviar mensajes
        sendButton.addActionListener((ActionEvent e) -> Enviar());
        messageField.addActionListener((ActionEvent e) -> Enviar());
    }
    
    private void Enviar() {
        String mensaje = messageField.getText().trim();
        if (!mensaje.isEmpty()) {
           
            String mensajeEncriptado = Encriptacion.encriptarCliente(mensaje, 3); 
            
            
            chatArea.append("Cliente: " + mensaje + "\n");
            chatArea.append("Cliente (encriptado): " + mensajeEncriptado + "\n");
          
            System.out.println("Mensaje encriptado (Cliente -> Servidor): " + mensajeEncriptado);
            salida.println(mensajeEncriptado);
            messageField.setText("");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MssCliente cliente = new MssCliente("localhost", 12345);
            cliente.setVisible(true);
        });
    }
}
