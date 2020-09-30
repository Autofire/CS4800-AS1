package Calculator;
//  Author: Daniel Edwards
//   Class: CS 4800

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

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
    private JButton buttonDecimal;
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
            //numberButtons[i].setMnemonic(Integer.toString(i).charAt(0));
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
            	clearOperand();
            }
        });
        buttonClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	clearOperand();
                currentOperation = Operation.NONE;
            }
        });
        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(currentOperandIsResult) {
            	    clearOperand();
                }
            	else {
                    currentOperand.deleteCharAt(currentOperand.length() - 1);
                    fixOperand();
                    updateDisplay();
                }
            }
        });
        buttonDecimal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Need to check this if the result has a decimal place in it.
                if(currentOperandIsResult) {
                    clearOperand();
                }

                if(currentOperand.indexOf(".") == -1) {
                    appendToOperand('.');
                }
            }
        });
    }

    private ActionListener numberActionListener(int number) {
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	appendToOperand(Integer.toString(number).charAt(0));
            }
        };

        return listener;
    }

    private ActionListener operationActionListener(Operation op) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushOperation(op);
            }
        };
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

            if(result % 1 > 0.000001) {
                currentOperand = new StringBuilder(
                        String.format("%f", result)
                );
            }
            else {
                currentOperand = new StringBuilder(
                        String.format("%.0f", result)
                );
            }

            currentOperation = Operation.NONE;
            currentOperandIsResult = true;
            updateDisplay();
        }
    }

    private void clearOperand() {
        currentOperand = new StringBuilder("0");
        currentOperandIsResult = false;
        updateDisplay();
    }

    private void appendToOperand(char newChar) {

        if(currentOperandIsResult) {
        	clearOperand();
        }

        currentOperand.append(newChar);

        fixOperand();
        updateDisplay();
    }

    private void fixOperand() {
        // It's possible to have an empty operand here.
        // For that reason, we MUST check for that first.
        // If the operand is empty, we'll say it's positive zero.
		boolean isNegative = currentOperand.length() > 0 && currentOperand.charAt(0) == '-';

		// If the number is negative, strip that out for now.
		if(isNegative) {
		    currentOperand.deleteCharAt(0);
        }

		if(currentOperand.length() == 0) {
            // We're empty; stick a zero on
            currentOperand.append('0');
        }
		else {
            // Remove leading zeroes
            String[] parts = currentOperand.toString().split("\\.");

            // If we end with '.', we need to preserve that.
			// If the number already has digits after '.', then the split works.
            if(currentOperand.charAt(currentOperand.length()-1) == '.') {
                parts = new String[]{parts[0], ""};
            }

            StringBuilder wholePart = new StringBuilder(parts[0]);
            while (wholePart.length() > 1 && wholePart.charAt(0) == '0') {
                wholePart.deleteCharAt(0);
            }
            parts[0] = wholePart.toString();

            currentOperand = new StringBuilder(String.join(".", parts));
        }

        // If we stripped a negative out, throw it back in.
        if(isNegative) {
            currentOperand.insert(0, '-');
        }
    }

    private void updateDisplay() {
        valueDisplay.setText(currentOperand.toString());
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
