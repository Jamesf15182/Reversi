import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.io.*;
import java.util.Scanner;

public class Reversi extends JFrame{

   JPanel turnLayout = new JPanel();
   JPanel board = new JPanel();
   JButton[][] squares = new JButton[8][8];
   
   JLabel currentTurn = new JLabel();
   JLabel totalXDisplay = new JLabel();
   JLabel totalODisplay = new JLabel();
   JLabel invalidMoveDisplay = new JLabel();
   
   String turn = "X";
   int totalX = 0;
   int totalO = 0;
   
   JPanel player1Total = new JPanel();
   JPanel player2Total = new JPanel();
   JPanel restartGame = new JPanel();
   JPanel moveDisplay = new JPanel();
   
   Font font1 = new Font("Times New Roman", Font.BOLD,20);
   Font font2 = new Font("Times New Roman", Font.BOLD,28);
   
   int move = 0;
   
   String[][] validMoves = new String[8][8];
   
   int nCounter = 0;
   int nwCounter = 0;
   int neCounter = 0;
   int sCounter = 0;
   int swCounter = 0;
   int seCounter = 0;
   int eCounter = 0;
   int wCounter = 0;
   
   int winCounter = 0;
   int noValidMoves = 0;
   int gameOver2 = 0;
   
   JButton restart = new JButton("Reset Game");
   
   public Reversi(){
   
      //creates the popout/template to work on
      JFrame menu = new JFrame();
      menu.setTitle("Final Project");
      menu.setSize(1000, 713);
      menu.setLayout(null);
      menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      menu.setResizable(false);
      ImageIcon gameImage = new ImageIcon("gameIcon.png");
      menu.setIconImage(gameImage.getImage());

      
      JMenuBar barMenu = new JMenuBar();
      menu.setJMenuBar(barMenu);
      
      JMenu rules = new JMenu("Rules");
      barMenu.add(rules);
      JMenuItem howToPlay = new JMenuItem("How To Play");
      rules.add(howToPlay);
      menu.setVisible(true);
      
      howToPlay.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent e){
            
         String displayHowToPlay = 
         "\nObjective: \n" 
         + "\nThe goal of the game is to have the most pieces on the board at the end of the game.\n"
         
         + "\nHow To Play: \n" 

	      + "\nEach piece played must be laid adjacent to an opponent's piece so that the opponent's piece \n"  
	      + "or a row of opponent's pieces is flanked by the new piece and another piece of the player's \n" 
	      + "letter. All of the opponent's pieces between these two pieces are 'captured' and turned over \n" 
	      + "to match the player's letter. It can also be that pieces or rows of pieces in more than one \n" 
	      + "direction are trapped between the new piece played too. In this case, all the pieces in all \n" 
	      + "viable directions are also turned over. \n" 

         + "\nEnd The Game: \n" 

	      + "\nTo finish the game the board must have no empty spaces left. Also, it will end if both users \n" 
	      + "have no valid moves. However, if only one player has no valid moves it will just pass their turn.\n";
         
         JOptionPane.showMessageDialog(null, displayHowToPlay, "How To Play:", JOptionPane.PLAIN_MESSAGE);
         
      }});//end actionListener
      
      //creates the game board and formats it
      board.setBackground(Color.GREEN);
      board.setBounds(0,50,600,600);
      board.setLayout(new GridLayout(8,8));
      
      //creates a banner at the top to show whose turn it is
      turnLayout.setBackground(Color.GRAY);
      turnLayout.setBounds(0,0,600,50);
      currentTurnDisplay();
      
      turnLayout.add(currentTurn);
      player1Total.add(totalXDisplay);
      player2Total.add(totalODisplay);
      
      //creates a reset button to rest the game if you want to rerestart
      restartGame.setBounds(675,550,250,75);
      restart.setFocusPainted(false);
      restart.setContentAreaFilled(false);
      restart.setPreferredSize(new Dimension(200,50));
      
      //will create the default board 
      createBoard();
      
      //will get the total number of pieces on the board
      countTotal();
      
      //used to show x total number of current X's
      player1Total.setBounds(675,200,250,75);
   
      //used to show x total number of current O's
      player2Total.setBounds(675,400,250,75);
      
      //used to show if the move was invalid
      moveDisplay.setBounds(675, 25, 250, 75);
      moveDisplay.add(invalidMoveDisplay);
      
      restartGame.add(restart);
      menu.add(moveDisplay);
      menu.add(restartGame);
      menu.add(player1Total);
      menu.add(player2Total);
      menu.add(turnLayout);
      menu.add(board);
      menu.setVisible(true);
            
      for (int i = 0; i < 8; i++){
      
         for (int j = 0; j < 8; j++){

               squares[i][j].addActionListener(
                  new ActionListener()
                  {
                      public void actionPerformed(ActionEvent ae){
                      
                        int value1 = 0;
                        int value2 = 0;
                      
                        for (int i = 0; i < 8; i++){
      
                           for (int j = 0; j < 8; j++){

                              if(ae.getSource() == squares[i][j]){
                      
                                 if(squares[i][j].getText() == ""){
                  
                                    if(turn == "X"){
                                    
                                       validMove(i,j);
                                       
                                       if(noValidMoves == 0 ){
                                       JOptionPane.showMessageDialog(menu, "No valid moves turn passed","Passed Turn", JOptionPane.PLAIN_MESSAGE);
                                       turn = "O";
                                       currentTurnDisplay();
                                       gameOver2++;
                                       }else if(move == 1){
                                             
                                          gameOver2 = 0; 
                                          squares[i][j].setText("X");
                                          squares[i][j].setFont(font2);
                                          squares[i][j].setContentAreaFilled(false);
                                          squares[i][j].setFocusPainted(false);
                                          changeSquares(i,j);
                                          turn = "O";
                                          countTotal();
                                          currentTurnDisplay();
                                          invalidMoveDisplay.setText("");
                                          gameOver();
                                          
                                          if(winCounter == 0){
                                          
                                              if(totalX > totalO){
                                              int minus = totalX - totalO;
                                              JOptionPane.showMessageDialog(menu, "Player X Wins by " + minus + " pieces", "Winner", JOptionPane.PLAIN_MESSAGE);
                                              } else if(totalO > totalX){
                                                int minus = totalO - totalX;
                                                JOptionPane.showMessageDialog(menu, "Player O Wins by " + minus + " pieces", "Winner", JOptionPane.PLAIN_MESSAGE);
                                              }else if (totalO == totalX){
                                                JOptionPane.showMessageDialog(menu, "Both Players have the same number of Pieces","Tied",JOptionPane.PLAIN_MESSAGE);
                                              }
                                              
                                              resetBoard();
                                              turn = "X";
                                              countTotal();
                                              currentTurnDisplay();

                                          }
                                           
        
                                          
                                       }else{
                                          gameOver2 = 0; 
                                          invalidMoveDisplay.setText("Invalid Move");
                                          invalidMoveDisplay.setForeground(Color.RED);
                                          invalidMoveDisplay.setFont(font1);
                                         
                                       }//end else
                                       
                                       if (gameOver2 == 2){
                                       
                                        if(totalX > totalO){
                                              int minus = totalX - totalO;
                                              JOptionPane.showMessageDialog(menu, "Player X Wins by " + minus + " pieces no more valid moves","Winner",JOptionPane.PLAIN_MESSAGE);
                                              } else if(totalO > totalX){
                                                int minus = totalO - totalX;
                                                JOptionPane.showMessageDialog(menu, "Player O Wins by " + minus + " pieces no more valid moves","Winner",JOptionPane.PLAIN_MESSAGE);
                                              }else if (totalO == totalX){
                                                JOptionPane.showMessageDialog(menu, "Both Players have the same number of Pieces","Tied",JOptionPane.PLAIN_MESSAGE);
                                              }

                                             resetBoard();
                                             turn = "X";
                                             countTotal();
                                             currentTurnDisplay();
                                       }
                                       
                                       move = 0;
                                       
                                    }else  if (turn =="O"){
                                    
                                       validMove(i,j);
                                       
                                       if(noValidMoves == 0 ){
                                       JOptionPane.showMessageDialog(menu, "No valid moves turn passed", "Passed Turn", JOptionPane.PLAIN_MESSAGE);
                                       turn = "O";
                                       currentTurnDisplay();
                                       gameOver2++;
                                       }else if(move == 1){
                                       
                                          gameOver2 = 0; 
                                          squares[i][j].setText("O");
                                          squares[i][j].setFont(font2);
                                          squares[i][j].setContentAreaFilled(false);
                                          squares[i][j].setFocusPainted(false);
                                          changeSquares(i,j);
                                          turn = "X";
                                          countTotal();
                                          currentTurnDisplay();
                                          invalidMoveDisplay.setText("");
                                          gameOver();
                                          
                                          if(winCounter == 0){
                                          
                                              if(totalX > totalO){
                                              int minus = totalX - totalO;
                                              JOptionPane.showMessageDialog(menu, "Player X Wins by " + minus + " pieces","Winner", JOptionPane.PLAIN_MESSAGE);
                                              } else if(totalO > totalX){
                                                int minus = totalO - totalX;
                                                JOptionPane.showMessageDialog(menu, "Player O Wins by " + minus + " pieces","Winner", JOptionPane.PLAIN_MESSAGE);
                                              }else if (totalO == totalX){
                                                JOptionPane.showMessageDialog(menu, "Both Players have the same number of Pieces","Tied",JOptionPane.PLAIN_MESSAGE);
                                              }
                                              
                                              resetBoard();
                                              turn = "X";
                                              countTotal();
                                              currentTurnDisplay();
                                          }
                                          
                                       }else{
                                          gameOver2 = 0; 
                                          invalidMoveDisplay.setText("Invalid Move");
                                          invalidMoveDisplay.setForeground(Color.RED);
                                          invalidMoveDisplay.setFont(font1);
                                          
                                       }//end else
                                       
                                       move = 0;
                                        if (gameOver2 == 2){
                                       
                                        if(totalX > totalO){
                                              int minus = totalX - totalO;
                                              JOptionPane.showMessageDialog(menu, "Player X Wins by " + minus + " pieces no more valid moves","Winner", JOptionPane.PLAIN_MESSAGE);
                                              } else if(totalO > totalX){
                                                int minus = totalO - totalX;
                                                JOptionPane.showMessageDialog(menu, "Player O Wins by " + minus + " pieces no more valid moves","Winner", JOptionPane.PLAIN_MESSAGE);
                                              }else if (totalO == totalX){
                                                JOptionPane.showMessageDialog(menu, "Both Players have the same number of Pieces","Tied",JOptionPane.PLAIN_MESSAGE);
                                              }
                                              resetBoard();
                                              turn = "X";
                                              countTotal();
                                              currentTurnDisplay();
                                       
                                       }
                                       
                                    }//end if
                                    
                                 }else{
                                 
                                        invalidMoveDisplay.setText("Invalid Move");
                                        invalidMoveDisplay.setForeground(Color.RED);
                                        invalidMoveDisplay.setFont(font1);
                                        
                                 }//end else
                     
                              }//end if
                     
                           }//end for
                           
                        }//end for

                }});//end actionListener
                
         }//end for
      }//end for
      
      restart.addActionListener(
         new ActionListener()
         {
            public void actionPerformed(ActionEvent e){
            
             //will create the default board 
             resetBoard();
      
             //will get the total number of pieces on the board
             countTotal();
             
             turn = "X";
            currentTurnDisplay();
                 
         }});//end actionListener
         
   }//end reversi
   
   //will create the initial board to play on
   public void createBoard(){
   
      for (int i = 0; i < 8; i++){
      
         for (int j = 0; j < 8; j++){
         
            squares[i][j] = new JButton();
            squares[i][j].setBackground(Color.GREEN);
            squares[i][j].setContentAreaFilled(false);
            board.add(squares[i][j]);
            
           if (i == 3 && j == 3){
           
               squares[3][3].setText("X");
               squares[3][3].setContentAreaFilled(false);
               squares[3][3].setFocusPainted(false);
               squares[3][3].setFont(font2);
               
           }//end if
           
           if (i == 3 && j == 4){
           
               squares[3][4].setText("O");
               squares[3][4].setContentAreaFilled(false);
               squares[3][4].setFocusPainted(false);
               squares[3][4].setFont(font2);
               
           }//end if
           
           if (i ==4 && j == 3){
           
               squares[4][3].setText("O");
               squares[4][3].setContentAreaFilled(false);
               squares[4][3].setFocusPainted(false);
               squares[4][3].setFont(font2);
               
           }//end if
           
           if (i == 4 && j == 4){
          
               squares[4][4].setText("X");
               squares[4][4].setContentAreaFilled(false);
               squares[4][4].setFocusPainted(false);
               squares[4][4].setFont(font2);
               
           }//end if
           
         }//end for
         
      }//end for
   
   }//end createBoard
   
   //will go through the squares array and count how many O's and X's there are
   public void countTotal(){
   
      totalX = 0;
      totalO = 0;
   
      for (int i = 0; i < 8; i++){
      
         for (int j = 0; j < 8; j++){
         
            if (squares[i][j].getText().equals("X")){
            
                totalX++;
                
            }//end if
            
            if (squares[i][j].getText().equals("O")){
            
                totalO++;
                
            }//end if
              
           
         }//end for
         
       }//end for
       
      totalXDisplay.setText("Total X's: " + totalX);
      totalXDisplay.setFont(font1);
      
      totalODisplay.setText("Total O's: " + totalO);
      totalODisplay.setFont(font1);
   
   }//end countTotal
   
   //will reset the board back to the default values
   public void resetBoard(){
   
      totalX = 0;
      totalO = 0;
   
      for (int i = 0; i < 8; i++){
      
         for (int j = 0; j < 8; j++){
         
            squares[i][j].setText("");
            
           if (i == 3 && j == 3){
           
               squares[3][3].setText("X");
               squares[3][3].setContentAreaFilled(false);
               squares[3][3].setFocusPainted(false);
               squares[3][3].setFont(font2);
               
           }//end if
           
           if (i == 3 && j == 4){
           
               squares[3][4].setText("O");
               squares[3][4].setContentAreaFilled(false);
               squares[3][4].setFocusPainted(false);
               squares[3][4].setFont(font2);
               
           }//end if
           
           if (i == 4 && j == 3){
           
               squares[4][3].setText("O");
               squares[4][3].setContentAreaFilled(false);
               squares[4][3].setFocusPainted(false);
               squares[4][3].setFont(font2);
               
           }//end if
           
           if (i == 4 && j == 4){
           
               squares[4][4].setText("X");
               squares[4][4].setContentAreaFilled(false);
               squares[4][4].setFocusPainted(false);
               squares[4][4].setFont(font2);
               
           }//end if
           
         }//end for
         
      }//end for
      
   }//end createBoard
   
   public void currentTurnDisplay(){
   
      currentTurn.setText("Current Turn: " + turn);
      currentTurn.setFont(font1);
   
   }//end currentTurnDisplay
   
   public void validMove(int i, int j){
   
     move = 0;
     String playerTurn = "";
     String other = "";
     noValidMoves = 0;
     
     if(turn == "X"){
         playerTurn = "X";
         other = "O";
     }
     
     if(turn == "O"){
         playerTurn = "O";
         other = "X";
     }
     
     for(int h = 0; h<8; h++){
		for(int k = 0; k<8; k++){
			validMoves[h][k] = "";
			}
		}
      
      int t = 0;
		int y = 0;
		int north = 2;
		int northW = 2;
		int northE = 2;
		int south = 2;
		int southW = 2;
		int southE = 2;
		int east = 2;
		int west = 2;
		int test = 0;
		int test2 = 0;
      
      //will go through the current board and find where the valid moves are
      for(t= 0; t<8; t++){
     
			for(y= 0; y<8; y++){
         
			   if(squares[t][y].getText() == playerTurn){
            
            //test if move is valid for north
			   north = 2;
			   while(north == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test-1 < 0 || test-1 >7){
				   north = 0; 
               break;
				}//end if
	
				if(test2+0 < 0 || test2+0 >7){
				   north = 0;
               break;
				}//end if

				if(test-2 < 0 || test-2 >7){
				   north = 0;
				   break;
				}//end if

				if(squares[test-1][test2].getText() != other){
					north = 0;
					break;
				}//end if

				if(north !=0){
				  north = 3;
				  test = test - 2;
				  break; 
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (north == 3){

					if(test < 0 || test >7){
				   		north = 0;
						   break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   north = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && north != 0){
						north = 1;
						validMoves[test][test2] = playerTurn;
						nCounter = 1;
						break;
					}//end if

					test--;

			}//end while


			 //test if move is valid for north
			   northE = 2;
			   while(northE == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test-1 < 0 || test-1 >7){
				   northE = 0;
				   break;
				}//end if
	
				if(test2+1 < 0 || test2+1 >7){
				   northE = 0;
				   break;
				}//end if

				if(test-2 < 0 || test-2 >7){
				   northE = 0;
				   break;
				}//end if

				if(test2+2 < 0 || test2+2 >7){
				   northE = 0;
				   break;
				}//end if

				if(squares[test-1][test2+1].getText() != other){
					northE = 0;
				   break;     
				}//end if

				if(northE !=0){
				  northE = 3;
				  test = test - 2;
				  test2 = test2 + 2;
				  break;
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (northE == 3){

					if(test < 0 || test >7){
				   		northE = 0;
						   break;
					}//end if

					if(test2 < 0 || test2 >7){
				   		northE = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   northE = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && northE != 0){
						northE = 1;
						validMoves[test][test2] = playerTurn;
						neCounter = 1;
                  break;
					}//end if

					test--;
					test2++;

			}//end while

			//test if move is valid for north
			   northW = 2;
			   while(northW == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test-1 < 0 || test-1 >7){
				   northW = 0;
				   break;
				}//end if
	
				if(test2-1 < 0 || test2-1 >7){
				   northW = 0;
				   break;
				}//end if

				if(test-2 < 0 || test-2 >7){
				   northW = 0;
				   break;
				}//end if

				if(test2-2 < 0 || test2-2 >7){
				   northW = 0;
				   break;
				}//end if

				if(squares[test-1][test2-1].getText() != other){
					northW = 0;
					break;
				}//end if

				if(northW !=0){
				  northW = 3;
				  test = test - 2;
				  test2 = test2 - 2;
				break;
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (northW == 3){

					if(test < 0 || test >7){
				   		northW = 0;
						break;
					}//end if
		
					if(test2 < 0 || test2 >7){
				   		northW = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   northW = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && northW != 0){
						northW = 1;
						validMoves[test][test2] = playerTurn;
						nwCounter = 1;
                  break;
					}//end if

					test--;
					test2--;

			}//end while
					
			 //test if move is valid for north
			   south= 2;
			   while(south == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test+1 < 0 || test+1 >7){
				   south = 0;
				   break;
				}//end if
	
				if(test2+0 < 0 || test2+0 >7){
				   south = 0;
				   break;
				}//end if

				if(test+2 < 0 || test+2 >7){
				   south = 0;
				   break;
				}//end if

				if(squares[test+1][test2].getText() != other){
					south = 0;
					break;
				}//end if

				if(south !=0){
				  south = 3;
				  test = test + 2;
				break;
				}	
				
			}//end while

				//only happen for north if passed the other checks
			while (south == 3){

					if(test < 0 || test >7){
				   		south = 0;
						break;
					}//end if

					

					if(squares[test][test2].getText()  == playerTurn){
					   south = 0;
					   break;
					}//end if

					if(squares[test][test2].getText()  == "" && south != 0){
						south = 1;
						validMoves[test][test2] = playerTurn;
						sCounter = 1;
                  break;
					}//end if

					test++;

			}//end while

			   //test if move is valid for north
			   southE = 2;
			   while(southE == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test+1 < 0 || test+1 >7){
				   southE = 0;
				      break;
				}//end if
	
				if(test2+1 < 0 || test2+1 >7){
				   southE = 0;
				  break;
				}//end if

				if(test+2 < 0 || test+2 >7){
				   southE = 0;
				   break;
				}//end if

				if(test2+2 < 0 || test2+2 >7){
				   southE = 0;
				   break;
				}//end if

				if(squares[test+1][test2+1].getText() != other){
					southE = 0;
					break;
				}//end if

				if(southE !=0){
				  southE = 3;
				  test = test + 2;
				  test2 = test2 + 2;
				   break;
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (southE == 3){

					if(test < 0 || test >7){
				   		southE = 0;
						break;
					}//end if

					
					if(test2 < 0 || test2 >7){
				   		southE = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   southE = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && southE != 0){
						southE = 1;
						validMoves[test][test2] = playerTurn;
						seCounter = 1;
                  break;
					}//end if

					test++;
					test2++;

			}//end while

			 //test if move is valid for north
			   southW = 2;
			   while(southW == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test+1 < 0 || test+1 >7){
				   southW = 0;
				     break;
				}//end if
	
				if(test2-1 < 0 || test2-1 >7){
				   southW = 0;
				  break;
				}//end if

				if(test+2 < 0 || test+2 >7){
				   southW = 0;
				   break;
				}//end if

				if(test2-2 < 0 || test2-2 >7){
				   southW = 0;
				   break;
				}//end if

				if(squares[test+1][test2-1].getText() != other){
					southW = 0;
					break;
				}//end if

				if(southW !=0){
				  southW = 3;
				  test = test + 2;
				  test2 = test2 - 2;
				break;
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (southW == 3){

					if(test < 0 || test >7){
				   		southW = 0;
						break;
					}//end if

					if(test2 < 0 || test2 >7){
				   		southW = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   southW = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && southW != 0){
						southW = 1;
						validMoves[test][test2] = playerTurn;
						swCounter = 1;
                  break;
					}//end if

					test++;
					test2--;

			}//end while

			//test if move is valid for north
			   east= 2;
			   while(east == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test+0 < 0 || test+0 >7){
				   east = 0;
				   break;
				}//end if
	
				if(test2+1 < 0 || test2+1 >7){
				   east = 0;
				   break;
				}//end if

				if(test2+2 < 0 || test2+2 >7){
				   east = 0;
				   break;
				}//end if

				if(squares[test][test2+1].getText() != other){
					east = 0;
					break;
				}//end if

				if(east !=0){
				  east = 3;
				  test2 = test2 + 2;
				break;
				}

			}//end while

				//only happen for north if passed the other checks
			while (east == 3){

					if(test2 < 0 || test2 >7){
				   		east = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   east = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && east != 0){
						east = 1;
						validMoves[test][test2] = playerTurn;
						eCounter = 1;
                  break;
					}//end if

					test2++;

			}//end while

			//test if move is valid for north
			   west= 2;
			   while(west == 2){

				//row
				test = t;
				//col
				test2 = y;
			        
				if(test+0 < 0 || test+0 >7){
				   west = 0;
				    break;
				}//end if
	
				if(test2-1 < 0 || test2-1 >7){
				   west = 0;
				   break;
				}//end if

				if(test2-2 < 0 || test2-2 >7){
				   west = 0;
				   break;
				}//end if

				if(squares[test][test2-1].getText() != other){
					west = 0;
					break;
				}//end if

				if(west !=0){
				  west = 3;
				  test2 = test2 - 2;
				break;
				}
				
				
			}//end while

				//only happen for north if passed the other checks
			while (west == 3){

					if(test2 < 0 || test2 >7){
				   		west = 0;
						break;
					}//end if

					if(squares[test][test2].getText() == playerTurn){
					   west = 0;
					   break;
					}//end if

					if(squares[test][test2].getText() == "" && west != 0){
						west = 1;
						validMoves[test][test2] = playerTurn;
						wCounter = 1;
                  break;
					}//end if

					test2--;

			}//end while
      
            }//end if
      
         }//end for
    
      }//end for
      
      
			if(validMoves[i][j] == "X"){
         
            move = 1;
            
			}//end if
         
         if(validMoves[i][j] == "O"){
         
            move = 1;
            
			}//end if
         
         for(int h = 0; h<8; h++){
		      for(int k = 0; k<8; k++){
            
               if(validMoves[h][k] == playerTurn){
			         noValidMoves++;
               }
			   }
		   }
       
   }
   
   public void changeSquares(int c, int y){
   
         
         
         int userRow = c;
         int userCol = y;
         int testCounter = 1;
		   int holder4 = 0;
         int holder5 = 0;
         int pass = 0;
         int testNumR = 0;
         int testNumC = 0;
         String player = "";
         String other = "";
         
         if(turn == "X"){
            player = "X";
            other = "O";
         } else if (turn == "O"){
            player = "O";
            other = "X";
         }
         
         
         holder4 = userRow;	
			holder5 = userCol;
			pass = 0;
         
			while(nCounter == 1){
         
            if(holder4+1 < 8){

				   if(squares[holder4+1][holder5].getText() == player){
					   testCounter = 0;
					   break;
               
				      }//end if
         
				   if(squares[holder4+1][holder5].getText() == ""){
					   testCounter = 0;
					   break;
				   }//end if
               
             }

				
				for(int i = 2; i<8; i++){

						if(holder4+i < 0 || holder4+i >7){
                     
							break;
						}
						
						if(squares[holder4+i][holder5].getText() == ""){
                  
						break;
					}

						if(squares[holder4+i][holder5].getText() == player){
							pass++;
                     
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4+1][holder5].getText() == other){
						squares[holder4+1][holder5].setText(player);
						holder4++;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				nCounter--;

			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(sCounter == 1){
         
            if(holder4-1 > 0){

				if(squares[holder4-1][holder5].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if
            

				if(squares[holder4-1][holder5].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            
            }
				
				for(int i = 2; i<8; i++){

						if(holder4-i < 0 || holder4-i >7){
                  
							break;
						}

						if(squares[holder4-i][holder5].getText() == ""){
                  
							break;
					}

						if(squares[holder4-i][holder5].getText() == player){
							pass++;
                     
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				while(testCounter != 0){

					if(squares[holder4-1][holder5].getText() == other){
						squares[holder4-1][holder5].setText(player);
						holder4--;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				sCounter--;
			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(eCounter== 1){
            
            if(holder5-1 > 0){

				if(squares[holder4+0][holder5-1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4+0][holder5-1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            
            }

				
				for(int i = 2; i<8; i++){

						if(holder5-i < 0 || holder5-i >7){
                  
							break;
						}

						if(squares[holder4][holder5-i].getText() == ""){
                  
							break;
						}

						if(squares[holder4][holder5-i].getText() == player){
							pass++;
                     
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4][holder5-1].getText() == other){
						squares[holder4][holder5-1].setText(player);
						holder5--;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				eCounter--;
	
			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while( wCounter== 1){

         if(holder5+1 > 8){

				if(squares[holder4+0][holder5+1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4+0][holder5+1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            
            }

				
				for(int i = 2; i<8; i++){

						if(holder5+i < 0 || holder5+i >7){
                  
							break;
						}

						if(squares[holder4][holder5+i].getText() == ""){
                  
						break;
					}

						if(squares[holder4][holder5+i].getText() == player){
							pass++;
                     
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4][holder5+1].getText() == other){
						squares[holder4][holder5+1].setText(player);
						holder5++;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				wCounter--;
			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(neCounter == 1){

         if(holder4+1 < 8 && holder5-1 > 0){
				if(squares[holder4+1][holder5-1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4+1][holder5-1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
           }

				
				for(int i = 2; i<8; i++){

						if(holder4+i < 0 || holder4+i >7){
                  
				   			break;
				   
						}//end if
	
						if(holder5-i < 0 || holder5-i >7){
                  
				  			 break;
				
						}//end if

						if(squares[holder4+i][holder5-i].getText() == ""){
                  
						break;
					}

						if(squares[holder4+i][holder5-i].getText() == player){
							pass++;
                     
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4+1][holder5-1].getText() == other){
						squares[holder4+1][holder5-1].setText(player);
						holder4++;
						holder5--;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				neCounter--;
			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(nwCounter == 1){

            if(holder4+1 < 8 && holder5+1 < 8){
            
				if(squares[holder4+1][holder5+1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4+1][holder5+1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            
            }

				
				for(int i = 2; i<8; i++){

					if(holder4+i < 0 || holder4+i >7){
               
				   		break;
				   
					}//end if
	
					if(holder5+i < 0 || holder5+i >7){
               
				  		break;
				
					}//end if
				
					if(squares[holder4+i][holder5+i].getText() == ""){
               
						break;
					}

					if(squares[holder4+i][holder5+i].getText() == player){
							pass++;
                     	
							break;
						}
					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4+1][holder5+1].getText() == other){
						squares[holder4+1][holder5+1].setText(player);
						holder4++;
						holder5++;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				nwCounter--;
	
			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(seCounter== 1){

            if(holder4-1 > 0 && holder5-1 > 0){
				if(squares[holder4-1][holder5-1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4-1][holder5-1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            }

				
				for(int i = 2; i<8; i++){

					if(holder4-i < 0 || holder4-i >7){
               
				   		break;
				   
					}//end if
	
					if(holder5-i < 0 || holder5-i >7){
               
				  		break;
				
					}//end if
				
					if(squares[holder4-i][holder5-i].getText() == ""){
               
						break;
					}

					if(squares[holder4-i][holder5-i].getText() == player){
						pass++;
                  
						break;
					}

					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}
				
				while(testCounter != 0){

					if(squares[holder4-1][holder5-1].getText() == other){
						squares[holder4-1][holder5-1].setText(player);
						holder4--;
						holder5--;
						testCounter = 1;
					}else{
						testCounter = 0;
                  
                  break;
					}
				}

				seCounter--;

			}//end while

			holder4 = userRow;
			holder5 = userCol;
			pass = 0;
			testCounter = 1;
			while(swCounter == 1){

            if(holder4-1 >0 && holder5+1 < 8){
				if(squares[holder4-1][holder5+1].getText() == ""){
					testCounter = 0;
               
					break;
				}//end if

				if(squares[holder4-1][holder5+1].getText() == player){
					testCounter = 0;
               
					break;
				}//end if
            }

				
				for(int i = 2; i<8; i++){

					if(holder4-i < 0 || holder4-i >7){
               
				   		break;
				   
					}//end if
	
					if(holder5+i < 0 || holder5+i >7){
               
				  		break;
				
					}//end if
			
					if(squares[holder4-i][holder5+i].getText() == ""){
               
						break;
					}

					if(squares[holder4-i][holder5+i].getText() == player){
						pass++;
						break;
					}

					}

					if(pass >= 1){
						testCounter = 1;
					}else{
						testCounter = 0;
					}

				
				while(testCounter != 0){

					if(squares[holder4-1][holder5+1].getText() == other){
						squares[holder4-1][holder5+1].setText(player);
						holder4--;
						holder5++;
						testCounter = 1;
					}else{
						testCounter = 0;
                  break;
					}
				}

				swCounter--;
			}//end while
                 
}

   public void gameOver(){
   
   winCounter = 0;
   
      for (int i = 0; i < 8; i++){
      
         for (int j = 0; j < 8; j++){
         
               if (squares[i][j].getText() == ""){
                  winCounter++;
               
               }//end if
         }//end for
         
      }//end for
   
   }//end gameOver()
    
   public static void main(String[] args) {
   
      new Reversi();
      
   }//end main
   
}//end class