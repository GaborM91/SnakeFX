/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snakefx;



import java.io.File;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author maklari
 */
public class SnakeFX extends Application {
    Loop l= new Loop();
    GraphicsContext gc;
    boolean up,down,left,right,isDirectionChanged,catched,match;
    double x=0,y=0;
    final int fpsDivider=3;
    int frames=0;
    ArrayList<Positions> p= new ArrayList<>();
    Random r= new Random();
    Integer randomx,randomy;
    double predx=485,predy=485;
    String musicFile = "beep.wav";     

Media sound ;
MediaPlayer mediaPlayer ;
 
    
    @Override
    public void start(Stage primaryStage) {
        sound = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
       primaryStage.setTitle("SnakeFX");
        Group root = new Group();
        Canvas canvas = new Canvas(1000, 1000);
        gc = canvas.getGraphicsContext2D();
        gc.setLineWidth(5);
        gc.setStroke(Color.BLUE);
        right=true;
        p.add(new Positions(205,5));
        p.add(new Positions(165,5));
        p.add(new Positions(125,5));
        p.add(new Positions(85,5));
        p.add(new Positions(45,5));
        
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
           @Override
           public void handle(KeyEvent event) {
               if(event.getCode()==KeyCode.RIGHT && !right && !left && !isDirectionChanged){
                    up=false;
                    down=false;
                    left=false;
                    right=true;    
                    isDirectionChanged=true;
               }
               if(event.getCode()==KeyCode.LEFT && !left && !right && !isDirectionChanged){
                    up=false;
                    down=false;
                    left=true;
                    right=false;
                    isDirectionChanged=true;
               }
               if(event.getCode()==KeyCode.UP && !up && !down && !isDirectionChanged){
                    up=true;
                    down=false;
                    left=false;
                    right=false;
                    isDirectionChanged=true;
               }
               if(event.getCode()==KeyCode.DOWN && !down && !up && !isDirectionChanged){
                    up=false;
                    down=true;
                    left=false;
                    right=false;
                    isDirectionChanged=true;
               }
               
           }
            
        });
        
        root.getChildren().add(canvas);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        l.start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    class Loop extends AnimationTimer{

        @Override
        public void handle(long now) {
            if(frames%fpsDivider==0){
                for (int i =p.size()-1;i>0;--i){
                    p.get(i).x=p.get(i-1).x;
                    p.get(i).y=p.get(i-1).y;
                }
            }
            if(up==true  && frames%fpsDivider==0){
                p.get(0).y-=40;
                isDirectionChanged=false;
            }
            else if(down==true && frames%fpsDivider==0){
                p.get(0).y+=40;
                isDirectionChanged=false;
            }
            else if(left==true && frames%fpsDivider==0){
                p.get(0).x-=40;
                isDirectionChanged=false;
            }
            else if(right==true && frames%fpsDivider==0){
                p.get(0).x+=40;
                isDirectionChanged=false;
            }
            
            if(p.get(0).x==1005){
                p.get(0).x=5;
            }
            if(p.get(0).x==-35){
                p.get(0).x=965;
            }
            if(p.get(0).y==1005){
                p.get(0).y=5;
            }
            if(p.get(0).y==-35){
                p.get(0).y=965;
            }            
            
            gc.clearRect(0, 0, 1000, 1000);
            for(int i=p.size()-1;i>=0;--i){
                gc.strokeRoundRect(p.get(i).x, p.get(i).y, 30, 30, 10, 10);
                
            }
            gc.setStroke(Color.RED);
                gc.strokeOval(predx, predy, 30, 30);
                gc.setStroke(Color.BLUE);
            for(int i=1;i<p.size();++i){
                if(p.get(i).x == p.get(0).x && p.get(i).y==p.get(0).y){
                    l.stop();
                }                
                
            }
            if(p.get(0).x==predx && p.get(0).y==predy && frames%fpsDivider==0){
                    //problémás még  
                    mediaPlayer.stop();
                    p.add(new Positions(p.get(2).x,p.get(2).y));
                    catched=true;
                    mediaPlayer.play();
                    
                }
            if(frames%fpsDivider==0){
                frames=0;
            }
            frames++;
            if(catched==true){
                
                match=true;
                while(match){
                    randomx=r.nextInt(15)*40+5;
                    randomy=r.nextInt(15)*40+5;
                    for(int i=0;i<p.size();++i){
                        if(p.get(i).x==Double.parseDouble(randomx.toString()) && p.get(i).y==Double.parseDouble(randomy.toString())){
                            break;
                        }
                        if(i==p.size()-1){
                            match=false;
                        }
                    }
                }
                predx=Double.parseDouble(randomx.toString());
                predy=Double.parseDouble(randomy.toString());
                gc.setStroke(Color.RED);
                gc.strokeOval(predx, predy, 30, 30);
                gc.setStroke(Color.BLUE);
                catched=false;
            }
            
            
            
            
        }
        @Override
        public void start(){
            super.start();
        }
        @Override
        public void stop(){
            super.stop();
        }
        
    }
    class Positions{
        public double x,y;
        public Positions(double xpos, double ypos){
            x=xpos;
            y=ypos;
        }
        
    }
   
    
}
