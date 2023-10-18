/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.micropupsichy.memgenerator;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 *
 * @author Даниил
 * @author Илья
 * @author Дима
 * @author Антон
 */
public class MemGenerator {
    /*path, text, [position], [fontName], [FontSize], [fontColor], [fontStyle]*/
    private static String path, text, position="bottom", fontName="Arial";
    private int fontSize=50, fontColor=0, fontStyle=0;
    
    public static void main(String[] args) {
        MemGenerator mmg = new MemGenerator();
        try {
            switch (args[0]){
                case "help":
                    System.out.println(mmg.getHelp());
                    break;
                case "mem":
                    path = args[1];
                    text = args[2];
                    String[] optionalParams = Arrays.copyOfRange(args, 
                            3, args.length);
                    mmg.getOptionalParams(optionalParams);
                    System.gc();
                    GetImage image = mmg.new GetImage();
                    break;
                default:
                    System.out.println("Error: invalid arguments! Please, input 'help' for command arguments.");
            }
        } catch (IOException ex) {
            System.out.println("Error: "+ex.getMessage());
        } catch (ArrayIndexOutOfBoundsException exp){
            System.out.println("Error: no input arguments! Please, input 'help' for command arguments.");
        }
    }
    
    private void getOptionalParams(String[] optionalParams){
        for(String optionalParam : optionalParams){
            String[] current = optionalParam.split("=");
            switch (current[0]){
                case "position":
                    position = current[1];
                break;
                case "fontName":
                    fontName = current[1];
                break;
                case "fontSize":
                    fontSize = Integer.parseInt(current[1]);
                break;
                case "fontColor":
                    fontColor = Integer.parseInt(current[1]);
                break;
                case "fontStyle":
                    fontStyle = Integer.parseInt(current[1]);
                break;
                default:
                    System.out.println("Error: "+current[0]+" is invalid arguments! Please, input 'help' for command arguments.");
            }
        }
    }
    
    private String getHelp(){
        try(FileInputStream stream= new FileInputStream("help.txt")){
            StringBuilder area;
            try(BufferedReader buff = new BufferedReader(new InputStreamReader(stream, "UTF-8"))){
                String line;
                area = new StringBuilder();
                while ((line=buff.readLine())!=null){
                    area.append(line);
                }
            }
            return area.toString();
        }catch (IOException ex){
            System.out.println("Error: "+ex.getMessage());
        }
        return null;
    }

        public class GetImage{
            public BufferedImage image;
            private Graphics g;
            public GetImage() throws IOException {
                this.image = ImageIO.read(new File(System.getProperty("user.dir")+"//"+path));
                g = image.getGraphics();
                setParamsFont();
                painter();
                saveImage();
            }
            
            private void painter(){
                FontMetrics metrics = g.getFontMetrics(g.getFont());
                int positionX = (image.getWidth() - metrics.stringWidth(text)) / 2;
                int positionY;
                switch (position){
                    case "center":
                        positionY = (image.getHeight() - metrics.getHeight()) / 2 + metrics.getAscent();
                    break;
                    case "bottom":
                        positionY = image.getHeight() - metrics.getAscent();
                    break;
                    case "top":
                        positionY = metrics.getAscent();
                    break;
                    default:
                        positionY = image.getHeight() - metrics.getAscent();
                }
                g.drawString(text, positionX, positionY);
                g.dispose();
            }
            
            private void setParamsFont(){
                if (fontColor==0){
                    g.setColor(Color.WHITE);
                }else if (fontColor==1){
                    g.setColor(Color.BLACK);
                }
                g.setFont(new Font(fontName, fontStyle, fontSize));
            }
            
            private void saveImage() throws IOException{
                ImageIO.write(image, "png", new File(System.getProperty("user.dir")+"//"+path.split("\\.")[0]+"-memasik.png"));
                System.out.println("Save completed!");
            }
        }
}
