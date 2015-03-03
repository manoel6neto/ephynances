package br.com.physisbrasil.web.ephynances.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.logging.Level;
//import java.util.logging.Logger;

/**
 *
 * @author Thomas
 */
public class Comunicacao {

    // ----------------------------- Properties ----------------------------------- //
    private Socket socket;
    private DataInputStream is;
    private DataOutputStream os;

    // ----------------------------- The End Properties ----------------------------------- //
    // ----------------------------- MAIN TESTES --------------------------------------- //
    /*public static void main(String[] args) throws IOException {

        try {

            //Conectando
            Comunicacao co = new Comunicacao("192.168.0.248", 7000);

            //Enviando comandos
            ArrayList<String> retorno = co.sendToken("restart");
            System.out.println(retorno.toString());

            //Recebendo a resposta
            retorno = co.receiveToken();
            System.out.println(retorno.toString());

            co.closeConnection();
        } catch (UnknownHostException ex) {

            Logger.getLogger(Comunicacao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }*/

    // ----------------------------- THE END MAIN TESTES --------------------------------------- //
    // ---------------------------------------- Constructors -------------------------------------- //
    public boolean Connect(String ip, int port) throws UnknownHostException {
        try {
            
            if (ip != null && port > 0) {
                InetAddress addr = InetAddress.getByName(ip);
                this.socket = new Socket(addr, port);
            }
            
            return this.socket.isConnected();
        } catch (IOException e) {

            this.socket = null;
            return false;
            //Add logClass
        }
    }

    // -------------------------------- The End Constructors ------------------------------- //
    // -------------------------------- Methods --------------------------------------------- //
    //Fecha a conexão
    public void closeConnection() throws IOException {

        try {

            this.socket.close();
        } catch (IOException e) {

            //Add logClass
        }
    }

    //Formata a mensagem para o padrão de comunicação
    private String getProtocolMessage(String token) {

        String message;
        int size;

        if (token != null && !token.isEmpty()) {

            size = token.length();
            message = "#" + size + "#" + token.trim();
        } else {

            return null;
        }

        return message;
    }

    //Emviar mensagem via socket
    public ArrayList<String> sendToken(String message) {

        try {

            if (getProtocolMessage(message) != null) {

                if (this.socket != null) {

                    //Enviando a mensagem no socket
                    os = new DataOutputStream(this.socket.getOutputStream());
                    os.writeBytes(getProtocolMessage(message));
                    os.flush();
                } else {
                    //Socket desconectado
                    return new ArrayList<String>(Arrays.asList("error", "Falha no envio dos dados. Objeto não está conectado."));
                }
            } else {

                //Nenhuma mensagem informada
                return new ArrayList<String>(Arrays.asList("error", "Nenhuma mensagem informada."));
            }
        } catch (IOException e) {

            //Erro desconhecido
            return new ArrayList<String>(Arrays.asList("error", "Erro desconhecido: " + e.getMessage() + "."));
        }

        //Enviado com sucesso
        return new ArrayList<String>(Arrays.asList("ok", "ok"));
    }

    //receber mensagem via socket
    public ArrayList<String> receiveToken() {

        String message = "";
        String size = "";
        String aux;

        try {

            if (this.socket != null) {

                if (this.socket.isConnected()) {

                    is = new DataInputStream(this.socket.getInputStream());

                    byte[] bytes = new byte[1];
                    is.read(bytes);
                    if (!"#".equals(new String(bytes))) {

                        //Operador de controle não encontrado
                        return new ArrayList<String>(Arrays.asList("error", "Falha na recepção dos dados. Operador de controle inicial não encontrado."));
                    } else {

                        bytes = new byte[1];
                        is.read(bytes);
                        aux = new String(bytes);
                        while (!"#".equals(aux)) {

                            //salvando o tamanho e lendo o buffer novamente
                            size = size + aux;
                            bytes = new byte[1];
                            is.read(bytes);
                            aux = new String(bytes);
                        }

                        //Lendo o array de bytes e convertendo para String
                        byte[] data = new byte[Integer.parseInt(size)];
                        is.read(data);
                        message = new String(data);
                    }
                }
            }
        } catch (IOException e) {

            //Erro desconhecido
            return new ArrayList<String>(Arrays.asList("error", "Erro desconhecido: " + e.getMessage() + "."));
        }

        //Recebido com sucesso
        return new ArrayList<String>(Arrays.asList("ok", message));
    }

    // -------------------------------- The End Methods --------------------------------------------- //    
}
