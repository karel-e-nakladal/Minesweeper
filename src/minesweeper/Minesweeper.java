/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package minesweeper;

import java.util.Scanner;

/**
 *
 * @author nakla
 */
public class Minesweeper {

    /**
     * @param args the command line arguments
     */
    public static void main(String []args){
        
        Field game = new Field();
        int[] defaultSize = {10,30,50};
        
        boolean defaultSettings = false, run = true, gameInProgress = true;
        char action;
        int x,y,size;
        Scanner input = new Scanner(System.in);
        
        
        game.printTitle();
        while(run){
            System.out.println("Select board size \n[1]10x10\n[2]30x30\n[3]50x50");
            size = input.nextInt();
            
            switch(size){
                case 1:
                    game.createField(10);
                    break;
                case 2:
                    game.createField(30);
                    break;
                case 3:
                    game.createField(50);
                    break;
                default:
                    defaultSettings = true;
                    game.start();
                    break;
            }
            if (!defaultSettings){
                //System.out.println(size);
                //game.fillField(size);
                
                System.out.println((size +2) * defaultSize[size-1]);
                game.fillField((size + 2) * defaultSize[size-1]);
            }
            
            while(gameInProgress){
                game.clear();
                game.printField();
                System.out.println("Reveal a field (1/R)\nFlag a mine (2/F)\nPlease select action from list above:");
                action = input.next().charAt(0);
                System.out.print("Please select X:");
                x = input.nextInt() - 1;

                System.out.print("Please select Y:");
                y = input.nextInt() - 1;


                if(game.isInField(x, y))game.turn(x, y, action);

                run = game.inProgress;
                gameInProgress = game.inProgress;
            }
        }
        
    }
    
    
}
