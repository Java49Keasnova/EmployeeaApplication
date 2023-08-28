package telran.net;
public class CalculatorTcpServerAppl {
    static final int PORT=5000;
    public static void main(String[] args) throws Exception{
        ApplProtocol protocol = new CalculatorProtocol();
        TcpServer server = new TcpServer(PORT, protocol);
        server.run();
    }





}