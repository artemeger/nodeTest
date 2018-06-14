

public class Main {

    Communication com = new Communication();

    public static void main(String [] args){

       Main main = new Main();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       main.startAllTheThings();

    }

    private void startAllTheThings() {
        Thread msgs = new Thread(new Runnable() {
            int counter  = 0;
            @Override
            public void run() {
                while(counter < 50){
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    com.sendMessage();
                    counter++;
                    System.out.println("Send Message: "+ counter);
                }
            }
        });

        Thread status = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    com.sendStatusRequest();
                    com.sendDumpConsensusRequest();
                }
            }
        });

        msgs.start();
        status.start();

    }


}
