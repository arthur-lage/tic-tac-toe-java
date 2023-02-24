import java.awt.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;

public class TicTacToe extends JPanel {
    boolean isPlayerXsTurn;
    boolean isGameDone = false;
    int winner = -1;
    int playerOneScore = 0, playerTwoScore = 0;
    int[][] board = new int[3][3];

    int lineWidth = 5;
    int lineLength = 270;
    int x = 15, y = 100;
    int offset = 95;
    int a = 0;
    int b = 5;
    int selX = 0, selY = 0;

    Color turtle = new Color(0x80bdab);
    Color orange = new Color(0xfdcb9e);
    Color offwhite = new Color(0xf7f7f7);
    Color darkgray = new Color(0x3f3f44);

    JButton jButton;

    public void resetGame() {
        isPlayerXsTurn = true;
        winner = -1;
        isGameDone = false;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }

        getJButton().setVisible(false);
    }

    public JButton getJButton() {
        return jButton;
    }

    public void setPlayerOneScore(int a) {
        playerOneScore = a;
    }

    public void setPlayerTwoScore(int a) {
        playerTwoScore = a;
    }

    public TicTacToe() {
        Dimension size = new Dimension(420, 300);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);
        jButton = new JButton("Play again?");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        add(jButton);
        getJButton().setVisible(false);
        addMouseListener(new XOListener());
        resetGame();
    }

    public void paintComponent(Graphics page) {
        super.paintComponent(page);
        drawBoard(page);
        drawUI(page);
        drawGame(page);
    }

    public void drawBoard(Graphics page) {
        setBackground(turtle);
        page.setColor(darkgray);
        page.fillRoundRect(x, y, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(x, y + offset, lineLength, lineWidth, 5, 30);
        page.fillRoundRect(y, x, lineWidth, lineLength, 5, 30);
        page.fillRoundRect(y + offset, x, lineWidth, lineLength, 5, 30);
    }

    public void drawUI(Graphics page) {
        // CREATE UI ON RIGHT AND SET FONT
        page.setColor(darkgray);
        page.fillRect(300, 0, 120, 300);
        Font font = new Font("Helvetica", Font.PLAIN, 20);
        page.setFont(font);

        // SHOW PLAYERS SCORES
        page.setColor(offwhite);
        page.drawString("Win Count", 310, 30);
        page.drawString(": " + playerOneScore, 362, 70);
        page.drawString(": " + playerTwoScore, 362, 105);

        // DRAW X USING IMAGE
        ImageIcon xIcon = new ImageIcon("orangex.png");
        Image xImg = xIcon.getImage();
        Image newXImg = xImg.getScaledInstance(27, 27, java.awt.Image.SCALE_SMOOTH);
        ImageIcon newXIcon = new ImageIcon(newXImg);
        page.drawImage(newXIcon.getImage(), 44 + offset * 1 + 190, 47 + offset * 0, null);

        // DRAW O
        page.setColor(offwhite);
        page.fillOval(328, 80, 30, 30);
        page.setColor(darkgray);
        page.fillOval(334, 85, 19, 19);

        page.setColor(offwhite);
        Font font1 = new Font("serif", Font.ITALIC, 18);
        page.setFont(font1);

        if (isGameDone) {
            if (winner == 1) {
                page.drawString("The winner is", 310, 150);
                page.drawImage(xImg, 335, 160, null);
            } else if (winner == 2) {
                page.drawString("The winner is", 310, 150);
                page.setColor(offwhite);
                page.fillOval(332, 160, 50, 50);
                page.setColor(darkgray);
                page.fillOval(342, 170, 30, 30);
            } else if (winner == 3) {
                page.drawString("It is a tie!", 330, 178);
            }
        } else {
            Font font2 = new Font("Serif", Font.ITALIC, 20);
            page.setFont(font2);
            page.drawString("It is,", 350, 160);

            if (isPlayerXsTurn) {
                page.drawString("X's turn", 325, 180);
            } else {
                page.drawString("O's turn", 325, 180);
            }
        }
    }

    public void drawGame(Graphics page) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 1) {
                    ImageIcon xIcon = new ImageIcon("orangex.png");
                    Image xImg = xIcon.getImage();
                    page.drawImage(xImg, 30 + offset * i, 30 + offset * j, null);
                }

                if (board[i][j] == 2) {
                    page.setColor(offwhite);
                    page.fillOval(30 + offset * i, 30 + offset * j, 50, 50);
                    page.setColor(turtle);
                    page.fillOval(40 + offset * i, 40 + offset * j, 30, 30);
                }
            }
        }

        repaint();
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("Tic Tac Toe");
        frame.getContentPane();

        TicTacToe tPanel = new TicTacToe();
        frame.add(tPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);

        frame.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                File file = new File("score.txt");

                try {
                    Scanner sc = new Scanner(file);
                    tPanel.setPlayerOneScore(Integer.parseInt(sc.nextLine()));
                    tPanel.setPlayerTwoScore(Integer.parseInt(sc.nextLine()));
                    sc.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    PrintWriter pw = new PrintWriter("score.txt");
                    pw.write(tPanel.playerOneScore + "\n");
                    pw.write(tPanel.playerTwoScore + "\n");
                    pw.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    public class XOListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent event) {
            selX = -1;
            selY = -1;
            if (isGameDone == false) {
                a = event.getX();
                b = event.getY();

                if (a > 12 && a < 99) {
                    selX = 0;
                } else if (a > 103 && a < 195) {
                    selX = 1;
                } else if (a > 200 && a < 287) {
                    selX = 2;
                } else {
                    selX = -1;
                }

                if (b > 12 && b < 99) {
                    selY = 0;
                } else if (b > 103 && b < 195) {
                    selY = 1;
                } else if (b > 200 && b < 287) {
                    selY = 2;
                } else {
                    selY = -1;
                }

                if (selX != -1 && selY != -1) {
                    if (board[selX][selY] == 0) {
                        if (isPlayerXsTurn) {
                            board[selX][selY] = 1;
                            isPlayerXsTurn = false;
                        } else {
                            board[selX][selY] = 2;
                            isPlayerXsTurn = true;
                        }

                        checkWinner();
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public void checkWinner() {
        if (isGameDone) {
            System.out.println("game over");
            return;
        }

        int temp = -1;

        if ((board[0][0] == board[0][1])
                && (board[0][1] == board[0][2])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[1][0] == board[1][1])
                && (board[1][1] == board[1][2])
                && (board[1][0] != 0)) {
            temp = board[1][1];
        } else if ((board[2][0] == board[2][1])
                && (board[2][1] == board[2][2])
                && (board[2][0] != 0)) {
            temp = board[2][1];

            // horizontal
        } else if ((board[0][0] == board[1][0])
                && (board[1][0] == board[2][0])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[0][1] == board[1][1])
                && (board[1][1] == board[2][1])
                && (board[0][1] != 0)) {
            temp = board[0][1];
        } else if ((board[0][2] == board[1][2])
                && (board[1][2] == board[2][2])
                && (board[0][2] != 0)) {
            temp = board[0][2];

            // diagonal
        } else if ((board[0][0] == board[1][1])
                && (board[1][1] == board[2][2])
                && (board[0][0] != 0)) {
            temp = board[0][0];
        } else if ((board[0][2] == board[1][1])
                && (board[1][1] == board[2][0])
                && (board[0][2] != 0)) {
            temp = board[0][2];
        } else {
            // CHECK FOR A TIE
            boolean notDone = false;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == 0) {
                        notDone = true;
                        break;
                    }
                }
            }
            if (notDone == false) {
                temp = 3;
            }
        }
        if (temp > 0) {
            winner = temp;
            if (winner == 1) {
                setPlayerOneScore(playerOneScore + 1);
            }

            if (winner == 2) {
                setPlayerTwoScore(playerTwoScore + 1);
            }

            isGameDone = true;
            getJButton().setVisible(true);
        }
    }
}