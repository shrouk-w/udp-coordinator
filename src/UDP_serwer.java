import java.io.IOException;

import java.net.*;

import java.util.ArrayList;

public class UDP_serwer {

    int port;

    int num;

    DatagramSocket socket;

    ArrayList <Integer> nums;

    public UDP_serwer(int port, int num) {

        this.port = port;

        this.num = num;

        System.out.println("my num: "+num);

        try {

            try {

                socket = new DatagramSocket(port);

                nums = new ArrayList<>();

                run_as_master(socket);

            } catch (SocketException e) {

                socket = new DatagramSocket();

                run_as_slave(socket);

            }

        } catch (Exception a)

        {

            System.out.println("cos nie dziala");

            a.printStackTrace();

        }

    }

    private void run_as_slave(DatagramSocket socket){

        try {

            System.out.println("running as slave");

            byte[] tosend = String.valueOf(num).getBytes();

            DatagramPacket packet = new DatagramPacket(tosend, tosend.length, InetAddress.getByName("localhost"), port);

            boolean ack = false;

            socket.setSoTimeout(5000);

            while(!ack)

            {

                socket.send(packet);

                byte[] response = new byte[1024];

                DatagramPacket responsepacket = new DatagramPacket(response, response.length);

                socket.receive(responsepacket);

                String confirmmsg = new String(responsepacket.getData(), 0, responsepacket.getLength()).trim();

                System.out.println("msg received: "+confirmmsg);

                if("yey!".equals(confirmmsg))

                {

                    ack=true;

                    System.out.println("ready to die");

                }

            }

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void run_as_master(DatagramSocket socket){

        try {

            System.out.println("running as master");

            boolean run = true;

            nums.add(num);

            while (run) {

                byte[] data = new byte[1024];

                DatagramPacket datagramPacket = new DatagramPacket(data, data.length);

                socket.receive(datagramPacket);

                String recieveddata = new String(datagramPacket.getData(), 0, datagramPacket.getLength()).trim();

                int val = Integer.parseInt(recieveddata);

                if(datagramPacket.getPort()==port) //sytuacja jak odbiorę od samego siebie wiadomość

                {

                    System.out.println("received from myself: "+val);

                    continue;

                }

                byte[] ack = "yey!".getBytes();

                DatagramPacket ackpacket = new DatagramPacket(ack, ack.length, datagramPacket.getAddress(), datagramPacket.getPort());

                socket.send(ackpacket);

                switch (val) {

                    case -1: {

                        System.out.println(-1);

                        sendtoall(socket, -1);

                        socket.close();

                        run = false;

                        break;

                    }

                    case 0: {

                        int avg = 0;

                        int count = 0;

                        for (int a : nums) {

                            if (a != 0) {

                                avg += a;

                                count++;

                            }

                        }

                        avg /= count;

                        System.out.println("avg: "+avg);

                        sendtoall(socket, avg);

                        break;

                    }

                    default: {

                        System.out.println("value received: "+val);

                        nums.add(val);

                        break;

                    }

                }

            }

        } catch (Exception a)

        {

            System.out.println("blad w logice mastera");

            a.printStackTrace();

        }

    }

    private void sendtoall(DatagramSocket socket, int val)

    {

        try{

            byte[] tosend = String.valueOf(val).getBytes();

            DatagramPacket packet = new DatagramPacket(tosend, tosend.length, InetAddress.getByName("255.255.255.255"), port);

            socket.send(packet);

        } catch (IOException e) {

            System.out.println("rozglaszanie sie nie powiodlo");

            e.printStackTrace();

        }

    }

}

