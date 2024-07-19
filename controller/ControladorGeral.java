/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 18/06/2024 - 14:54
* Ultima alteracao.: 04/07/2024 - 20:33
* Nome.............: Transito Automato
* Funcao...........: Inicia o programa, incializando os controles e os paineis do programa, tambem trata qualquer tipo de evento acionado
                     pelo usuario
*************************************************************** */
package controller;

import model.*;

import javafx.scene.image.ImageView;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.control.Tooltip;

public class ControladorGeral extends Pane
{
  private Pane painelDeControle;//painel onde os controles irao ficar
  private Slider[] slidersDeVelocidade;//altera a velocidade das threads carro
  private CheckBox[] visualizadoresDePercurso;//altera a visibilidade do trajeto do percurso
  private Button botaoReset;//reseta o programa
  
  private GerenciadorDeCarros gerenciador;//gerencia as threads carro
  
  private final int QUANTIDADE_DE_CARROS = 8;
  
  /* ***************************************************************
  * Metodo: construtor
  * Funcao: inicializa as variaveis necessarias para iniciar o programa
  * Parametros: nenhum
  * Retorno: nenhum
  *************************************************************** */
  public ControladorGeral()
  {
    gerenciador = new GerenciadorDeCarros(QUANTIDADE_DE_CARROS);
    slidersDeVelocidade = new Slider[QUANTIDADE_DE_CARROS];
    visualizadoresDePercurso = new CheckBox[QUANTIDADE_DE_CARROS];
    
    painelDeControle = new Pane();
    botaoReset = new Button();
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: inicializarPrograma
  * Funcao: inicializa o programa e configura as variaveis de controle
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void inicializarPrograma()
  {
    getStylesheets().add("style/stylePrincipal.css");//adiciona o arquivo css ao root
    getChildren().add(new ImageView("images/Background.jpg"));
    
    configuraSliders();
    configuraCheckBox();
    configuraBotaoReset();
    getChildren().add(painelDeControle);
    getChildren().add(gerenciador.configuraPainelPrincipal());//adiciona o painel principal ao root
    
    gerenciador.inicializarThreads();//inicializa as threads
  }//fim do inicializarPrograma
  
  /* ***************************************************************
  * Metodo: configuraSliders
  * Funcao: cria e configura todos os sliders de velocidade
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void configuraSliders()
  {
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)
    {
      slidersDeVelocidade[indice] = new Slider();
      
      slidersDeVelocidade[indice].setMax(20);//configura os valores do slider
      slidersDeVelocidade[indice].setValue(10);
      
      slidersDeVelocidade[indice].setTooltip(new Tooltip("Altera Velocidade Do Kart"));//informa o uso do slider
      slidersDeVelocidade[indice].setPrefWidth(150);//configura a posicao e o tamanho do slider
      slidersDeVelocidade[indice].setLayoutY(60 + indice * 80);
      slidersDeVelocidade[indice].setLayoutX(30);
      slidersDeVelocidade[indice].setId(String.format("sliderCarro%d", indice));//adiciona um id css de acordo com o indice
      
      int indiceDoCarro = indice;//adiciona listeners ao slider
      slidersDeVelocidade[indice].setOnMouseDragged(event -> gerenciador.alterarVelocidade(indiceDoCarro, 
        (int) slidersDeVelocidade[indiceDoCarro].getValue()));
      slidersDeVelocidade[indice].setOnMouseClicked(event -> gerenciador.alterarVelocidade(indiceDoCarro, 
        (int) slidersDeVelocidade[indiceDoCarro].getValue()));
    }//fim do for
    
    painelDeControle.getChildren().addAll(slidersDeVelocidade);//adiciona o slider ao painel de controle
  }//fim do configuraSliders
  
  /* ***************************************************************
  * Metodo: configuraCheckBox
  * Funcao: cria e configura todos os check box de visibilidade de percurso
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void configuraCheckBox()
  {
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)
    {
      visualizadoresDePercurso[indice] = new CheckBox();
      visualizadoresDePercurso[indice].setTooltip(new Tooltip("Altera Visibilidade Do Percurso"));//informa o uso do check box
      visualizadoresDePercurso[indice].setLayoutX(220);//configura a posicao do checkBox
      visualizadoresDePercurso[indice].setLayoutY(60 + indice * 80);
      visualizadoresDePercurso[indice].setId(String.format("visualizadorDePercursoCarro%d", indice));//adiciona id de acordo com indice
      
      int indiceDoCarro = indice;//adiciona o listener ao checkBox
      visualizadoresDePercurso[indice].setOnAction(event -> gerenciador.alterarVisibilidadeDoPercurso(indiceDoCarro,
        visualizadoresDePercurso[indiceDoCarro].isSelected()));
    }//fim do for
     
    painelDeControle.getChildren().addAll(visualizadoresDePercurso);//adiciona os checkBoxes ao painel de controle
  }//fim do configuraCheckBox

  /* ***************************************************************
  * Metodo: configuraBotaoReset
  * Funcao: configura o botao de resetar o programa
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  private void configuraBotaoReset()
  {
    botaoReset.setText("RESET");
    botaoReset.setTooltip(new Tooltip("Reinicia A Simulação"));//informa o uso do button
    botaoReset.setLayoutY(10);//muda posicao e tamanho do botao
    botaoReset.setLayoutX(60);
    botaoReset.setPrefWidth(120);
    botaoReset.setPrefHeight(30);
    
    botaoReset.setOnAction(event -> resetProgram());//adiciona um listener ao botao
    
    painelDeControle.getChildren().add(botaoReset);//adiciona o botao ao painel principal
  }//fim do configuraBotaoReset
  
  /* ***************************************************************
  * Metodo: resetProgram
  * Funcao: reseta os valores dos controles, as imagens de percurso e as threads do programa
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */  
  private void resetProgram()
  {
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)
    {
      slidersDeVelocidade[indice].setValue(10);//altera os controles para os valores iniciais
      visualizadoresDePercurso[indice].setSelected(false);
    }//fim do for
    
    gerenciador.resetProgram();//reseta as variaveis pertencentes ao gerenciador
  }//fim do resetProgram
}//fim da classe ControladorGeral
