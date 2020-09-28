package Calculator;
//  Author: Daniel Edwards
//   Class: CS 4800

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {
    private JPanel mainPanel;
    private JLabel valueDisplay;
    private JPanel buttonPanel;

    //region Buttons
    private JButton button0;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button8;
    private JButton button7;
    private JButton button9;
    private JButton buttonSign;
    //endregion

    //String currentOperand;
    //double currentOperand;
    double currentOperand;
    boolean currentHasFraction;

    public MainFrame() {
        currentOperand = 0;
        currentHasFraction = false;
        updateDisplay();

        JButton[] numberButtons = new JButton[] {
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
        };
        for(int i = 0; i < numberButtons.length; i++) {
            numberButtons[i].addActionListener(NumberActionListener(i));
        }

        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand *= -1;
            }
        });
    }

    private ActionListener NumberActionListener(int number) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand = currentOperand * 10 + number;
                updateDisplay();
            }
        };
    }

    private void updateDisplay() {
        double currentFraction = currentOperand % 1;
        double currentWhole = currentOperand - currentFraction;

        if(currentFraction == 0) {
            valueDisplay.setText(Long.toString((long)currentOperand));
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Calculator");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
