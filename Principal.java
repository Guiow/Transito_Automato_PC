/* ***************************************************************
* Autor............: Guilherme Oliveira
* Inicio...........: 18/06/2024 - 14:37
* Ultima alteracao.: 18/06/2024 - 15:34
* Nome.............: Transito Automato
* Funcao...........: Classe utilizada para iniciar o programa e exibir a GUI inicial.
*************************************************************** */

import controller.*;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Principal extends Application
{
  /* ***************************************************************
  * Metodo: start
  * Funcao: Inicializa a GUI, carrega e exibe o SceneInicial para o usuario.
  * Parametros: stage = parametro Stage para armazenar e exibir os scenes.
  * Retorno: void
  *************************************************************** */
  @Override
  public void start(Stage stage)
  {
    ControladorGeral controlador = new ControladorGeral();//cria o controlador do programa
    Scene scene = new Scene(controlador, 999, 700);//o scene root
    controlador.inicializarPrograma();//inicializa o programa
    
    stage.setY(0);
    stage.setTitle("Transito Automato");
    stage.setScene(scene);
    stage.show();
  }//fim do start
  
  /* ***************************************************************
  * Metodo: main
  * Funcao: Utilizado para começar o programa quando o programa for compilado e depois executado
  * Parametros: args = argumentos para compilacao do programa, nao necessario nessa situacao
  * Retorno: void
  *************************************************************** */
  public static void main(String[] args)
  {
    launch();
  }//fim do main
}//fim da classe Principal
