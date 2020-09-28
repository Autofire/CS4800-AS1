package Calculator;
//  Author: Daniel Edwards
//   Class: CS 4800

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {

    private enum Operation {
        NONE, ADD, MINUS, MULTIPLY, DIVIDE
    }

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
    private JButton buttonDivide;
    private JButton buttonMultiply;
    private JButton buttonMinus;
    private JButton buttonAdd;
    private JButton buttonEquals;
    //endregion

    //String currentOperand;
    //double currentOperand;
    double currentOperand;
    boolean currentHasFraction;

    Operation currentOperation;
    double otherOperand;

    public MainFrame() {
        currentOperand = 0;
        currentHasFraction = false;

        otherOperand = 0;
        currentOperation = Operation.NONE;

        updateDisplay();

        JButton[] numberButtons = new JButton[] {
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
        };
        for(int i = 0; i < numberButtons.length; i++) {
            numberButtons[i].addActionListener(numberActionListener(i));
        }

        buttonAdd.addActionListener     (operationActionListener(Operation.ADD));
        buttonMinus.addActionListener   (operationActionListener(Operation.MINUS));
        buttonMultiply.addActionListener(operationActionListener(Operation.MULTIPLY));
        buttonDivide.addActionListener  (operationActionListener(Operation.DIVIDE));

        buttonEquals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolveOperation();
            }
        });
        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand = -currentOperand;
                updateDisplay();
            }
        });
    }

    private ActionListener numberActionListener(int number) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand = currentOperand * 10 + number;
                updateDisplay();
            }
        };
    }

    private ActionListener operationActionListener(Operation op) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushOperation(op);
            }
        };
    }

    private void updateDisplay() {

        double currentFraction = currentOperand % 1;
        //double currentWhole = currentOperand - currentFraction;

        String text = "";
        if(currentFraction == 0) {
            text += (Long.toString((long)currentOperand));
        }
        else {
            text += (Double.toString(currentOperand));
        }

        valueDisplay.setText(text);
    }

    private void pushOperation(Operation op) {

        if(currentOperation != Operation.NONE) {
            resolveOperation();
        }

        currentOperation = op;

        // Also push operand. Don't update quite yet.
        otherOperand = currentOperand;
        currentOperand = 0;
    }

    private void resolveOperation() {
        if(currentOperation != Operation.NONE) {

            switch(currentOperation) {
                case ADD:
                    currentOperand += otherOperand;
                    break;
                case MINUS:
                    currentOperand = otherOperand - currentOperand;
                    break;
                case MULTIPLY:
                    currentOperand *= otherOperand;
                    break;
                case DIVIDE:
                    currentOperand = otherOperand / currentOperand;
                    break;
            }

            currentOperation = Operation.NONE;
            updateDisplay();
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
