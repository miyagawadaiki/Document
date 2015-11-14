import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class Back extends Applet implements MouseListener,MouseMotionListener,ActionListener{

   Button button1,button2,button3,button4;

   int X0=10,X1=125;
   int Y0=55,Y1=70,Y2=160,Y3=240,Y4=305;

   int RX0=30,RX1=60,RX2=210,RX3=260;
   int RY0=225,RY1=240;

   int WIDTH=7;              //入力データの幅
   int HEIGHT=11;            //入力データの高さ
   int INPUT=WIDTH*HEIGHT;   //入力層の数（入力データ数）
   int HIDDEN=16;            //隠れ層の数
   int PATTERN=10;           //パターンの種類
   int OUTPUT=PATTERN;       //出力層の数（出力データ数）
   int OUTER_CYCLES=200;     //外部サイクル（一連のパターンの繰返し学習）の回数
   int INNER_CYCLES=200;     //内部サイクル（同一パターンの繰返し学習）の回数
   float ALPHA=1.2f;         //学習の加速係数
   float BETA=1.2f;          //シグモイド曲線の傾斜

   int[] sample_in=new int[INPUT];                  //学習用入力
   int[] written_in=new int[INPUT];                 //認識用手書き入力

   float[][] weight_ih=new float[INPUT][HIDDEN];    //入力層と隠れ層の間の重み係数
   float[] thresh_h=new float[HIDDEN];              //隠れ層の閾値
   float[] hidden_out=new float[HIDDEN];            //隠れ層の出力

   float[][] weight_ho=new float[HIDDEN][OUTPUT];   //隠れ層と出力層の間の重み係数
   float[] thresh_o=new float[OUTPUT];              //出力層の閾値
   float[] recog_out=new float[OUTPUT];             //認識出力（出力層の出力）

   int[] teach=new int[PATTERN];                    //教師信号





   boolean learning_flag;  //「学習モード」フラグ

   //学習用入力データの基となるパターン
   int[][] sample_array={{0,0,1,1,1,0,0,  //'0'
                         0,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,0,0,0,1,0,
                         0,0,1,1,1,0,0},
  
                        {0,0,0,1,0,0,0,  //'1'
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0},
 
                        {0,0,1,1,1,0,0,  //'2'
                         0,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,1,0,
                         0,0,0,0,1,0,0,
                         0,0,0,1,0,0,0,
                         0,0,1,0,0,0,0,
                         0,1,0,0,0,0,0,
                         1,1,1,1,1,1,1},
  
                        {0,0,1,1,1,0,0,  //'3'
                         0,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         0,0,0,0,0,1,0,
                         0,0,0,0,1,0,0,
                         0,0,0,0,0,1,0,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,0,0,0,1,0,
                         0,0,1,1,1,0,0},
 
                        {0,0,0,0,1,0,0,  //'4'
                         0,0,0,1,1,0,0,
                         0,0,1,0,1,0,0,
                         0,0,1,0,1,0,0,
                         0,1,0,0,1,0,0,
                         0,1,0,0,1,0,0,
                         1,0,0,0,1,0,0,
                         1,1,1,1,1,1,1,
                         0,0,0,0,1,0,0,
                         0,0,0,0,1,0,0,
                         0,0,0,0,1,0,0},
 
                        {1,1,1,1,1,1,1,  //'5'
                         1,0,0,0,0,0,0,
                         1,0,0,0,0,0,0,
                         1,0,0,0,0,0,0,
                         1,1,1,1,1,0,0,
                         0,0,0,0,0,1,0,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         1,0,0,0,0,1,0,
                         0,1,1,1,1,1,0},
 
                        {0,0,0,0,1,1,0,  //'6'
                         0,0,0,1,0,0,0,
                         0,0,1,0,0,0,0,
                         0,1,0,0,0,0,0,
                         0,1,0,0,0,0,0,
                         1,0,0,0,0,0,0,
                         1,0,1,1,1,0,0,
                         1,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         0,1,0,0,0,1,0,
                         0,0,1,1,1,0,0},
 
                        {1,1,1,1,1,1,1,  //'7'
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,1,0,
                         0,0,0,0,0,1,0,
                         0,0,0,0,1,0,0,
                         0,0,0,0,1,0,0,
                         0,0,0,1,0,0,0,
                         0,0,0,1,0,0,0,
                         0,0,1,0,0,0,0,
                         0,0,1,0,0,0,0},
 
                        {0,0,1,1,1,0,0,  //'8'
                         0,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,0,0,0,1,0,
                         0,0,1,1,1,0,0,
                         0,1,0,0,0,1,0,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,1,1,1,1,0},
                 
                        {0,1,1,1,1,1,0,  //'9'
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,1,1,1,1,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         0,0,0,0,0,0,1,
                         1,0,0,0,0,0,1,
                         0,1,1,1,1,1,0}};

   int[][] teach_array=new int[PATTERN][OUTPUT];  //パターンと出力すべき教師信号の比較表

   int x_new,y_new,x_old,y_old;           //手書き文字入力用座標


   public void init(){

      setBackground(Color.gray);

      //ボタンの設定
      add(button1=new Button("  再学習  "));
      add(button2=new Button(" 学習終了 "));
      add(button3=new Button("入力クリヤ"));
      add(button4=new Button("  認  識  "));
      button1.addActionListener(this);
      button2.addActionListener(this);
      button3.addActionListener(this);
      button4.addActionListener(this);

      //マウスの設定
      addMouseListener(this);
      addMouseMotionListener(this);

      //教師信号の設定
      for(int q=0;q<PATTERN;q++)
         for(int k=0;k<OUTPUT;k++){
            if(q==k) teach_array[q][k]=1;
            else     teach_array[q][k]=0;
         }

      //モードの初期設定
      learning_flag=true;

   }

   //------------------- ボタン関係のメソッド ------------------

   public void actionPerformed(ActionEvent ae){

      if(ae.getSource()==button1){      //「再学習」
         learning_flag=true;
         repaint();
      }
      if(ae.getSource()==button2){      //「学習終了」
         learning_flag=false;
         repaint();
      }
      if(ae.getSource()==button3){      //「入力クリヤ」
         if(!learning_flag)
            repaint();
      }
      if(ae.getSource()==button4){      //「認識」
         if(!learning_flag)
            recognizeCharacter();
      }

   }

   //---------- マウス関係のメソッド（手書き文字入力）----------

   public void mousePressed(MouseEvent me){
      int x=me.getX();
      int y=me.getY();
      if(!learning_flag && x>=RX1 && x<=RX1+WIDTH*10 && y>=RY1 && y<=RY1+HEIGHT*10){
         x_old=me.getX();
         y_old=me.getY();
         written_in[(y_old-RY1)/10*WIDTH+(x_old-RX1)/10]=1;
      }
   }

   public void mouseClicked(MouseEvent me){}
   public void mouseEntered(MouseEvent me){}
   public void mouseExited(MouseEvent me){}
   public void mouseReleased(MouseEvent me){}

   public void mouseDragged(MouseEvent me){
      int x=me.getX();
      int y=me.getY();
      if(!learning_flag && x>=RX1 && x<=RX1+WIDTH*10 && y>=RY1 && y<=RY1+HEIGHT*10){
         Graphics g=getGraphics(); 
         x_new=me.getX();
         y_new=me.getY();
         g.drawLine(x_old,y_old,x_new,y_new);
         x_old=x_new;
         y_old=y_new;
         written_in[(y_old-RY1)/10*WIDTH+(x_old-RX1)/10]=1;
      }
 
   }

   public void mouseMoved(MouseEvent me){}
 


   //---------- 起動時およびrepaint()で呼び出されるメソッド ----------
 
   public void paint(Graphics g){

      int i,j,k,p,q,r,x;

      String string;

      float outer_error;          //外部サイクルエラー累計
      float inner_error;          //内部サイクルエラー累計
      float temp_error;           //隠れ層の誤差の累計 

      //学習モードの背景
      if(learning_flag){
         g.setColor(new Color(255,255,192));
         g.fillRect(5,35,590,460);
         g.setColor(Color.black);
         g.drawString("学習モード",500,55);
      }

      //認識モードの背景
      else{
         g.setColor(new Color(192,255,255));
         g.fillRect(5,35,590,460);
         g.setColor(Color.black);
         g.drawString("認識モード",500,55);
      }

      //学習用パターンの表示
      g.drawString("使用している学習用パターン",X0,Y0);
      for(q=0;q<PATTERN;q++){
         x=56*q;
         for(j=0;j<HEIGHT;j++)
            for(i=0;i<WIDTH;i++){
               if(sample_array[q][WIDTH*j+i]==1)     g.setColor(Color.red);
               else                                  g.setColor(Color.cyan);
               g.fillRect(X0+x+6*i,Y1+6*j,5,5);
            }
      }
      g.setColor(Color.black);

      //-------------------------------------------------------------------
      //--------------------------- 学習モード ----------------------------
      //-------------------------------------------------------------------
      if(learning_flag){

         //閾値と重みの乱数設定
         for(j=0;j<HIDDEN;j++){
            thresh_h[j]=(float)Math.random()-0.5f;
            for(i=0;i<INPUT;i++)
               weight_ih[i][j]=(float)Math.random()-0.5f;
         }
         for(k=0;k<OUTPUT;k++){
            thresh_o[k]=(float)Math.random()-0.5f;
            for(j=0;j<HIDDEN;j++)
               weight_ho[j][k]=(float)Math.random()-0.5f;
         }

         //-------------------------- 学習 --------------------------

         for(p=0;p<OUTER_CYCLES;p++){     //外部サイクル

            outer_error=0.0f;         //外部二乗誤差のクリヤー

            for(q=0;q<PATTERN;q++){   //パターンの切り替え

               //パターンに対応した入力と教師信号の設定
               sample_in=sample_array[q];
               teach=teach_array[q];

               for(r=0;r<INNER_CYCLES;r++){   //内部サイクル

                  //順方向演算
                  forwardNeuralNet(sample_in,recog_out);       

                  //逆方向演算（バックプロパゲーション）
                  backwardNeuralNet();

               }

               //内部二乗誤差の計算
               inner_error=0.0f;   //内部二乗誤差のクリヤー
               for(k=0;k<OUTPUT;k++)
                  inner_error+=(teach[k]-recog_out[k])*(teach[k]-recog_out[k]);

               outer_error+=inner_error;   //外部二乗誤差への累加算

            }

            //外部サイクルの回数と外部二乗誤差の表示
            g.drawString("実行中の外部サイクルの回数と二乗誤差",X0,Y2);
            g.setColor(new Color(255,255,192));
            g.fillRect(X0+5,Y2+10,200,50);   //以前の表示を消去
            g.setColor(Color.black);
            g.drawString("OuterCycles="+String.valueOf(p),X0+10,Y2+25);
            g.drawString("TotalSquaredError="+String.valueOf(outer_error),X0+10,Y2+45);

         } 


         //--------------------- 学習結果の確認 ---------------------

         g.drawString("学習結果の確認",X0,Y3);
         for(k=0;k<OUTPUT;k++){
            g.drawString("Output",X1+45*k,Y3+25);
            g.drawString("  ["+String.valueOf(k)+"]",X1+5+45*k,Y3+40);
         }      

         for(q=0;q<PATTERN;q++){

            //入力パターンの設定
            sample_in=sample_array[q];

            //順方向演算
            forwardNeuralNet(sample_in,recog_out);

            //結果の表示
            g.setColor(Color.black);
            g.drawString("TestPattern["+String.valueOf(q)+"]",X0+10,Y4+20*q);
            for(k=0;k<OUTPUT;k++){
               if(recog_out[k]>0.99){        //99% より大は、赤で YES と表示
                  g.setColor(Color.red);
                  string="YES";
               }
               else if(recog_out[k]<0.01){   // 1% より小は、青で NO と表示
                  g.setColor(Color.blue);
                  string="NO ";
               }
               else{                         // 1% 以上 99% 以下は、黒で ? と表示
                  g.setColor(Color.black);
                  string=" ? ";
               }
               g.drawString(string,X1+10+45*k,Y4+20*q);
            }

         }
      }

      //-------------------------------------------------------------------
      //--------------------------- 認識モード ----------------------------
      //-------------------------------------------------------------------
      else{
         g.setColor(Color.black);
         g.drawString("マウスで数字を描いて下さい",RX0,RY0);
         g.drawRect(RX1-1,RY1-1,WIDTH*10+2,HEIGHT*10+2);     //外枠
         g.setColor(Color.gray);
         for(j=1;j<HEIGHT;j++)
            g.drawLine(RX1,RY1+10*j,RX1+WIDTH*10,RY1+10*j);  //横方向区切り
         for(i=1;i<WIDTH;i++)
            g.drawLine(RX1+10*i,RY1,RX1+10*i,RY1+HEIGHT*10);  //縦方向区切り
         for(i=0;i<INPUT;i++)
            written_in[i]=0;     //手書き入力データのクリヤ
      }

   }

   //順方向演算のメソッド
   public void forwardNeuralNet(int[] input,float[] output){

      float[] out=new float[OUTPUT];
      float[] hidden=new float[HIDDEN];

      //隠れ層出力の計算
      for(int j=0;j<HIDDEN;j++){
         hidden[j]=-thresh_h[j];
         for(int i=0;i<INPUT;i++)
            hidden[j]+=input[i]*weight_ih[i][j];
         hidden_out[j]=sigmoid(hidden[j]);
      }

      //出力層出力の計算
      for(int k=0;k<OUTPUT;k++){
         out[k]=-thresh_o[k];
         for(int j=0;j<HIDDEN;j++)
            out[k]+=hidden_out[j]*weight_ho[j][k];
         output[k]=sigmoid(out[k]);
      }

   }

   //逆方向演算のメソッド
   public void backwardNeuralNet(){

      int i,j,k;

      float[] output_error=new float[OUTPUT];       //出力層の誤差
      float[] hidden_error=new float[HIDDEN];       //隠れ層の誤差

      float temp_error;

      //出力層の誤差の計算
      for(k=0;k<OUTPUT;k++)
         output_error[k]=(teach[k]-recog_out[k])*recog_out[k]*(1.0f-recog_out[k]);

      //隠れ層の誤差の計算
      for(j=0;j<HIDDEN;j++){
         temp_error=0.0f;
         for(k=0;k<OUTPUT;k++)
            temp_error+=output_error[k]*weight_ho[j][k];
         hidden_error[j]=hidden_out[j]*(1.0f-hidden_out[j])*temp_error;
      }

      //重みの補正
      for(k=0;k<OUTPUT;k++)
         for(j=0;j<HIDDEN;j++)
            weight_ho[j][k]+=ALPHA*output_error[k]*hidden_out[j];
      for(j=0;j<HIDDEN;j++)
         for(i=0;i<INPUT;i++)
            weight_ih[i][j]+=ALPHA*hidden_error[j]*sample_in[i];

      //閾値の補正
      for(k=0;k<OUTPUT;k++)
         thresh_o[k]-=ALPHA*output_error[k];
      for(j=0;j<HIDDEN;j++)
         thresh_h[j]-=ALPHA*hidden_error[j];

   }
  
   //Sigmoid関数を計算するメソッド
   public float sigmoid(float x){

      return 1.0f/(1.0f+(float)Math.exp(-BETA*x));

   }

   //入力文字を認識するメソッド
   public void recognizeCharacter(){

      Graphics g=getGraphics();
      String string;

      //順方向演算
      forwardNeuralNet(written_in,recog_out);

      //結果の表示
      for(int k=0;k<OUTPUT;k++){
          g.setColor(Color.black);
          g.drawString(String.valueOf(k)+"である",RX2,RY1+20*k);
          if(recog_out[k]>0.8f)  g.setColor(Color.red);
          else                   g.setColor(Color.black);

          g.fillRect(RX3,RY1-10+20*k,(int)(200*recog_out[k]),10);
          g.drawString(String.valueOf((int)(100*recog_out[k]+0.5f))+"%",RX3+(int)(200*recog_out[k])+10,RY1+20*k);
       }

   }

}



