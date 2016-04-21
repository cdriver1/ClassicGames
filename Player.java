/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classisgames;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 *
 * @author cedriver
 */
public class Player {

    public String _name;
    private transient final Socket _sock;
    private transient final ObjectOutputStream _out;
    private transient final ObjectInputStream _in;

    public Player(Socket socket, String name) throws IOException {
        _sock = socket;
        _out = (ObjectOutputStream) _sock.getOutputStream();
        _in = (ObjectInputStream) _sock.getInputStream();
        _name = name;
    }

    public Player(Socket socket) throws IOException {
        _sock = socket;
        _out = (ObjectOutputStream) _sock.getOutputStream();
        _in = (ObjectInputStream) _sock.getInputStream();
    }

    public Socket getSock() {
        return _sock;
    }

    public void writeBytes(String msg) throws IOException {
        _out.write(msg.getBytes(StandardCharsets.UTF_8));
    }

    public String readUTF() throws IOException {
        return _in.readUTF();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        return _in.readObject();
    }

    public byte[] readBytes() throws IOException {
        byte[] bytes = new byte[1024];
        _in.read(bytes);
        return bytes;
    }

    public void writeObject(Object obj) throws IOException {
        _out.reset();
        _out.writeObject(obj);
        _out.flush();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return Objects.equals(this._name, other._name);
    }

    public void setName(String name) {
        _name = name;
    }

}
