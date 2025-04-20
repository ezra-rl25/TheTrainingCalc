package APIImplementation;

public class testGet {
    public static void main(String[] args) {
        Authorization auth = new Authorization();

        System.out.println("Name: " + auth.getName());
        System.out.println("City: " + auth.getCity());
    }
}