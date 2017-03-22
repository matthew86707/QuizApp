import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;

public class ClientMain {
	public static void main(String[] args) {
		try {
			//TCPConnection client = new TCPConnection("192.168.7.88", 1337);
			TCPConnection client = new TCPConnection("localhost", 1337);
			Scanner in = new Scanner(System.in);
			System.out.print("Enter your username: ");
			String username = in.nextLine();
			client.getOutputStream().writeObject(username + " connected.");
			while (!client.isClosed()) {
				int toRead = client.getInputStream().readInt();
				for (int i = 0; i < toRead; i++) {
					File temp = new File("tmp_" + i + ".png");
					temp.delete();
					temp.createNewFile();

					client.getInputStream().readFile(temp);
					Desktop.getDesktop().open(temp);
				}

				System.out.print("Enter your answers: ");
				String answers = in.nextLine();
				client.getOutputStream().writeObject(new DataPackage(username, answers));
			}
			in.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
