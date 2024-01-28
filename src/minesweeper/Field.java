/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minesweeper;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Karel
 */
public class Field {
    
    private char[][] playingField;
    private char[][] minesField;
    
    public boolean inProgress = true;
    private int size;
    private int flagCount = 0;
    private int minesCount = 0;
    int[][] minesLocation;
    
    /*
    1 = Bgreen = 102
    2 = green = 42
    3 = blue = 44
    4 = Bblue = 104
    5 = magenta = 45
    6 = Bmagenta = 105
    7 = red = 41
    8 = Bred = 101
    default = Bwhite = 107 
    */
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_MAGENTA_BACKGROUND = "\u001B[45m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    
    public static final String ANSI_BLACK_TEXT = "\u001B[40m";
    public static final String ANSI_WHITE_TEXT = "\u001B[97m";
    

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    
    public void start(){
        createField(20);
        fillField(10);
    }
    
    public void printTitle(){
        System.out.println("#######################################");
        System.out.println("############# Minesweeper #############");
        System.out.println("#######################################");
        System.out.println("# By Karel Nakládal ######### 11.2023 #");
        System.out.println("#######################################");
    }
    
    public void endGame(int state){
        inProgress = false;
        switch(state){
            case 0:
                System.out.println("=====================");
                System.out.println("====== YOU WON ======");
                System.out.println("=====================");
                break;
            case 1:
                System.out.println("======================");
                System.out.println("====== YOU LOSE ======");
                System.out.println("======================");
                break;
            default:
                System.out.println("===========");
                System.out.println("== ERROR ==");
                System.out.println("===========");
                break;
        }
    }
    
    
    
    //Game setup
    
    public void createField(int a){
        size = a;
        playingField = new char[a][a];
        minesField = new char[a][a];
        
        for(int i = 0; i < playingField.length; i++){
            Arrays.fill(playingField[i], '#');
            Arrays.fill(minesField[i], ' ');
        }
    }
    
    public void fillField(int diff){
        Random random = new Random();
        int x,y;
        boolean generate;
        minesLocation = new int[diff][2];
        
        for(int i = 0; i < diff; i++){
            generate = true;
            while(generate){
                x = random.nextInt(size);
                y = random.nextInt(size);
                if(minesField[x][y] == ' '){
                    minesField[x][y] = '*';
                    minesLocation[i][0] = x;
                    minesLocation[i][1] = y;
                    minesCount++;
                    generate = false;
                }
            }
        }
        generateNumbers();
    }
    
    public void generateNumbers(){
        for(int i = 0; i <= minesField.length - 1; i++){
            for(int n = 0; n <= minesField.length - 1 ; n++){
                if(minesField[i][n] == '*'){
                    wrap(i, n);
                }
            }
        }
    }
    
    private void wrap(int x, int y){
        int idk;
        for(int i = -1; i <= 1; i++){
            for(int n = -1; n <= 1; n++){
                if(!isInField(x+i,y+n)){
                    continue;
                }
                
                switch(minesField[x+i][y+n]){
                    case' ':
                        minesField[x+i][y+n] = '1';
                        break;
                    case'*':
                        break;
                    default:
                        idk = Integer.parseInt(String.valueOf(minesField[x+i][y+n]));
                        minesField[x+i][y+n] = Character.forDigit(idk + 1, 10);
                }
            }
        }
    }
    
    
    
    //Game rules
    
    public boolean isInField(int x, int y){
       return  !(x < 0 || y < 0 || x > minesField.length - 1 || y > minesField.length - 1);
    }
    
    public void reveal(int x, int y){
            playingField[x][y] = minesField[x][y];
    }
    
    
    
    //Game logic
    
    public void turn(int x, int y, char action){
        switch(action){
            case' ':
            case'1':
            case'r':
            case'R':
                check(y, x);
                break;
            case'2':
            case'f':
            case'F':
                flag(y, x);
                break;
            case'd':
                printMines();
                break;
        }
    }
    
    public boolean check(int x, int y){
        if(minesField[x][y] != '*'){
            spread(x,y);
        }else{
            endGame(1);
            return false;
        }
        
        System.out.println(minesField[x][y]);
        return true;
    }
    
    public void spread(int x, int y){
        if (!isInField(x,y)) return;
        if(playingField[x][y] == minesField[x][y] || minesField[x][y] == '*'){
        }else if(minesField[x][y] == ' '){
            reveal(x,y);
            spread(x,y+1);
            spread(x,y-1);
            spread(x+1,y);
            spread(x-1,y);
        }else if(minesField[x][y] == '1'){
            reveal(x,y);
            spread(x,y+1);
            spread(x,y-1);
            spread(x+1,y);
            spread(x-1,y);
        }else{
            reveal(x,y);
        }
    }
 
    public void spread(int x, int y, boolean iterate){
        if (!isInField(x,y)) return;
        if(iterate){
            spread(x, y);
        }else{
            if(!(playingField[x][y] == minesField[x][y] || minesField[x][y] == '*')){
                reveal(x,y);
            }
        }
    }
    
    public void flag(int x, int y){
        if(playingField[x][y] == minesField[x][y]){
            
        }else if(playingField[x][y] == '#'){
            playingField[x][y] = 'X';
            flagCount++;
        }else{
            playingField[x][y] = '#';
            flagCount--;
        }
        if(minesCount == flagCount){
            checkMines();
        }
        
    }
    
    public void checkMines(){
        boolean win = true;
        for(int[] mine : minesLocation){
            if( minesField[mine[0]][mine[1]] != '*'){
                System.out.println(minesField[mine[0]][mine[1]]);
                System.out.println("X: " + mine[0] + " - Y: " + mine[1]);
                win = false;
            }
            
        }
        System.out.println("win " + win);
        if(win){
            endGame(0);
        }
    }
    
    
    
    //Graphics
    public void clear(){
        System.out.flush();
    }
    
    public void printField(){
        String bgColor;
        
        System.out.println("Flags: " + flagCount + "\nMines: " + minesCount);
        System.out.print("X  ");
        for(int i = 0; i < playingField.length; i++ ){
            
            if(i > 9){
            System.out.print("" + (i + 1) + "  ");
            }else{
            System.out.print(" " + (i + 1) + "  ");
            }
        }
        System.out.println("\nY");

        for(int i = 0; i < playingField.length; i++){
            
            if(i + 1 > 9){
            System.out.print(i + 1);
            }else{
            System.out.print((i + 1) + " ");
            }
            for(int n = 0; n < playingField[i].length; n++){
                bgColor = ANSI_WHITE_BACKGROUND;
                
                switch(playingField[i][n]){
                    case'1':
                        bgColor = ANSI_GREEN_BACKGROUND;
                        break;
                    case'2':
                        bgColor = ANSI_CYAN_BACKGROUND;
                        break;
                    case'3':
                        bgColor = ANSI_YELLOW_BACKGROUND;
                        break;
                    case'4':
                        bgColor = ANSI_MAGENTA_BACKGROUND;
                        break;
                    case'5':
                        bgColor = ANSI_RED_BACKGROUND;
                        break;
                    case'6':
                        bgColor = ANSI_RED_BACKGROUND;
                        break;
                    case'7':
                        bgColor = ANSI_RED_BACKGROUND;
                        break;
                    case'8':
                        bgColor = ANSI_RED_BACKGROUND;
                        break;
                    case'#':
                        break;
                    default:
                        break;
                }
                
                System.out.print(ANSI_WHITE_BACKGROUND + ' ' + bgColor + "[" + playingField[i][n] + "]" + ANSI_RESET);
            }
            System.out.print(" ");
            if(i + 1 > 9){
            System.out.print(i + 1);
            }else{
            System.out.print((i + 1) + " ");
            }
            System.out.println("");
        }
        
        System.out.print("   ");
        for(int i = 0; i < playingField.length; i++ ){
            
            if(i > 9){
            System.out.print("" + (i + 1) + "  ");
            }else{
            System.out.print(" " + (i + 1) + "  ");
            }
        }
        System.out.println("");

    }
    
    
    
    public void printMines(){
        String bgColor;
        
        System.out.print("X  ");
        for(int i = 0; i < minesField.length; i++ ){
            
            if(i > 9){
            System.out.print("" + (i + 1) + "  ");
            }else{
            System.out.print(" " + (i + 1) + "  ");
            }
        }
        System.out.println("\nY");

        for(int i = 0; i < minesField.length; i++){
            if(i + 1 > 9){
            System.out.print(i + 1);
            }else{
            System.out.print((i + 1) + " ");
            }
            for(int n = 0; n < minesField[i].length; n++){                
                bgColor = ANSI_WHITE_BACKGROUND;
                
                switch(minesField[i][n]){
                    case'1':
                    case'2':
                        bgColor = ANSI_GREEN_BACKGROUND;
                        break;
                    case'3':
                    case'4':
                        bgColor = ANSI_YELLOW_BACKGROUND;
                        break;
                    case'5':
                    case'6':
                        bgColor = ANSI_MAGENTA_BACKGROUND;
                        break;
                    case'7':
                    case'8':
                        bgColor = ANSI_RED_BACKGROUND;
                        break;
                    default:
                        break;
                }
                
                System.out.print(ANSI_WHITE_BACKGROUND + ' ' + bgColor + "[" + minesField[i][n] + "]" + ANSI_RESET);
            }
            System.out.println("");
        }

    }
}
