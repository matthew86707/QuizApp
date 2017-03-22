import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFileChooser;

import org.jnetwork.ClientData;
import org.jnetwork.DataPackage;
import org.jnetwork.TCPConnection;
import org.jnetwork.TCPConnectionCallback;
import org.jnetwork.TCPServer;

public class ServerMain {
	private static TCPServer server;
	private static final Object LOCK = new Object();
	private static ArrayList<File> toSend = new ArrayList<>();

	public static void main(String[] args) {
		server = new TCPServer(1337, new TCPConnectionCallback() {
			@Override
			public void clientConnected(ClientData event) {
				TCPConnection client = (TCPConnection) event.getConnection();
				try {
					System.out.println(client.readObject());
					while (!event.getConnection().isClosed()) {
						synchronized (LOCK) {
							LOCK.wait();
						}
						client.getOutputStream().writeInt(toSend.size());
						for (File file : toSend) {
							client.getOutputStream().writeFile(file);
						}
						DataPackage back = (DataPackage) client.getInputStream().readObject();
						System.out.println(back.getObjects()[0] + ": " + back.getObjects()[1]);
					}
				} catch (EOFException | SocketException e) {
					return;
				} catch (InterruptedException | IOException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		try {
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					JFileChooser chooser = new JFileChooser();
					chooser.setMultiSelectionEnabled(true);

					Scanner in = new Scanner(System.in);
					while (!Thread.currentThread().isInterrupted()) {
						String line = in.nextLine();
						if (line.equalsIgnoreCase("image")) {
							if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
								toSend.clear();
								for (File file : chooser.getSelectedFiles()) {
									toSend.add(file);
								}
								synchronized (LOCK) {
									LOCK.notifyAll();
								}
							}
						}
					}
					in.close();
				}
			});
			thread.start();
			server.start();
			server.waitUntilClose();
			server.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
