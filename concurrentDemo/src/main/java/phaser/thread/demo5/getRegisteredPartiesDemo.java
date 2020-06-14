package phaser.thread.demo5;

import java.util.concurrent.Phaser;

/**
 * 动态的添加parties
 */
public class getRegisteredPartiesDemo {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(5);
        System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());

        phaser.register();
        System.out.println(phaser.getRegisteredParties());



    }
}
