package com.jtl.quiz.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import javax.swing.JPanel;

import com.jtl.quiz.main.ClientMain;
import com.jtl.quiz.model.Heart;
import com.jtl.quiz.model.Option;
import com.jtl.quiz.model.Player;

public class GraphicsPanel extends JPanel{
	
	 @Override
     public void paintComponent(Graphics g) {
		 try{
         super.paintComponent(g);
         g.setColor(Color.BLACK);
         g.drawString("Status : "  + GraphicsManager.getStatus(), 350, 20);
         g.drawString("Playing As : " + GraphicsManager.getCurrentPlayer(), 600, 20);
         int yStart = Player.STATIC_OFFSET_Y;
         for(Player player : Player.allPlayers){
        	 g.setColor(Color.BLACK);
        	 g.drawString(player.name, 40, yStart);
        	 g.setColor(Color.RED);
        	 for(int i = 0; i < player.hearts.size(); i++){
       		 g.drawImage(GraphicsManager.heart, Heart.STATIC_OFFSET_X + (i*Heart.OFFSET_X), yStart + Heart.STATIC_OFFSET_Y, Heart.SIZE_X, Heart.SIZE_Y, new ImageObserver() {
				
				@Override
				public boolean imageUpdate(Image img, int infoflags, int x, int y,
						int width, int height) {
					// TODO Auto-generated method stub
					return false;
				}
			});
        	 }
        	 yStart += Player.OFFSET_Y;
         }
         
         if(!(ClientMain.currentOptions == null)){
        	 for(int i = 0; i < ClientMain.currentOptions.options.size(); i++){
        		 if(ClientMain.currentOptions.options.get(i).equals(ClientMain.currentOption)){
        		 g.setColor(Color.BLUE);
        		 g.fillRect(Option.STATIC_OFFSET_X + Option.OFFSET_X * i, Option.STATIC_Y, Option.STATIC_OFFSET_X, Option.SIZE_Y);
        		 }else{
        			 g.setColor(Color.BLUE);
            		 g.drawRect(Option.STATIC_OFFSET_X + Option.OFFSET_X * i, Option.STATIC_Y, Option.STATIC_OFFSET_X, Option.SIZE_Y);
            		 g.setColor(Color.GRAY);
            		 g.fillRect(Option.STATIC_OFFSET_X + Option.OFFSET_X * i, Option.STATIC_Y, Option.STATIC_OFFSET_X, Option.SIZE_Y);
        		 }
        		 g.setColor(Color.WHITE);
        		 g.drawString(ClientMain.currentOptions.options.get(i).text, 10 + Option.STATIC_OFFSET_X + Option.OFFSET_X * i, Option.STATIC_Y + 20);
        	 }
        	 g.setColor(Color.BLACK);
    		 if(ClientMain.finalOption == null){
    			 g.drawImage(GraphicsManager.confirm, GraphicsManager.CONFIRM_X, GraphicsManager.CONFIRM_Y, this);
    		 }
         }
		 }catch(Exception e){
			 
		 }
     }

}
