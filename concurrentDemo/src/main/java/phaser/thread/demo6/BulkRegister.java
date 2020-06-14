package phaser.thread.demo6;

import java.util.concurrent.Phaser;

/**
 * 批量添加parties
 */
public class BulkRegister {
    public static void main(String[] args) {
        Phaser phaser = new Phaser(10);
        System.out.println(phaser.getRegisteredParties());

        phaser.bulkRegister(10);
        System.out.println(phaser.getRegisteredParties());

        phaser.bulkRegister(10);
        System.out.println(phaser.getRegisteredParties());

        phaser.bulkRegister(10);
        System.out.println(phaser.getRegisteredParties());
    }
}
