/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datagramGames;

import java.net.InetAddress;

/**
 *
 * @author cedriver
 */
public class Player {
    
    private String _name;
    private InetAddress _inetAddress;
    private int _port;

    public Player(String name, InetAddress inetAddress, int port) {
        _name = name;
        _inetAddress = inetAddress;
        _port = port;
    }

    public String getName() {
        return _name;
    }

    public InetAddress getInetAddress() {
        return _inetAddress;
    }

    public int getPort() {
        return _port;
    }
    
    
}
