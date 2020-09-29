package Calculator;
//  Author: Daniel Edwards
//   Class: CS 4800

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {

    private enum Operation {
        NONE, PLUS, MINUS, TIMES, DIVIDE
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
    private JButton buttonTimes;
    private JButton buttonMinus;
    private JButton buttonPlus;
    private JButton buttonEquals;
    private JButton buttonClearEntry;
    private JButton buttonDelete;
    private JButton buttonClear;
    //endregion

    //String currentOperand;
    //double currentOperand;
    StringBuilder currentOperand;
    StringBuilder firstOperand;
    boolean currentOperandIsResult;

    Operation currentOperation;

    public MainFrame() {
        currentOperand = new StringBuilder("0");
        firstOperand = new StringBuilder("0");
        currentOperation = Operation.NONE;
        currentOperandIsResult = false;

        updateDisplay();

        JButton[] numberButtons = new JButton[] {
                button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9
        };
        for(int i = 0; i < numberButtons.length; i++) {
            numberButtons[i].addActionListener(numberActionListener(i));
        }

        buttonPlus.addActionListener  (operationActionListener(Operation.PLUS));
        buttonMinus.addActionListener (operationActionListener(Operation.MINUS));
        buttonTimes.addActionListener (operationActionListener(Operation.TIMES));
        buttonDivide.addActionListener(operationActionListener(Operation.DIVIDE));

        buttonEquals.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resolveOperation();
            }
        });
        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(currentOperand.charAt(0) != '-') {
                    currentOperand.insert(0, "-");
                }
                else {
                    currentOperand.deleteCharAt(0);
                }
                updateDisplay();
            }
        });
        buttonClearEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand = new StringBuilder("0");
                updateDisplay();
            }
        });
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOperand = new StringBuilder("0");
                currentOperation = Operation.NONE;
                updateDisplay();
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	currentOperand.deleteCharAt(currentOperand.length()-1);
                updateDisplay();
            }
        });
    }

    private ActionListener numberActionListener(int number) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(currentOperandIsResult) {
            	    currentOperand = new StringBuilder(Integer.toString(number));
            	    currentOperandIsResult = false;
                }
            	else {
                    currentOperand.append(number);
                }

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
        valueDisplay.setText(currentOperand.toString());
    }

    private void pushOperation(Operation op) {

        if(currentOperation != Operation.NONE) {
            resolveOperation();
        }

        currentOperation = op;

        // Also push operand. Don't update quite yet.
        firstOperand = currentOperand;
        currentOperandIsResult = false;
        currentOperand = new StringBuilder("0");
    }

    private void resolveOperation() {
        if(currentOperation != Operation.NONE) {

            double op1 = Double.parseDouble(firstOperand.toString());
            double op2 = Double.parseDouble(currentOperand.toString());
            double result = Double.NaN;

            switch(currentOperation) {
                case PLUS:   result = op1 + op2; break;
                case MINUS:  result = op1 - op2; break;
                case TIMES:  result = op1 * op2; break;
                case DIVIDE: result = op1 / op2; break;
            }

            currentOperand = new StringBuilder(Double.toString(result));

            currentOperation = Operation.NONE;
            currentOperandIsResult = true;
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
