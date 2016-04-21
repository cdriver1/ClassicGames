/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagram;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author cedriver
 */
public class Controller implements Runnable {

    private CGGui _gui;
    private ArrayList<String> outMsgs = new ArrayList<String>();

    public Controller(CGGui gui) {
        _gui = gui;
    }

    public void processMessage(String msg) {
        _gui.msgViewTA.append("\n" + msg);
    }

    @Override
    public void run() {
        //<editor-fold defaultstate="collapsed" desc=" ${Commentout stuff} ">
//        Thread sender = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DatagramSocket ds;
//                    ds = new DatagramSocket();
//                    int sent = 0;
//                    while (true) {
//                        try {
//                            if (sent < outMsgs.size()) {
//                                String msg = outMsgs.get(outMsgs.size() - 1);
//                                byte[] outBytes = msg.getBytes(StandardCharsets.UTF_8);
//                                DatagramPacket dp = new DatagramPacket(outBytes, outBytes.length,
//                                        InetAddress.getByName("127.0.0.1"), 3500);
//                                ds.send(dp);
//                                plOut("Sent dp");
//                                sent++;
//                            }
//                            Thread.sleep(100);
//                        } catch (InterruptedException ex) {
//                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                        } catch (IOException ex) {
//                            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                        }
//                    }
//                } catch (SocketException ex) {
//                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        Thread rcvr = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    byte[] inBytes = new byte[512];
//                    DatagramSocket socket = new DatagramSocket(3500);
//                    while (true) {
//                        try {
//                            DatagramPacket packet = new DatagramPacket(inBytes, inBytes.length);
//                            socket.setSoTimeout(100);
//                            socket.receive(packet);
//                            processMessage(new String(packet.getData()));
//                            plOut(new String(packet.getData()));
//                        } catch (SocketTimeoutException ste) {
//                            try {
//                                Thread.sleep(50);
//                            } catch (InterruptedException ex) {
//                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                            }
//                        }
//                    }
//                } catch (SocketException ex) {
//                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//        rcvr.start();
//        sender.start();
//        
//        while(true){
//            try {
//                Thread.sleep(1500);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        //</editor-fold>
        byte[] inBytes = new byte[256];
        String inMsg;
        try {
            DatagramSocket socket = new DatagramSocket(3500);

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
                int sent = 0;
                //try {
                    if (sent < outMsgs.size()) {
                        String msg = outMsgs.get(outMsgs.size() - 1);
                        byte[] outBytes = msg.getBytes(StandardCharsets.UTF_8);
                        DatagramPacket dp = new DatagramPacket(outBytes, outBytes.length, InetAddress.getByName("127.0.0.1"), 3500);
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
                Thread.sleep(1500);
            }
        } catch (SocketException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMessage(String text) {
        outMsgs.add(text);
    }

    private void plOut(String info) {
        System.out.println(info);
    }
}
