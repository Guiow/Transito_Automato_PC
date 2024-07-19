/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 18/06/2024 - 15:57
* Ultima alteracao.: 02/07/2024 - 19:43
* Nome.............: Transito Automato
* Funcao...........: movimenta a imagem do carro em paralelo/pseudoparalelo com outros
*************************************************************** */
package model;

import java.util.concurrent.Semaphore;
import javafx.scene.image.ImageView;
import javafx.application.Platform;

public class Carro extends Thread
{
  private ImageView imagemDoCarro;
  
  private final int TAMANHO_DO_PERCURSO;
  private int[] percurso;//contem a informacao das acoes do carro 
  private Semaphore[] semaforosDoCarro;//semaforos que serao utilizados pelo carro

  private boolean estaParado;//verifica se esta parado
  private Semaphore parar;//semaforo que para o carro quando a velocidade for 0
  private int velocidade;
  private final int VALOR_DE_INCREMENTO = 2;
  
  private int incrementoDeRotacao;//usado para rotacionar o carro aos poucos
  private int incrementoLayoutX;//usado para mover o carro na horizontal
  private int incrementoLayoutY;//usado para mover o carro na vertical
  private int sentidoAtual;//define o sentido do carro sendo 0 esquerda, 1 cima, 2 direita, 3 baixo
  
  /* ***************************************************************
  * Metodo: construtor
  * Funcao: inicializa as variaveis necessarias
  * Parametros: urlDaImagem = caminho da imagem do carro, percurso = vetor com as informacoes que descrevem as acoes da thread
                configuracaoInicial = contem 3 valores sentido, posicao X e Y iniciais do carro,
                semaforosDoCarro = vetor de semaforos que serao utilizados pelo carro
  * Retorno: nenhum
  *************************************************************** */
  public Carro(String urlDaImagem, int[] percurso, int[] configuracaoInicial, Semaphore[] semaforosDoCarro)
  {
    this.percurso = percurso;
    this.semaforosDoCarro = semaforosDoCarro;
    TAMANHO_DO_PERCURSO = percurso.length;
 
    imagemDoCarro = new ImageView(urlDaImagem);//inicializa e posiciona a imagem
    imagemDoCarro.setLayoutX(configuracaoInicial[1]);
    imagemDoCarro.setLayoutY(configuracaoInicial[2]);
    
    sentidoAtual = configuracaoInicial[0];//inicializa com sentido inicial
    incrementoLayoutX = sentidoAtual == 0 ? -VALOR_DE_INCREMENTO : VALOR_DE_INCREMENTO;//positivo ou negativo de acordo com o sentido
    
    parar = new Semaphore(0);
    velocidade = 9;
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: run
  * Funcao: executa o metodo de movimento infinitamente
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  @Override
  public void run()
  {
    try
    {
      while (true)
      {
        int sentidoInicial = sentidoAtual;//muda a rotacao para inicial quando o carro completa o circuito
        Platform.runLater(() -> imagemDoCarro.setRotate(sentidoInicial * 90));
        movimento();
      }//fim do while
    }//fim do try
    catch(InterruptedException ignora){}
  }//fim do run
  
  /* ***************************************************************
  * Metodo: movimento
  * Funcao: movimenta o carro sem que haja colisao seguindo as acoes fornecidas pelo vetor percurso
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void movimento()
    throws InterruptedException
  {
    boolean alternadorDeRuaHV = true;//o carro se movimenta na rua horizontal quando true, quando false vertical
    int indiceDoSemaforo = 0;//indice que ira ser incrementado cada vez que o carro adquire um semaforo
    int comprimentoDaRua;
    
    for (int indiceDoPercurso = 0; indiceDoPercurso < TAMANHO_DO_PERCURSO; indiceDoPercurso++)
    {
      comprimentoDaRua = 66;//valor exato para que se percorra corretamente a rua
      
      switch (percurso[indiceDoPercurso])
      {
        case 0:
        case 1:
          mudarDirecao(percurso[indiceDoPercurso]);//caso 0 ou 1 o carro vira para direita ou para esquerda, respectivamente
          alternadorDeRuaHV = !alternadorDeRuaHV;
          comprimentoDaRua -= 36;//quantidade percorrida pelo movimento de rotacao
          
        case 2:
          if (alternadorDeRuaHV)
            percorreRuaHorizontal(comprimentoDaRua);//percorre rua horizontal
          else
            percorreRuaVertical(comprimentoDaRua);//percorre rua vertical
          break;
          
        case 3:
          semaforosDoCarro[indiceDoSemaforo++].acquire();//adquire semaforo
          break;
          
        case 4:
        case 5:
        case 7:
          semaforosDoCarro[semaforosDoCarro.length - percurso[indiceDoPercurso] + 3].release();//libera semaforos iniciais
          break;
          
        default:
          semaforosDoCarro[indiceDoSemaforo - percurso[indiceDoPercurso] + 7].release();//libera semaforos
      }//fim do switch
    }//fim do for
  }//fim do movimento
  
  
  /* ***************************************************************
  * Metodo: mudarDirecao
  * Funcao: altera as variaveis de incremento e sentido para o carro se movimentar em outra direcao
  * Parametros: curva = indica se o carro esta virando para esquerda ou para direita
  * Retorno: void
  *************************************************************** */
  private void mudarDirecao(int curva)
    throws InterruptedException
  {
    boolean virarPara = curva == 0;//true = direita, false = esquerda
    switch (sentidoAtual)//autualiza as variaveis de incremento de acordo com o sentido e curva
    {
      case 0://sentido atual para esquerda
        incrementoLayoutX = -VALOR_DE_INCREMENTO;
        incrementoLayoutY = virarPara ? -VALOR_DE_INCREMENTO : VALOR_DE_INCREMENTO;
        break;
      case 1://sentido atual para cima
        incrementoLayoutY = -VALOR_DE_INCREMENTO;
        incrementoLayoutX = virarPara ? VALOR_DE_INCREMENTO : -VALOR_DE_INCREMENTO;
        break;
        
      case 2://sentido atual para direita
        incrementoLayoutX = VALOR_DE_INCREMENTO;
        incrementoLayoutY = virarPara ? VALOR_DE_INCREMENTO : -VALOR_DE_INCREMENTO;
        break;
      
      case 3://sentido atual para baixo
        incrementoLayoutY = VALOR_DE_INCREMENTO;
        incrementoLayoutX = virarPara ? -VALOR_DE_INCREMENTO : VALOR_DE_INCREMENTO;
        break; 
    }//fim do switch
    
    if (virarPara)//autualiza o incremento de rotacao e o novo sentido
    {
      incrementoDeRotacao = 5;
      sentidoAtual = (sentidoAtual + 1) % 4;
    }//fim do if
    else
    {
      incrementoDeRotacao = -5;
      sentidoAtual = (sentidoAtual + 4 - 1) % 4;
    }//fim do else
    
    movimentoDeRotacao();//apos autualizar as variaveis rotaciona o carro
  }//fim do mudarDirecao
  
  /* ***************************************************************
  * Metodo: movimentoDeRotacao
  * Funcao: realiza o movimento de rotacao do carro
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void movimentoDeRotacao()
    throws InterruptedException
  {
    for (int distancia = 0; distancia < 18; distancia++)//rotaciona o carro progressivamente
    {
      verificarPause();
      Platform.runLater(() -> 
      {
        imagemDoCarro.setLayoutX(imagemDoCarro.getLayoutX() + incrementoLayoutX);
        imagemDoCarro.setLayoutY(imagemDoCarro.getLayoutY() + incrementoLayoutY);
        imagemDoCarro.setRotate(imagemDoCarro.getRotate() + incrementoDeRotacao);
      });
    }//fim do for
  }//fim do movimentoDeRotacao
  
  /* ***************************************************************
  * Metodo: percorreRuaHorizontal
  * Funcao: movimenta o carro horizontalmente pela pista
  * Parametros: distancia = distancia que falta ate o final da rua
  * Retorno: void
  *************************************************************** */
  private void percorreRuaHorizontal(int distancia)
    throws InterruptedException
  {
    for (int percorrerDistancia = distancia; percorrerDistancia > 0; percorrerDistancia--)//movimenta progressivamente
    {
      verificarPause();
      Platform.runLater(() -> imagemDoCarro.setLayoutX(imagemDoCarro.getLayoutX() + incrementoLayoutX));
    }//fim do for
  }//fim do percorreRuaHorizontal
  
  /* ***************************************************************
  * Metodo: percorreRuaVertical
  * Funcao: movimenta o carro verticalmente pela pista
  * Parametros: distancia = distancia que falta ate o final da rua
  * Retorno: void
  *************************************************************** */
  private void percorreRuaVertical(int distancia)
    throws InterruptedException
  {  
    for (int percorrerDistancia = distancia; percorrerDistancia > 0; percorrerDistancia--)//movimenta progressivamente
    {
      verificarPause();
      Platform.runLater(() -> imagemDoCarro.setLayoutY(imagemDoCarro.getLayoutY() + incrementoLayoutY));
    }//fim do for
  }//fim do percorreRuaVertical
  
  /* ***************************************************************
  * Metodo: setVelocidade
  * Funcao: gerencia a velocidade do carro e o para caso necessario
  * Parametros: velocidade = nova velocidade que ira sobrescrever a atual
  * Retorno: void
  *************************************************************** */
  public void setVelocidade(int velocidade)
  {
    if (estaParado && velocidade != 0)//retoma o movimento do carro caso esteja parado
    {
      estaParado = false;
      parar.release();
    }//fim do if
     
    else if (velocidade == 0)//altera a variavel para parar o carro
      estaParado = true;
    
    else
      this.velocidade = 22 - velocidade;//altera velocidade
  }//fim do setVelocidade
  
  /* ***************************************************************
  * Metodo: verificarPause
  * Funcao: verifica constantemente se o carro foi pausado e aplicau tempo de espera na thread de acordo com a velocidade
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void verificarPause()
    throws InterruptedException
  {
    if (estaParado)//verifica se deve parar o carro
      parar.acquire();
      
    Thread.sleep(velocidade);//tempo de espera
  }//fim do verificarPause

  /* ***************************************************************
  * Metodo: getImagemDoCarro
  * Funcao: retorna a referencia da imagem do carro
  * Parametros: nenhum
  * Retorno: ImageView = imagem do carro
  *************************************************************** */
  public ImageView getImagemDoCarro()
  {
    return imagemDoCarro;
  }//fim do getImagemDoCarro
}//fim da classe Carro
