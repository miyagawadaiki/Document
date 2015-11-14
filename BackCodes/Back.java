import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class Back extends Applet implements MouseListener,MouseMotionListener,ActionListener{

   Button button1,button2,button3,button4;

   int X0=10,X1=125;
   int Y0=55,Y1=70,Y2=160,Y3=240,Y4=305;

   int RX0=30,RX1=60,RX2=210,RX3=260;
   int RY0=225,RY1=240;

   int WIDTH=7;              //���̓f�[�^�̕�
   int HEIGHT=11;            //���̓f�[�^�̍���
   int INPUT=WIDTH*HEIGHT;   //���͑w�̐��i���̓f�[�^���j
   int HIDDEN=16;            //�B��w�̐�
   int PATTERN=10;           //�p�^�[���̎��
   int OUTPUT=PATTERN;       //�o�͑w�̐��i�o�̓f�[�^���j
   int OUTER_CYCLES=200;     //�O���T�C�N���i��A�̃p�^�[���̌J�Ԃ��w�K�j�̉�
   int INNER_CYCLES=200;     //�����T�C�N���i����p�^�[���̌J�Ԃ��w�K�j�̉�
   float ALPHA=1.2f;         //�w�K�̉����W��
   float BETA=1.2f;          //�V�O���C�h�Ȑ��̌X��

   int[] sample_in=new int[INPUT];                  //�w�K�p����
   int[] written_in=new int[INPUT];                 //�F���p�菑������

   float[][] weight_ih=new float[INPUT][HIDDEN];    //���͑w�ƉB��w�̊Ԃ̏d�݌W��
   float[] thresh_h=new float[HIDDEN];              //�B��w��臒l
   float[] hidden_out=new float[HIDDEN];            //�B��w�̏o��

   float[][] weight_ho=new float[HIDDEN][OUTPUT];   //�B��w�Əo�͑w�̊Ԃ̏d�݌W��
   float[] thresh_o=new float[OUTPUT];              //�o�͑w��臒l
   float[] recog_out=new float[OUTPUT];             //�F���o�́i�o�͑w�̏o�́j

   int[] teach=new int[PATTERN];                    //���t�M��





   boolean learning_flag;  //�u�w�K���[�h�v�t���O

   //�w�K�p���̓f�[�^�̊�ƂȂ�p�^�[��
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

   int[][] teach_array=new int[PATTERN][OUTPUT];  //�p�^�[���Əo�͂��ׂ����t�M���̔�r�\

   int x_new,y_new,x_old,y_old;           //�菑���������͗p���W


   public void init(){

      setBackground(Color.gray);

      //�{�^���̐ݒ�
      add(button1=new Button("  �Ċw�K  "));
      add(button2=new Button(" �w�K�I�� "));
      add(button3=new Button("���̓N����"));
      add(button4=new Button("  �F  ��  "));
      button1.addActionListener(this);
      button2.addActionListener(this);
      button3.addActionListener(this);
      button4.addActionListener(this);

      //�}�E�X�̐ݒ�
      addMouseListener(this);
      addMouseMotionListener(this);

      //���t�M���̐ݒ�
      for(int q=0;q<PATTERN;q++)
         for(int k=0;k<OUTPUT;k++){
            if(q==k) teach_array[q][k]=1;
            else     teach_array[q][k]=0;
         }

      //���[�h�̏����ݒ�
      learning_flag=true;

   }

   //------------------- �{�^���֌W�̃��\�b�h ------------------

   public void actionPerformed(ActionEvent ae){

      if(ae.getSource()==button1){      //�u�Ċw�K�v
         learning_flag=true;
         repaint();
      }
      if(ae.getSource()==button2){      //�u�w�K�I���v
         learning_flag=false;
         repaint();
      }
      if(ae.getSource()==button3){      //�u���̓N�����v
         if(!learning_flag)
            repaint();
      }
      if(ae.getSource()==button4){      //�u�F���v
         if(!learning_flag)
            recognizeCharacter();
      }

   }

   //---------- �}�E�X�֌W�̃��\�b�h�i�菑���������́j----------

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
 


   //---------- �N���������repaint()�ŌĂяo����郁�\�b�h ----------
 
   public void paint(Graphics g){

      int i,j,k,p,q,r,x;

      String string;

      float outer_error;          //�O���T�C�N���G���[�݌v
      float inner_error;          //�����T�C�N���G���[�݌v
      float temp_error;           //�B��w�̌덷�̗݌v 

      //�w�K���[�h�̔w�i
      if(learning_flag){
         g.setColor(new Color(255,255,192));
         g.fillRect(5,35,590,460);
         g.setColor(Color.black);
         g.drawString("�w�K���[�h",500,55);
      }

      //�F�����[�h�̔w�i
      else{
         g.setColor(new Color(192,255,255));
         g.fillRect(5,35,590,460);
         g.setColor(Color.black);
         g.drawString("�F�����[�h",500,55);
      }

      //�w�K�p�p�^�[���̕\��
      g.drawString("�g�p���Ă���w�K�p�p�^�[��",X0,Y0);
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
      //--------------------------- �w�K���[�h ----------------------------
      //-------------------------------------------------------------------
      if(learning_flag){

         //臒l�Əd�݂̗����ݒ�
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

         //-------------------------- �w�K --------------------------

         for(p=0;p<OUTER_CYCLES;p++){     //�O���T�C�N��

            outer_error=0.0f;         //�O�����덷�̃N�����[

            for(q=0;q<PATTERN;q++){   //�p�^�[���̐؂�ւ�

               //�p�^�[���ɑΉ��������͂Ƌ��t�M���̐ݒ�
               sample_in=sample_array[q];
               teach=teach_array[q];

               for(r=0;r<INNER_CYCLES;r++){   //�����T�C�N��

                  //���������Z
                  forwardNeuralNet(sample_in,recog_out);       

                  //�t�������Z�i�o�b�N�v���p�Q�[�V�����j
                  backwardNeuralNet();

               }

               //�������덷�̌v�Z
               inner_error=0.0f;   //�������덷�̃N�����[
               for(k=0;k<OUTPUT;k++)
                  inner_error+=(teach[k]-recog_out[k])*(teach[k]-recog_out[k]);

               outer_error+=inner_error;   //�O�����덷�ւ̗݉��Z

            }

            //�O���T�C�N���̉񐔂ƊO�����덷�̕\��
            g.drawString("���s���̊O���T�C�N���̉񐔂Ɠ��덷",X0,Y2);
            g.setColor(new Color(255,255,192));
            g.fillRect(X0+5,Y2+10,200,50);   //�ȑO�̕\��������
            g.setColor(Color.black);
            g.drawString("OuterCycles="+String.valueOf(p),X0+10,Y2+25);
            g.drawString("TotalSquaredError="+String.valueOf(outer_error),X0+10,Y2+45);

         } 


         //--------------------- �w�K���ʂ̊m�F ---------------------

         g.drawString("�w�K���ʂ̊m�F",X0,Y3);
         for(k=0;k<OUTPUT;k++){
            g.drawString("Output",X1+45*k,Y3+25);
            g.drawString("  ["+String.valueOf(k)+"]",X1+5+45*k,Y3+40);
         }      

         for(q=0;q<PATTERN;q++){

            //���̓p�^�[���̐ݒ�
            sample_in=sample_array[q];

            //���������Z
            forwardNeuralNet(sample_in,recog_out);

            //���ʂ̕\��
            g.setColor(Color.black);
            g.drawString("TestPattern["+String.valueOf(q)+"]",X0+10,Y4+20*q);
            for(k=0;k<OUTPUT;k++){
               if(recog_out[k]>0.99){        //99% ����́A�Ԃ� YES �ƕ\��
                  g.setColor(Color.red);
                  string="YES";
               }
               else if(recog_out[k]<0.01){   // 1% ��菬�́A�� NO �ƕ\��
                  g.setColor(Color.blue);
                  string="NO ";
               }
               else{                         // 1% �ȏ� 99% �ȉ��́A���� ? �ƕ\��
                  g.setColor(Color.black);
                  string=" ? ";
               }
               g.drawString(string,X1+10+45*k,Y4+20*q);
            }

         }
      }

      //-------------------------------------------------------------------
      //--------------------------- �F�����[�h ----------------------------
      //-------------------------------------------------------------------
      else{
         g.setColor(Color.black);
         g.drawString("�}�E�X�Ő�����`���ĉ�����",RX0,RY0);
         g.drawRect(RX1-1,RY1-1,WIDTH*10+2,HEIGHT*10+2);     //�O�g
         g.setColor(Color.gray);
         for(j=1;j<HEIGHT;j++)
            g.drawLine(RX1,RY1+10*j,RX1+WIDTH*10,RY1+10*j);  //��������؂�
         for(i=1;i<WIDTH;i++)
            g.drawLine(RX1+10*i,RY1,RX1+10*i,RY1+HEIGHT*10);  //�c������؂�
         for(i=0;i<INPUT;i++)
            written_in[i]=0;     //�菑�����̓f�[�^�̃N����
      }

   }

   //���������Z�̃��\�b�h
   public void forwardNeuralNet(int[] input,float[] output){

      float[] out=new float[OUTPUT];
      float[] hidden=new float[HIDDEN];

      //�B��w�o�͂̌v�Z
      for(int j=0;j<HIDDEN;j++){
         hidden[j]=-thresh_h[j];
         for(int i=0;i<INPUT;i++)
            hidden[j]+=input[i]*weight_ih[i][j];
         hidden_out[j]=sigmoid(hidden[j]);
      }

      //�o�͑w�o�͂̌v�Z
      for(int k=0;k<OUTPUT;k++){
         out[k]=-thresh_o[k];
         for(int j=0;j<HIDDEN;j++)
            out[k]+=hidden_out[j]*weight_ho[j][k];
         output[k]=sigmoid(out[k]);
      }

   }

   //�t�������Z�̃��\�b�h
   public void backwardNeuralNet(){

      int i,j,k;

      float[] output_error=new float[OUTPUT];       //�o�͑w�̌덷
      float[] hidden_error=new float[HIDDEN];       //�B��w�̌덷

      float temp_error;

      //�o�͑w�̌덷�̌v�Z
      for(k=0;k<OUTPUT;k++)
         output_error[k]=(teach[k]-recog_out[k])*recog_out[k]*(1.0f-recog_out[k]);

      //�B��w�̌덷�̌v�Z
      for(j=0;j<HIDDEN;j++){
         temp_error=0.0f;
         for(k=0;k<OUTPUT;k++)
            temp_error+=output_error[k]*weight_ho[j][k];
         hidden_error[j]=hidden_out[j]*(1.0f-hidden_out[j])*temp_error;
      }

      //�d�݂̕␳
      for(k=0;k<OUTPUT;k++)
         for(j=0;j<HIDDEN;j++)
            weight_ho[j][k]+=ALPHA*output_error[k]*hidden_out[j];
      for(j=0;j<HIDDEN;j++)
         for(i=0;i<INPUT;i++)
            weight_ih[i][j]+=ALPHA*hidden_error[j]*sample_in[i];

      //臒l�̕␳
      for(k=0;k<OUTPUT;k++)
         thresh_o[k]-=ALPHA*output_error[k];
      for(j=0;j<HIDDEN;j++)
         thresh_h[j]-=ALPHA*hidden_error[j];

   }
  
   //Sigmoid�֐����v�Z���郁�\�b�h
   public float sigmoid(float x){

      return 1.0f/(1.0f+(float)Math.exp(-BETA*x));

   }

   //���͕�����F�����郁�\�b�h
   public void recognizeCharacter(){

      Graphics g=getGraphics();
      String string;

      //���������Z
      forwardNeuralNet(written_in,recog_out);

      //���ʂ̕\��
      for(int k=0;k<OUTPUT;k++){
          g.setColor(Color.black);
          g.drawString(String.valueOf(k)+"�ł���",RX2,RY1+20*k);
          if(recog_out[k]>0.8f)  g.setColor(Color.red);
          else                   g.setColor(Color.black);

          g.fillRect(RX3,RY1-10+20*k,(int)(200*recog_out[k]),10);
          g.drawString(String.valueOf((int)(100*recog_out[k]+0.5f))+"%",RX3+(int)(200*recog_out[k])+10,RY1+20*k);
       }

   }

}



