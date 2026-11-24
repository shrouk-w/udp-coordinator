public class DAS {
    public static void main(String[] args) {

        if(args.length!=2)
        {
            System.out.println("nie poprawne dane");
            return;
        }
        try {
            UDP_serwer a = new UDP_serwer(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        }catch (NumberFormatException a)
        {
            System.out.println("nie poprawny format portu lub numeru");
        }
    }
}