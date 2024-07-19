/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 18/06/2024 - 15:54
* Ultima alteracao.: 04/07/2024 - 16:31
* Nome.............: Transito Automato
* Funcao...........: Gerencia a criacao, configuracao e reinicializacao das threads que representam os carros
*************************************************************** */
package model;

import util.*;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class GerenciadorDeCarros
{
  private final Pane painelPrincipal;//Painel por onde os carros irao se mover
  private final Semaforos semaforos;//contem todos os semaforos
  private final Percursos percursos;//contem os percursos
  private ImageView[] imagensDosPercursos;
  private Carro[] carros;
  
  private final int QUANTIDADE_DE_CARROS;
  
  /* ***************************************************************
  * Metodo: construtor
  * Funcao: inicializa as variaveis necessarias
  * Parametros: quantidadeDeCarros
  * Retorno: nenhum
  *************************************************************** */
  public GerenciadorDeCarros(int quantidadeDeCarros)
  {
    painelPrincipal = new Pane(new ImageView("images/CenarioPrincipal.png"));
    semaforos = new Semaforos(quantidadeDeCarros);//contem os semaforos usados por cada carro
    carros = new Carro[quantidadeDeCarros];
    imagensDosPercursos = new ImageView[quantidadeDeCarros];
    
    percursos = Percursos.INFORMACAO_DE_PERCURSO;//contem o roteiro de percurso de cada carro
    QUANTIDADE_DE_CARROS = quantidadeDeCarros;
  }//fim do construtor
  
  /* ***************************************************************
  * Metodo: inicializarThreads
  * Funcao: inicializa e reinicializa as threads, semaforos e as imagens no painel principal
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void inicializarThreads()
  {
    semaforos.inicializaSemaforos();//inicializa ou reinicializa os semaforos
    
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)//cria as threads, configura e adiciona as imagens ao pane principal
    {
      carros[indice] = new Carro(String.format("images/imgsKart/Kart%d.png", indice), percursos.getPercurso(indice),
        percursos.getConfiguracaoInicial(indice), semaforos.getSemaforos(indice));
      carros[indice].setDaemon(true);
      
      painelPrincipal.getChildren().add(carros[indice].getImagemDoCarro());
    }//fim do for
    
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)
      carros[indice].start();
  }//fim do inicializarThreads
  
  /* ***************************************************************
  * Metodo: resetProgram
  * Funcao: reinicia as threads
  * Parametros: nenhum
  * Retorno: void
  *************************************************************** */
  public void resetProgram()
  {
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)//interrompe cada thread e remove sua imagem do pane
    {
      carros[indice].interrupt();
      painelPrincipal.getChildren().remove(carros[indice].getImagemDoCarro());
      imagensDosPercursos[indice].setVisible(false);
    }//fim do for
    inicializarThreads();
  }//fim do resetProgram
  
  /* ***************************************************************
  * Metodo: configuraPainelPrincipal
  * Funcao: configura o painel principal e retorna a sua referencia
  * Parametros: nenhum
  * Retorno: Pane = painel principal
  *************************************************************** */
  public Pane configuraPainelPrincipal()
  {
    painelPrincipal.setLayoutX(300);//ajusta posicao X do painel principal
    for (int indice = 0; indice < QUANTIDADE_DE_CARROS; indice++)
    {
      imagensDosPercursos[indice] = new ImageView(String.format("images/imgsPercurso/Percurso%d.png", indice));
      imagensDosPercursos[indice].getStyleClass().add("imagensDosPercursosOpacidade");
      imagensDosPercursos[indice].setVisible(false);
      painelPrincipal.getChildren().add(imagensDosPercursos[indice]);//adiciona a imagem dos percursos ao pane
    }//fim do for
    return painelPrincipal;
  }//fim do configuraPainelPrincipal
  
  /* ***************************************************************
  * Metodo: alterarVelocidade
  * Funcao: altera a velocidade de um carro no vetor de acordo com o indice
  * Parametros: indiceDoCarro = indice do carro no vetor, novaVelocidade = velocidade que ira ser usada para sobrescrever a atual
  * Retorno: void
  *************************************************************** */
  public void alterarVelocidade(int indiceDoCarro, int novaVelocidade)
  {
    carros[indiceDoCarro].setVelocidade(novaVelocidade);
  }//fim do alterarVelocidade

  /* ***************************************************************
  * Metodo: alterarVisibilidadeDoPercurso
  * Funcao: muda a visibilidade da imagem de percurso
  * Parametros: indice = indice da imagem, estaSelecionado = verifica se o usuario selecionou ou deselecionou a visibilidade
  * Retorno: void
  *************************************************************** */
  public void alterarVisibilidadeDoPercurso(int indice, boolean estaSelecionado)
  {
    imagensDosPercursos[indice].setVisible(estaSelecionado);
  }//fim do alterarVisibilidadeDoPercurso
}//fim da classe GerenciadorDeCarros
