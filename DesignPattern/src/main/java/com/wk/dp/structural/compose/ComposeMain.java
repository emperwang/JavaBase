package com.wk.dp.structural.compose;

/**
 * @author: ekiawna
 * @Date: 2021/4/1 12:45
 * @Description
 */
public class ComposeMain {
    public static void main(String[] args) {
        MainBoard mainBoard = new MainBoard();
        mainBoard.addComponent(new Cpu("CPU"));
        mainBoard.addComponent(new NetCard("netCard"));
        ComputerCase computerCase = new ComputerCase();
        computerCase.addComponent(mainBoard);

        Computer computer = new Computer();
        computer.addComponent(computerCase);
        computer.addComponent(new Displayer("display"));

        computer.showInfo();
    }
}
