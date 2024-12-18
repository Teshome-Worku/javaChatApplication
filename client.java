package clientside;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.Border;

import java.net.*;

public class client implements ActionListener {
    static JFrame f = new JFrame();
    JTextField text;
    JPanel area;
    static DataOutputStream dout;
    static JScrollPane pane;
    static Box vertical = Box.createVerticalBox(); // Create a vertical box to hold messages

    client() {
        f.setTitle("Chat Application");

        // Top Panel
        JPanel p = new JPanel();
        p.setBackground(new Color(7, 94, 84));
        p.setBounds(0, 0, 450, 70);
        p.setLayout(null);
        f.add(p);

        JLabel title = new JLabel("Client");
        title.setBounds(170, 20, 200, 35);
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        p.add(title);

        // Chat Area
        area = new JPanel();
        //area.setBounds(5, 75, 425, 540);
        area.setBackground(Color.WHITE);
        //area.setLayout(new BorderLayout());
        area.setLayout(new BoxLayout(area, BoxLayout.Y_AXIS));
        pane=new JScrollPane(area);
        pane.setBounds(5,75,425,540);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        //f.add(area);
        f.add(pane);

        // Input Field
        text = new JTextField();
        text.setBounds(5, 620, 310, 30);
        text.setForeground(Color.BLACK);
        text.setFont(new Font("Arial", Font.BOLD, 20));
        f.add(text);

        // Send Button
        JButton button = new JButton("Send");
        button.setBounds(320, 620, 108, 30);
        button.setFont(new Font("Arial", Font.BOLD, 15));
        button.setForeground(Color.RED);
        button.setBorder(null);
        button.setBackground(new Color(7, 94, 84));
        button.addActionListener(this);
        f.add(button);

        // Frame Settings
        f.setUndecorated(false);
        f.setLayout(null);
        f.setSize(450, 700);
        f.setLocation(800, 40);
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String out = text.getText().trim(); // Get the input text and trim whitespace

        // Prevent sending empty messages
        if (!out.isEmpty()) {
            JPanel p2 = formatLabel(out);

            JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Align right
            rightPanel.setBackground(Color.WHITE);
            rightPanel.add(p2);
            JPanel pan=new JPanel(new BorderLayout());
            pan.setBackground(Color.WHITE);
            //pan.add(p2,BorderLayout.PAGE_START);
            area.add(pan,BorderLayout.EAST);
            

            vertical.add(rightPanel);
            vertical.add(Box.createVerticalStrut(10)); // Add spacing between messages

            area.add(vertical, BorderLayout.PAGE_START);//PAGE_START
            f.revalidate();
            f.repaint();

            try {
                dout.writeUTF(out); // Send message to server
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            text.setText(""); // Clear input field
        }
    }

    public static JPanel formatLabel(String out) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel output = new JLabel("<html><p style='width: 150px; border-radius :20px;'>" + out + "</p></html>");
        output.setFont(new Font("Tahoma", Font.BOLD, 16));
        output.setBackground(new Color(180, 220, 180)); // Light green background for messages
        output.setOpaque(true);
        output.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Padding for the label
        panel.add(output);

        return panel;
    }
    //main method

    public static void main(String[] args) {
        new client();
        String ip = "Localhost"; // 127.0.0.1
        int port = 51888;

        try {
            try (Socket socket = new Socket(ip, port)) {
				DataInputStream din = new DataInputStream(socket.getInputStream());
				dout = new DataOutputStream(socket.getOutputStream());

				while (true) {
				    String msg = din.readUTF();

				    JPanel panel = formatLabel(msg);

				    JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // Align left
				    leftPanel.setBackground(Color.WHITE);
				    leftPanel.add(panel);

				    vertical.add(leftPanel);
				    vertical.add(Box.createVerticalStrut(5)); // Add spacing

				    f.validate();
				    pane.getVerticalScrollBar().setValue(pane.getVerticalScrollBar().getMaximum());
				}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
