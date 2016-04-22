/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classisgames;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author cedriver
 */
public class Controller implements Runnable {

    private CGGui _gui;
    private ArrayList<String> outMsgs = new ArrayList<String>();
    private ArrayList<Player> players = new ArrayList<>();
    private Player cPlayer;

    public Controller(CGGui gui) throws UnknownHostException {
        _gui = gui;
        players.add(new Player("Myself", InetAddress.getByName("127.0.0.1"), 3500));
        cPlayer = players.get(0);
    }

    public void addOpponent(String name, String inetAddress, String port, int result) {
        InetAddress ip;
        try {
            ip = InetAddress.getByName(inetAddress);
            int p = Integer.parseInt(port);
            players.add(new Player(name, ip, p));
            if(result == JOptionPane.YES_OPTION)
                cPlayer = players.get(players.size()-1);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void choosePlayer(String name) {
        for (Player p : players) {
            if (p.getName() == name) {
                cPlayer = p;
                break;
            }
        }
    }

    public void processMessage(String msg) {
        String[] msgArry = msg.split(":");
        switch (msgArry[0]) {
            case "message":
                _gui.msgViewTA.append("\n" + msg);
                break;
            case "game":
                updateHangman(msgArry);
                break;
            case "tic":
                upDateTic(msgArry);
                break;
            default:
                _gui.msgViewTA.append("\nUnknown Message Type");
                break;
        }

    }

    public void sendMessage(String text) {
        outMsgs.add(text);
    }

    private void plOut(String info) {
        System.out.println(info);
    }

    @Override
    public void run() {
        byte[] inBytes = new byte[256];
        String inMsg;
        try {
            DatagramSocket socket = new DatagramSocket(3500);
            int sent = 0;
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(inBytes, inBytes.length);
                    socket.setSoTimeout(100);
                    socket.receive(packet);
                    processMessage(new String(packet.getData()));
                    plOut(new String(packet.getData()));
                } catch (SocketTimeoutException ste) {// do nothing
                }

                DatagramSocket ds;
                ds = new DatagramSocket();

                //try {
                if (sent < outMsgs.size()) {
                    String msg = outMsgs.get(outMsgs.size() - 1);
                    byte[] outBytes = msg.getBytes(StandardCharsets.UTF_8);
                    DatagramPacket dp = new DatagramPacket(outBytes, outBytes.length, cPlayer.getInetAddress(), cPlayer.getPort());
                    ds.send(dp);
                    plOut("Sent '" + msg + "'");
                    sent++;
                }
                //Thread.sleep(100);
                //} //catch (InterruptedException ex) {
//                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                }
//                outMsgs.add(count + " message/s have been sent");
//                count++;
                Thread.sleep(250);
            }
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void updateHangman(String[] msgArry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void upDateTic(String[] msgArry) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
