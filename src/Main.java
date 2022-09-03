import server.*;

import java.io.IOException;

public class Main {

    //Привет, отправляю на проверку, улучшения пока что не делал, пока что только ТЗ сделал


    public static void main(String[] args) throws IOException {
        new KVServer().start();
        new HttpTaskServer().start();
    }
}