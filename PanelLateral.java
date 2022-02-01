 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;


public class PanelLateral extends JPanel {
	

	/**
	 * Las dimensiones de cada mosaico en la vista previa de la siguiente pieza.
	 */
	private static final int TamTitulo = Tablero.tamFig >> 1;
	
	/**  
	 * El ancho del sombreado en cada mosaico en la vista previa de la siguiente pieza.
	 */
	private static final int anchoSombra = Tablero.anchSombra>> 1;
	
	/**
	 * El número de filas y columnas en la ventana de vista previa. Ajustado a
          * 5 porque podemos mostrar cualquier pieza con algún tipo de relleno.
	 */
	private static final int cantFig = 5;
	
	/**
	 * El centro X del cuadro de vista previa de la siguiente pieza.
	 */
	private static final int CENTRO_CUADRADO_X = 130;
	
	/**
	 * El centro Y del cuadro de vista previa de la siguiente pieza.
	 */
	private static final int CENTRO_CUADRADO_Y = 65;
	
	/**
	 *El tamaño del cuadro de vista previa de la siguiente pieza.
	 */
	private static final int tamCuadrado = (TamTitulo * cantFig >> 1);
	
	/**
	 * El número de píxeles usados en pequeños recuadros 
	 */
	private static final int pequeRecuad= 20;
	
	/**
	 * El número de píxeles utilizados en recuadros grandes.
	 */
	private static final int recuadGrand = 40;
	
	/**
	 * La coordenada y de la categoría de estadísticas.
	 */
	private static final int Estadisticas = 175;
	
	/**
	 * La coordenada y de la categoría de controles.
	 */
	private static final int Controles = 300;
	
	/**
	 * El número de píxeles para compensar entre cada cadena.
	 */
	private static final int PasoTexto = 25;
	
	/**
	 * La fuente pequeña.
	 */
	private static final Font FuentePequenia = new Font("Tahoma", Font.BOLD, 11);
	
	/**
	 * La fuente grande.
	 */
	private static final Font FuenteGrande = new Font("Tahoma", Font.BOLD, 13);
	
	/**
	 * El color para dibujar el texto y el cuadro de vista previa en.
	 */
	private static final Color DibColor= new Color(128, 192, 128);
	
	/**
	 * La instancia de Tetris.
	 */
	private Tetris tetris;
	
	
	public PanelLateral(Tetris tetris) {
		this.tetris = tetris;
		
		setPreferredSize(new Dimension(200,Tablero.PANEL_ALTO));
		setBackground(Color.BLACK);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Establece el color para dibujar.
		g.setColor(DibColor);
		
		/*
		 * Esta variable almacena la coordenada y actual de la cadena.
                    * De esta manera podemos reordenar, agregar o eliminar nuevas cadenas si es necesario
                    * sin necesidad de cambiar las otras cadenas 
		 */
		 int almacena; 
			
		//Dibuja la categoría "Estadísticas".
		
		g.setFont(FuenteGrande);
		g.drawString("Estadísticas", pequeRecuad, almacena = Estadisticas);
		g.setFont(FuentePequenia);
		g.drawString("Nivel: " + tetris.getNivel(), recuadGrand, almacena += PasoTexto);
		g.drawString("Score: " + tetris.getPuntos(), recuadGrand, almacena += PasoTexto);
		
		 //dibuja la categoría "Controles".
		g.setFont(FuenteGrande);
		g.drawString("Controles", pequeRecuad, almacena = Controles);
		g.setFont(FuentePequenia);
		g.drawString("A - Moverse a la izquierda", recuadGrand, almacena += PasoTexto);
		g.drawString("D - Moverse a la derecha", recuadGrand, almacena += PasoTexto);
		g.drawString("Q - Girar en sentido antihorario", recuadGrand, almacena += PasoTexto);
		g.drawString("E - Rotar las agujas del reloj", recuadGrand, almacena += PasoTexto);
		g.drawString("S - Bajar", recuadGrand, almacena += PasoTexto);
		g.drawString("P - Pausar Juego", recuadGrand, almacena += PasoTexto);
		
		/*
		 * Dibuja el cuadro de vista previa de la siguiente pieza.
		 */
		g.setFont(FuenteGrande);
		g.drawString("Next Piece:", pequeRecuad, 70);
		g.drawRect(CENTRO_CUADRADO_X - tamCuadrado, CENTRO_CUADRADO_Y - tamCuadrado, tamCuadrado * 2, tamCuadrado * 2);
		
		/*
		 * Dibuja una vista previa de la siguiente pieza que se generará. El código es bastante
                                             *  idéntico al código de dibujo en el tablero, solo más pequeño y centrado, más bien
                                             * que restringido a una cuadrícula.
		 */
		Figura  tipo = tetris.getSigFig();
		if(!tetris.GameOver() && tipo != null) {
			/*
			 * Obtenga las propiedades de tamaño de la pieza actual.
			 */
			int cols = tipo.getCols();
			int filas = tipo.getFilas();
			int dimension = tipo.getDimension();
		
			/*
			 *Calcula la esquina superior izquierda (origen) de la pieza.
			 */
			int startX = (CENTRO_CUADRADO_X - (cols * TamTitulo / 2));
			int startY = (CENTRO_CUADRADO_Y - (filas * TamTitulo / 2));
		
			/*
			* Obtenga las inserciones para la vista previa. El valor por defecto
                                                                   * La rotación se usa para la vista previa, por lo que solo usamos 0.
			 */
			int top = tipo.getRecuadroSupe(0);
			int left = tipo.getRecuadroIzq(0);
		
			/*
			 * Recorre la pieza y dibuja sus fichas en la vista previa.
			 */
			for(int fila = 0; fila < dimension; fila++) {
				for(int col = 0; col < dimension; col++) {
					if(tipo.esFig(col, fila, 0)) {
						vistPrevia(tipo, startX + ((col - left) * TamTitulo), startY + ((fila - top) * TamTitulo), g);
					}
				}
			}
		}
	}
	
	/**
	 * Dibuja un mosaico en la ventana de vista previa.
	 */
	private void vistPrevia(Figura tipo, int x, int y, Graphics g) {
		/*
		 *Rellena todo el mosaico con el color base.
		 */
		g.setColor(tipo.getColorBase());
		g.fillRect(x, y, TamTitulo, TamTitulo);
		
		/*
		 * Rellena los bordes inferior y derecho del mosaico con el color de sombreado oscuro.
		 */
		g.setColor(tipo.getColorOscuro());
		g.fillRect(x, y + TamTitulo - anchoSombra, TamTitulo, anchoSombra);
		g.fillRect(x + TamTitulo - anchoSombra, y, anchoSombra, TamTitulo);
		
		/*
		 *Rellena los bordes superior e izquierdo con el sombreado claro. Dibujamos una sola línea
                    * para cada fila o columna en lugar de un rectángulo para que podamos dibujar un bonito
                    * Mirando en diagonal donde se encuentran las sombras claras y oscuras.
		 */
		g.setColor(tipo.getColorClaro());
		for(int i = 0; i < anchoSombra; i++) {
			g.drawLine(x, y + i, x + TamTitulo - i - 1, y + i);
			g.drawLine(x + i, y, x + i, y + TamTitulo - i - 1);
		}
	}
	
}
