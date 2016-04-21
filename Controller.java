/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classisgames;

/**
 *
 * @author Charles
 */
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Runnable {

    private boolean running = true;
    private Thread _sender = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (running) {
                    while (_outMsgs.size() > 0) {
                        byte[] bytes = _outMsgs.get(_outMsgs.size() - 1).getBytes(StandardCharsets.UTF_8);
                        _out.write(bytes);
                    }
                    Thread.sleep(500);
                }
            } catch (IOException ex) {
                plOut("Error during write");//Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                plOut("Sender sleep interrupted");//Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    private Thread _reciever = new Thread(new Runnable() {
        @Override
        public void run() {
            byte[] bytes = new byte[256];
            String msg = "";
            try {
                while (running) {
                    while (_in != null && _in.available() > 0) {
                        _in.read(bytes);
                        msg += new String(bytes, StandardCharsets.UTF_8);
                    }
                    if (!"".equals(msg)) {
                        _gui.messageView.append("\n" + msg);
                    }

                    Thread.sleep(500);
                }
            } catch (IOException ex) {
                //ShowMessage("not connected to opponent")Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
    private Socket _sock;
    private DatagramSocket _dSock;
    private ByteArrayOutputStream _out;
    private ByteArrayInputStream _in;
    private Dictionary<String, Player> _players = new Hashtable();
    private Dictionary<String, String> _ports = new Hashtable();
    public ArrayList<String> _outMsgs = new ArrayList<String>();
    private CGGui _gui;

    public Controller(CGGui gui) throws SocketException {
        _gui = gui;
        _dSock = new DatagramSocket(7350);
    }

    public void connectToOpponent(String opponent) throws IOException {
        if (_players.get(opponent) != null) {
            _sock = _players.get(opponent).getSock();
        }
    }

    public void addOpponent(String opponent, String ipString, String port) throws UnknownHostException {
        try {
            Socket s = new Socket(InetAddress.getByName(ipString), Integer.parseInt(_ports.get(opponent)));
        } catch (IOException ex) {
            //Show message "Can't connect to this opponent"
        }
        //_players.put(opponent, new Player());
        _ports.put(opponent, port);
    }

    public void SendMessage(String msg) {
        _outMsgs.add(msg);
    }

    private void plOut(String msg) {
        System.out.println(msg);
    }

    @Override
    public void run() {
        try {
            if (_sock.isConnected()) {
                _out = (ByteArrayOutputStream) _sock.getOutputStream();
                _in = (ByteArrayInputStream) _sock.getInputStream();
                while (running) {
                    byte[] bytes = new byte[256];
                    String inMsg = "";
                    try {
                        byte[] buf = new byte[1024];
                        DatagramPacket dp = new DatagramPacket(buf, 1024);
                        _dSock.receive(dp);
                        _sock.setSoTimeout(0);
                        while (_in != null && _in.available() > 0) {
                            _in.read(bytes);
                            inMsg += new String(bytes, StandardCharsets.UTF_8);
                        }
                        String[] msgArray = inMsg.split(":");
                        switch (msgArray[0]) {
                            case "hangman":
                                //add hangman logic
                                break;
                            case "tic":
                                // do tic tac toe logic
                                break;
                            case "msg":
                                if (msgArray.length > 1) {
                                    _gui.messageView.append(msgArray[1]);
                                }
                                break;
                        }
                        _sock.setSoTimeout(250);
                    } catch (SocketTimeoutException ste) {
                    }// do nothing
                    if (!_outMsgs.isEmpty()) {
                        for (String s : _outMsgs) {
                            _out.write(s.getBytes(StandardCharsets.UTF_8));
                        }
                        for (int i = _outMsgs.size() - 1; i < 0; i--) {
                            _outMsgs.remove(i);
                        }
                    }
                    Thread.sleep(100);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
